<script setup lang="ts">
import IconEllipse from './icons/IconEllipse.vue';
import { ref, watch, computed } from 'vue';
import TasksHandler from '@/api/TasksHandler';

const props = defineProps<{
  taskID: number;
  taskName: string;
  taskPriority?: string | null;
  parentTaskID: number | null;
  taskPosition: number;
  dateCompleted: Date | null;
  dueDate: Date | null;
  createdAt: Date;
}>();

const customFormatDate = (date: Date | null): string => {
  if (!date) return '';
  const day = String(date.getDate()).padStart(2, '0');
  const month = new Intl.DateTimeFormat('default', { month: 'short' }).format(date);
  const year = date.getFullYear();
  return `${day} ${month} ${year}`;
};

const emit = defineEmits(['task-clicked']);

const openTaskDetails = () => {
  emit('task-clicked', props.taskID);
};

const priorityMap: Record<string, number> = {
  Low: 1,
  Medium: 2,
  High: 3,
};

const localDateCompleted = ref<Date | null>(props.dateCompleted ?? null);

const getPriorityString = (priority: number | null): string | null => {
  const priorityMapReverse: Record<number, string> = {
    1: 'Low',
    2: 'Medium',
    3: 'High',
  };
  return priority === null ? null : priorityMapReverse[priority] || null;
};

watch(() => props.dateCompleted, (newVal) => {
  localDateCompleted.value = newVal ?? null;
});

const numericPriority = priorityMap[props.taskPriority as string] || null;

const isChecked = computed({
  get: () => localDateCompleted.value !== null, 
  set: async (val: boolean) => {
    const newDate = val ? new Date() : null;
    try {
      const updatedTask: Record<string, any> = {};

      if (props.taskID) updatedTask.taskID = props.taskID;
      if (props.taskName) updatedTask.taskName = props.taskName;
      if (numericPriority !== null) updatedTask.taskPriority = numericPriority.toString();
      if (props.dueDate) updatedTask.dueDate = props.dueDate;
      if (newDate !== null) updatedTask.dateCompleted = newDate;

      const jwtToken = localStorage.getItem('jwtToken');
      if (jwtToken) {
        console.log("Updating task with newDate:", newDate);

        await TasksHandler.updateTask(updatedTask, jwtToken);
        console.log(updatedTask);

        localDateCompleted.value = newDate;
        console.log("Task updated. New dateCompleted:", newDate);
      }
    } catch (error) {
      console.error('Error updating task:', error);
    }
  }
});
</script>

<template>
  <article class="task-item":class="{'priority-high': taskPriority === 'High','priority-medium': taskPriority === 'Medium','priority-low': taskPriority === 'Low' ,'task-completed': isChecked}">
    <section class="task-row">
      <input type="checkbox" class="checkbox" aria-label="Mark task as complete" v-model="isChecked"/>
      <section class="task-content">
        <header class="task-header">
          <h2 class="task-title">{{ taskName }}</h2>
          <time class="due-date due-date-red" :datetime="dueDate?.toISOString()">
            {{ customFormatDate(dueDate) }}
          </time>
          <p class="priority-label">{{ taskPriority }}</p>
        </header>
      </section>
      <button class="expand-button" @click="openTaskDetails">
        <IconEllipse />
        <IconEllipse />
        <IconEllipse />
      </button>
    </section>
  </article>
</template>

<style scoped>
.task-item {
  background: #fff;
  border: 2pt solid #d1d5db;
  border-radius: 8pt;
  padding: 12pt;
  margin: 1em 1em 0.5em 0.5em;
  box-shadow: 0 2pt 4pt rgba(0, 0, 0, 0.1);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  gap: 8pt;
}

.priority-high {
  border-color: red;
  border-width: 2pt;
  --priority-color: red;
}

.priority-medium {
  border-color: orange;
  border-width: 2pt;
  --priority-color: orange;
}

.priority-low {
  border-color: rgb(0, 255, 0);
  border-width: 2pt;
  --priority-color: rgb(0, 255, 0);
}

.priority-label {
  font-weight: 500;
  font-size: 0.9rem;
  color: var(--priority-color, inherit);
}

.task-row {
  display: flex;
  align-items: flex-start;
  gap: 12pt;
  margin-bottom: 8pt;
}

.checkbox {
  appearance: none;
  height: 1.5em;
  width: 1.5em;
  border-radius: 4pt;
  border: 1pt solid #d1d5db;
  cursor: pointer;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.checkbox:checked {
  background-color: #9333ea;
  border: none;
}

.checkbox:checked::after {
  content: '✔';
  color: #fff;
  font-size: 8pt;
  line-height: 12pt;
}

.task-content {
  flex: 1;
  min-width: 0;
}

.task-header {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  gap: 6pt;
}

.task-title {
  font-size: 1.1rem;
  font-weight: 500;
  overflow: hidden;
  color: var(--heading-purple);
  margin: 0;
}

.checkbox:checked + .task-content .task-title {
  text-decoration: line-through;
  color: #6b7280;
}

.due-date {
  font-size: 10pt;
  color: #555;
  flex-shrink: 0;
  font-weight: 500;
}

.due-date-red {
  color: #ef4444;
}

time {
  color: #6b7280;
  font-size: 0.9rem;
  display: block;
}

.expand-button {
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  opacity: 0;
}

.expand-button:hover {
  background: var(--button-hover-bg);
  opacity: 1; 
}
.task-completed {
  opacity: .3;
  z-index: 0;
}


</style>