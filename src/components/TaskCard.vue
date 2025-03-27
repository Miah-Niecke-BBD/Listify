<script setup lang="ts">
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
  const month = date.toLocaleString('default', { month: 'long' });
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
          <time class="due-date due-date-red" datetime="2025-03-22"> Due Date: {{ customFormatDate(dueDate) }} </time>
          <time datetime="2025-03-20"> Created: {{ customFormatDate(createdAt) }} </time>
        </header>
      </section>
      <button class="expand-button" @click="openTaskDetails" aria-label="Expand task details">▼</button>
    </section>
  </article>
</template>

<style scoped>
* {
  font-family: ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-seri;
}

.task-item {
  background: #fff;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 24px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition:
    box-shadow 0.2s ease-in-out,
    transform 0.2s ease-in-out;
  display: flex;
  flex-direction: column;
  gap: 8px;
}


.task-item:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}


.task-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 8px;
}


.checkbox {
  appearance: none;
  height: 20px;
  width: 20px;
  border-radius: 4px;
  border: 1px solid #d1d5db;
  cursor: pointer;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s ease-in-out;
}


.checkbox:checked {
  background-color: #9333ea;
  border: none;
}


.checkbox:checked::after {
  content: "✔";
  color: #fff;
  font-size: 14px;
  line-height: 20px;
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
  gap: 8px;
}

.task-title {
  font-size: 1.1rem;
  font-weight: 500;
  overflow: hidden;
  color: #333;
  margin: 0;
}

.checkbox:checked + .task-content .task-title {
  text-decoration: line-through;
  color: #6b7280;
}

.due-date {
  font-size: 0.9rem;
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

time:hover {
  color: #333;
  font-weight: 500;
}

.expand-button {
  background: #ddd;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: background 0.2s ease-in-out;
}

.expand-button:hover {
  background: #bbb;
}
</style>
