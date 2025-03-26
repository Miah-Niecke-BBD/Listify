<script setup lang="ts">
import { defineProps, defineEmits, ref } from 'vue';

interface Task {
  id: string;
  title: string;
  completed: boolean;
  description?: string;
  dueDate?: string;
  priority?: string | null;
  project?: string;
}

const props = defineProps<{
  tasks: Task[];
  projects: { id: string; name: string }[];
}>();

const emit = defineEmits(['toggle-completion', 'add-task', 'quick-add-task']);

const newTaskInput = ref('');

function toggleCompletion(id: string) {
  emit('toggle-completion', id);
}

function openAddTaskDialog() {
  emit('add-task');
}

function quickAddTask() {
  if (newTaskInput.value.trim()) {
    const newTask: Task = {
      id: `task-${Date.now()}`, // Fixed syntax error
      title: newTaskInput.value,
      completed: false,
      priority: null
    };
    emit('quick-add-task', newTask);
    newTaskInput.value = '';
  }
}

function formatDate(dateString?: string) {
  if (!dateString) return '';
  try {
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return 'Invalid Date'; // Handle invalid dates
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);

    if (date.toDateString() === today.toDateString()) {
      return 'Today';
    }
    if (date.toDateString() === tomorrow.toDateString()) {
      return 'Tomorrow';
    }
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: date.getFullYear() !== today.getFullYear() ? 'numeric' : undefined
    });
  } catch (error) {
    return 'Invalid Date';
  }
}

function getProjectName(projectId?: string) {
  if (!projectId) return null;
  const project = props.projects.find(p => p.id === projectId);
  return project ? project.name : null;
}
</script>

<template>
  <div class="task-list">
    <h2 class="my-list-header">My Tasks</h2>

    <div class="task-input-container">
      <div class="input-with-button">
        <input
          v-model="newTaskInput"
          type="text"
          placeholder="Add a new task..."
          class="task-input"
          @keyup.enter="quickAddTask"
          aria-label="New task input"
        >
        <button
          @click="quickAddTask"
          class="quick-add-btn"
          aria-label="Quick add task"
        >
          +
        </button>
      </div>
    </div>

    <div v-if="tasks.length === 0" class="empty-state">
      <div class="empty-icon">📋</div>
      <h3>No tasks yet</h3>
      <p>Add your first task to get started</p>
      <button @click="openAddTaskDialog" class="add-first-task-btn">
        + Add Your First Task
      </button>
    </div>

    <div v-else class="task-items">
      <article
        v-for="task in tasks"
        :key="task.id"
        class="task-card"
        :class="{ 'completed': task.completed }"
      >
        <section class="task-content">
          <button
            @click="() => toggleCompletion(task.id)"
            class="task-toggle"
            :class="{ 'checked': task.completed }"
            :aria-label="task.completed ? 'Mark task as incomplete' : 'Mark task as complete'"
          >
            <span v-if="task.completed" class="check-mark">✓</span>
          </button>
          <section class="task-details">
            <h3 class="task-title" :class="{ 'completed': task.completed }">
              {{ task.title }}
            </h3>
            <p v-if="task.description" class="task-description">
              {{ task.description }}
            </p>
            <section class="task-meta">
              <span v-if="task.dueDate" class="task-date">
                <span class="meta-icon">📅</span>
                {{ formatDate(task.dueDate) }}
              </span>
              <span
                v-if="task.priority"
                class="task-priority"
                :class="task.priority"
              >
                <span class="meta-icon">🔔</span>
                {{ task.priority }}
              </span>
              <span
                v-if="getProjectName(task.project)"
                class="task-project"
              >
                <span class="meta-icon">📁</span>
                {{ getProjectName(task.project) }}
              </span>
            </section>
          </section>
        </section>
      </article>
    </div>
  </div>
</template>

<style scoped>
.task-list {
  max-width: 800px;
  margin: 0 auto;
}

.my-list-header {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 20px;
  background: linear-gradient(135deg, #000000 0%, #333333 100%); /* Improved gradient */
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  display: inline-block;
}

.task-input-container {
  margin-bottom: 24px;
}

.input-with-button {
  display: flex;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  background-color: var(--card-bg);
}

.task-input {
  flex: 1;
  padding: 12px 16px;
  border: none;
  font-size: 14px;
  outline: none;
}

.quick-add-btn {
  background-color: var(--bg-secondary);
  border: none;
  width: 40px;
  font-size: 18px;
  color: var(--text-secondary);
  transition: all 0.2s;
  cursor: pointer;
}

.quick-add-btn:hover {
  background-color: var(--primary-color);
  color: white;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: var(--text-secondary);
  background-color: var(--card-bg);
  border-radius: 8px;
  border: 1px dashed var(--border-color);
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-state h3 {
  font-size: 18px;
  color: var(--text-primary);
}

.empty-state p {
  font-size: 14px;
  margin-bottom: 16px;
}

.add-first-task-btn {
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 6px;
  padding: 10px 18px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.add-first-task-btn:hover {
  background-color: var(--primary-dark);
}

.task-items {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.task-card {
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
  background-color: var(--card-bg);
  transition: all 0.2s ease;
  box-shadow: var(--shadow-sm);
}

.task-card:hover {
  box-shadow: var(--shadow-md);
}

.task-card.completed {
  opacity: 0.8;
  background-color: var(--bg-secondary);
}

.task-content {
  display: flex;
  align-items: flex-start;
}

.task-toggle {
  width: 22px;
  height: 22px;
  border: 2px solid var(--border-color);
  border-radius: 50%;
  margin-right: 16px;
  margin-top: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  cursor: pointer;
  transition: all 0.2s;
}

.task-toggle:hover {
  border-color: var(--primary-color);
  background-color: var(--hover-color);
}

.task-toggle.checked {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.check-mark {
  color: white;
  font-size: 12px;
}

.task-details {
  flex: 1;
}

.task-title {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 500;
  color: var(--text-primary);
}

.task-title.completed {
  text-decoration: line-through;
  color: var(--text-secondary);
}

.task-description {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.5;
}

.task-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  font-size: 12px;
}

.task-date, .task-priority, .task-project {
  display: flex;
  align-items: center;
  padding: 4px 8px;
  border-radius: 4px;
  background-color: var(--bg-secondary);
  color: var(--text-secondary);
}

.meta-icon {
  margin-right: 4px;
  font-size: 12px;
}

.task-priority.high {
  background-color: var(--priority-high);
  color: var(--priority-high-text);
}

.task-priority.medium {
  background-color: var(--priority-medium);
  color: var(--priority-medium-text);
}

.task-priority.low {
  background-color: var(--priority-low);
  color: var(--priority-low-text);
}

/* Enhanced responsiveness */
@media (max-width: 768px) {
  .task-list {
    padding: 0 16px; /* Add padding for smaller screens */
  }

  .task-meta {
    flex-direction: column;
    gap: 8px;
  }

  .input-with-button {
    width: 100%;
  }

  .quick-add-btn {
    min-width: 40px;
  }

  .task-card {
    padding: 12px; /* Slightly reduce padding */
  }
}

@media (max-width: 480px) {
  .task-input {
    font-size: 13px; /* Slightly smaller text */
  }

  .quick-add-btn {
    width: 36px; /* Slightly smaller button */
    font-size: 16px;
  }

  .add-first-task-btn {
    padding: 8px 16px; /* Adjust button size */
    font-size: 13px;
  }

  .task-title {
    font-size: 14px; /* Smaller title */
  }

  .task-description {
    font-size: 13px; /* Smaller description */
  }
}
</style>
