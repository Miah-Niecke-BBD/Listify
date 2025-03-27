<script setup lang="ts">
import { ref, computed, defineProps, watch } from 'vue';
import { GetDueTasks } from '@/api/Calendar';
import type { dueTasks } from '@/models/DueTasks';
import { jwtToken } from '@/models/JWTToken';
import { GetTeams } from '@/api/SidebarApi';
import type { TeamInterface } from '@/models/TeamInterface';
import { useRoute } from 'vue-router';


const route = useRoute();

const tasksMap = ref<Record<string, dueTasks[]>>({});
const currentOffset = ref(0);
const today = new Date();


const getFormattedDate = (date: Date, format: 'date' | 'weekday' = 'date'): string => {
  const normalizedDate = new Date(date);
  normalizedDate.setHours(0, 0, 0, 0);

  const options = format === 'date'
    ? { month: 'short', day: 'numeric', year: 'numeric' }
    : { weekday: 'long' };
  return normalizedDate.toLocaleDateString('en-UK', options);
};

const getDateFromOffset = (offset: number): Date => {
  const date = new Date(today);
  date.setDate(today.getDate() + offset);
  date.setHours(0, 0, 0, 0);
  return date;
};

const visibleDays = computed(() => {
  const days = [];
  for (let i = 0; i < 7; i++) {
    const offset = currentOffset.value + i;
    const date = getDateFromOffset(offset);
    const fullDate = date.toISOString().split('T')[0];

    if (!tasksMap.value[fullDate]) tasksMap.value[fullDate] = [];

    days.push({
      dateLabel: getFormattedDate(date, 'date'),
      dayName: getFormattedDate(date, 'weekday'),
      fullDate,
      tasks: tasksMap.value[fullDate]
    });
  }
  return days;
});

const customFormatDate = (date: Date | string | null|undefined): string => {
  if (!date) return 'No date';
  const parsedDate = typeof date === 'string' ? new Date(date) : date;
  return isNaN(parsedDate.getTime())
    ? 'Invalid date'
    : `${String(parsedDate.getDate()).padStart(2, '0')} ${new Intl.DateTimeFormat('default', { month: 'short' }).format(parsedDate)} ${parsedDate.getFullYear()}`;
};

const loadTasksForTeam = async (teamID: string) => {
  const dueTasksResponse: dueTasks[] | null = await GetDueTasks(jwtToken, teamID);
  dueTasksResponse?.forEach((task) => {
    const taskDate = new Date(task.dueDate);
    taskDate.setHours(0, 0, 0, 0);
    const taskDay = taskDate.toISOString().split('T')[0];
    if (!tasksMap.value[taskDay]) tasksMap.value[taskDay] = [];
    tasksMap.value[taskDay].push(task);
  });
};

const loadAllTasks = async () => {
  try {
    tasksMap.value = {};
    if (route.name === 'MyCalendar') {
      const response: TeamInterface[] | null = await GetTeams(jwtToken);
      const teams = ref<TeamInterface[]>([]);
      if (response) {
        teams.value = response;
      }
      for (const team of teams.value) {
        await loadTasksForTeam(team.teamID.toString());
      }
    } else if (route.name === 'calendar') {
      await loadTasksForTeam(route.params.id as string);
    }
  } catch (error) {
    console.error('Error getting due tasks:', error);
  }
};

watch(
  () => route.params,
  loadAllTasks,
  { immediate: true }
);

const nextWeek = () => currentOffset.value += 7;
const prevWeek = () => currentOffset.value -= 7;


</script>

<template>
  <main class="container">
    <header class="top-bar">
      <button class="nav-button" id="left-btn" @click="prevWeek" :disabled="currentOffset === 0">Previous 7 Days</button>
      <button class="nav-button" id="right-btn" @click="nextWeek">Next 7 Days</button>
    </header>
    <h1>Upcoming</h1>
    <section class="calendar-scroll">
      <article v-for="day in visibleDays" :key="day.fullDate" class="day-column">
        <header class="day-header">
          <h3>
            <time :datetime="day.fullDate">{{ day.dateLabel }}</time>
            {{ day.dayName }}
          </h3>
        </header>
        <ul class="task-list">
          <li v-for="task in day.tasks" :key="task.taskID" class="task-item" >
            <section class="task-content">
              <header class="task-header">
                <h2 class="task-title">{{ task.taskName }}</h2>
                <time class="due-date" :datetime="task.dueDate">{{ customFormatDate(task.dueDate) }}</time>
              </header>
            </section>
          </li>
        </ul>
      </article>
    </section>
  </main>

</template>

<style scoped>
.container {
  margin-left: 1em;
  display: flex;
  flex-direction: column;
  max-width: max-content;
  overflow-x: auto;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

h1 {
  margin-bottom: 1rem;
  margin-left: 2rem;
  font-size: 18pt;
  text-align: left;
  color: var(--heading-purple);
}

.calendar-scroll {
  display: flex;
  gap: 1rem;
  padding-bottom: 1rem;
  overflow-x: auto;
  width: 83vw;
}

.day-column {
  display: flex;
  flex-direction: column;
  width: 15rem;
  height: 83vh;
  border: 1pt solid var(--card-bg);
  border-radius: 0.5rem;
  padding: 1rem;
  background-color: var(--background-color);
  box-shadow: 0.05rem 0.05rem 0.05rem var(--card-bg);
  flex-shrink: 0;
}

.day-header h3 {
  display: flex;
  flex-direction: column;
  text-align: center;
  border-bottom: solid 1pt black;
  padding-bottom: 1em;
}

.task-list {
  display: flex;
  flex-direction: column;
  list-style: none;
  overflow-y: auto;
}

.nav-button {
  padding: 0.5rem 1rem;
  background-color: var(--primary-color);
  color: var(--background-color);
  border: none;
  border-radius: 0.375rem;
  cursor: pointer;
  font-size: 1rem;
  margin-top: 1em;
  width: 9rem;
}

.task-item {
  background: #fff;
  border: 0.0625rem solid #d1d5db;
  border-radius: 0.5rem;
  padding: 0.75rem;
  margin: 1rem 1rem 0.5rem 0.5rem;
  box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.1);
  display: flex;
  gap: 0.5rem;
}

.task-content {
  flex: 1;
  min-width: 0;
}

.task-header {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

.task-title {
  font-size: 14pt;
  font-weight: 500;
  color: var(--heading-purple);
}

.due-date {
  font-size: 9pt;
  color: #555;
  font-weight: 500;
  margin-left: 1em;
}

.nav-button:disabled {
  background-color: var(--background-color);
  cursor: not-allowed;
}

.nav-button:hover:not(:disabled) {
  background-color: #005a99;
}




</style>
