<script setup lang="ts">
import { ref, computed, defineProps, watch } from 'vue';
import { GetDueTasks } from '@/api/CalendarApi';
import type { dueTasks } from '@/models/DueTasks';
import { jwtToken } from '@/models/JWTToken';
import type { TeamInterface } from '@/models/TeamInterface';
import { useRoute } from 'vue-router';

const dueTask = ref<dueTasks[]>([]);

const route = useRoute()

interface Day {
  dateLabel: string;
  dayName: string;
  fullDate: string;
  tasks: dueTasks[];
}

const props = defineProps<{
  teams: TeamInterface[];
}>();

const tasksMap = ref<Record<string, dueTasks[]>>({});
const currentOffset = ref(0);

const today = new Date();

function formatDate(date: Date): string {
  return date.toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric'
  });
}

function getDayName(date: Date): string {
  return date.toLocaleDateString('en-UK', { weekday: 'long' });
}

function getDateFromOffset(offset: number): Date {
  const date = new Date(today);
  date.setDate(today.getDate() + offset);
  return date;
}

const visibleDays = computed(() => {
  const days: Day[] = [];
  for (let i = 0; i < 7; i++) {
    const offset = currentOffset.value + i;
    const date = getDateFromOffset(offset);
    const fullDate = date.toISOString().split('T')[0];

    if (!tasksMap.value[fullDate]) {
      tasksMap.value[fullDate] = [];
    }

    days.push({
      dateLabel: formatDate(date),
      dayName: getDayName(date),
      fullDate,
      tasks: tasksMap.value[fullDate]
    });
  }
  return days;
});

function nextWeek() {
  currentOffset.value += 7;
}

function prevWeek() {
  if (currentOffset.value >= 7) {
    currentOffset.value -= 7;
  }
}

const yearLabel = computed(() => {
  const firstDay = getDateFromOffset(currentOffset.value);
  return firstDay.getFullYear();
});


let hasFetched = false;

async function loadAllTasks(teams: TeamInterface[]) {
  try {
   if(route.name === 'MyCalendar'){
    for (const team of teams) {
        const dueTasksResponse: dueTasks[] | null = await GetDueTasks(jwtToken, team.teamID.toString());

        if (dueTasksResponse) {
          dueTask.value.push(...dueTasksResponse);
          dueTasksResponse.forEach((task) => {
            const taskDate = new Date(task.dueDate).toISOString().split('T')[0];
            if (!tasksMap.value[taskDate]) {
              tasksMap.value[taskDate] = [];
            }
            tasksMap.value[taskDate].push(task);
          });
        }
      }
   }else if(route.name === 'calendar'){
        const teamID = route.params.id as string;
        console.log(typeof teamID)
        const dueTasksResponse: dueTasks[] | null = await GetDueTasks(jwtToken, teamID.toString());

        if (dueTasksResponse) {
          dueTask.value.push(...dueTasksResponse);
          dueTasksResponse.forEach((task) => {
            const taskDate = new Date(task.dueDate).toISOString().split('T')[0];
            if (!tasksMap.value[taskDate]) {
              tasksMap.value[taskDate] = [];
            }
            tasksMap.value[taskDate].push(task);
        });
        
      }
   }
      
  } catch (error) {
    console.error('Error getting due tasks:', error);
  }
}


watch(
  () => props.teams,
  async (newTeams) => {
   
      await loadAllTasks(newTeams);
  }
);

watch(
  () => route.fullPath,
  async () => {
    dueTask.value = [];
    tasksMap.value = {};
    await loadAllTasks(props.teams);
  }
);

</script>



<template>
  <main class="container">
    <header class="top-bar">
      <button class="nav-button" id="left-btn" @click="prevWeek" :disabled="currentOffset === 0">
        Previous 7 Days
      </button>

      <button class="nav-button" id="right-btn" @click="nextWeek">
        Next 7 Days
      </button>
    </header>
    <h1>Upcoming</h1>
    <section class="calendar-scroll">
      <article
        v-for="day in visibleDays"
        :key="day.fullDate"
        class="day-column"
        aria-labelledby="day-{{ day.fullDate }}"
      >
        <header class="day-header">
          <h3 id="day-{{ day.fullDate }}">
            <time :datetime="day.fullDate">{{ day.dateLabel }}</time> <pre>{{ day.dayName }}</pre>
          </h3>
        </header>
        <ul class="task-list">
          <li v-for="task in day.tasks" :key="task.taskID">
            <section class="taskCard"> 
              <ul>
                <li>{{ task.taskName }}</li>
              </ul>
            </section> 
          </li>
        </ul>

      </article>
    </section>
  </main>
</template>

<style scoped>
.container {
  display: flex;
  flex-flow: column;
  padding: 1rem;
}

.top-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
}

.year-label {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  font-size: 1.2rem;
  font-weight: bold;
  color: var(--heading-purple);
}

h1{
  margin-bottom: 1rem;
  text-align: center;
  color:var(--heading-purple);
}

h3{
  display: flex;
  flex-direction: column;
  text-align: center;
  gap: 5pt;
}

h3 pre{
  font-size: 12pt;
  padding-bottom: 5pt;
  border-bottom: solid 1pt black;
}

.calendar-scroll {
  display: flex;
  flex-wrap: nowrap;
  gap: 1rem;
  overflow-x: auto;
  overflow-y: hidden;
  padding-bottom: 1rem;
  height: 600px;
}

.day-column {
  width: 200px;
  height: 550px;
  border: 1px solid var(--card-bg);
  border-radius: 8px;
  padding: 1rem;
  background-color: var(--background-color);
  box-shadow: .1em .1em .1em var(--card-bg);
  flex-shrink: 0;
}

.day-header {
  margin-bottom: 2rem;
  font-size: 0.95rem;
  font-size: 14pt;
}

.task-list {
  display: flex;
  flex-flow: wrap column;
  list-style: none;
  overflow-y:auto;
}


.nav-button {
  padding: 8px 16px;
  background-color: var(--primary-color);
  color: var(--background-color);
  border: none;
  border-radius: 6pt;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s ease;
}

.taskCard{
  background: #fff;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 24px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.taskCard ul{
 list-style: none;
}

.nav-button:disabled {
  background-color: var(--background-color);
  cursor: not-allowed;
} 

.nav-button:hover:not(:disabled) {
  background-color: #005a99;
}
</style>
