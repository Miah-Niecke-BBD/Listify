<script setup lang="ts">
import IconEllipse from './icons/IconEllipse.vue';

const props = defineProps<{
  taskID: number
  taskName: string;
  parentTaskID: number | null;
  taskPosition: number;
  dueDate: Date | null;
  createdAt: Date;
}>();

const customFormatDate = (date: Date | null): string => {
  if (!date) return 'No date';
  const day = String(date.getDate()).padStart(2, '0');
  const month = new Intl.DateTimeFormat('default', { month: 'short' }).format(date);
  const year = date.getFullYear();
  return `${day} ${month} ${year}`;
};


const emit = defineEmits(["task-clicked"]);

const openTaskDetails = () => {
  emit("task-clicked", props.taskID)
}
</script>

<template>
  <article class="task-item">
    <section class="task-row">
      <input type="checkbox" class="checkbox" aria-label="Mark task as complete" />
      <section class="task-content">
        <header class="task-header">
          <h2 class="task-title">{{ taskName }}</h2>
          <time class="due-date due-date-red" datetime="2025-03-22">{{ customFormatDate(dueDate) }} </time>
        </header>
      </section>
      <button class="expand-button" @click="openTaskDetails" ><IconEllipse/><IconEllipse/><IconEllipse/></button>
    </section>
  </article>
</template>

<style scoped>

.task-item {
  background: #fff;
  border: 1pt solid #d1d5db;
  border-radius: 8pt;
  padding: 12pt;
  margin: 1em 1em .5em .5em;
  box-shadow: 0 2pt 4pt rgba(0, 0, 0, 0.1);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  gap: 8pt;
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
  content: "✔";
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
</style>
