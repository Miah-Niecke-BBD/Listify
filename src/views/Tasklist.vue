<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue';

interface Task {
  taskID: number;
  taskName: string;
  createdAt?: string;
  projectID: number;
  completed: boolean;
}

const tasks = ref<Task[]>([]);
const newTaskInput = ref('');
const isLoading = ref(false);
const error = ref<string | null>(null);
const currentProjectID = ref<number>(1); // Dummy project ID

const isTasksEmpty = computed(() => tasks.value.length === 0);

// Load dummy tasks on mount
const loadTasks = () => {
  tasks.value = [
    { taskID: 1, taskName: "Buy groceries", createdAt: "2025-03-27", projectID: 1, completed: false },
    { taskID: 2, taskName: "Finish report", createdAt: "2025-03-26", projectID: 1, completed: true }
  ];
};

// Add a new task locally
const quickAddTask = () => {
  if (!newTaskInput.value.trim()) return;
  const newTask: Task = {
    taskID: tasks.value.length + 1,
    taskName: newTaskInput.value,
    createdAt: new Date().toISOString().split('T')[0], // YYYY-MM-DD
    projectID: currentProjectID.value,
    completed: false
  };
  tasks.value.unshift(newTask);
  newTaskInput.value = '';
};

// Format date
const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
};

// Initialize on mount
onMounted(() => {
  loadTasks();
  nextTick(() => {
    const inputElement = document.querySelector('.task-input') as HTMLInputElement;
    if (inputElement) inputElement.focus();
  });
});
</script>

<template>
  <main class="task-container">
    <header class="task-header">
      <h1 class="app-title">My Tasks</h1>
    </header>

    <!-- Error display -->
    <section v-if="error" class="error-message">
      <p>{{ error }}</p>
      <button @click="error = null" class="error-close-btn">×</button>
    </section>

    <!-- Task input -->
    <section class="task-input-section">
      <form @submit.prevent="quickAddTask" class="task-form">
        <input v-model="newTaskInput" type="text" placeholder="Add a new task..." class="task-input" />
        <button type="submit" class="add-task-btn">+ Add Task</button>
      </form>
    </section>

    <!-- Tasks list -->
    <section class="tasks-section">
      <h2 class="tasks-heading">Tasks</h2>

      <template v-if="isTasksEmpty">
        <p class="empty-state-text">No tasks found. Add your first task above!</p>
      </template>

      <ul v-else class="task-list">
        <li v-for="task in tasks" :key="task.taskID" class="task-item">
          <input type="checkbox" v-model="task.completed" />
          <h3 class="task-title" :class="{ 'completed-task': task.completed }">{{ task.taskName }}</h3>
          <p>Created: {{ formatDate(task.createdAt) }}</p>
        </li>
      </ul>
    </section>
  </main>
</template>


<style scoped>
.task-container {
  width: 100%;
  margin-left: -30px;
  padding: 0 1.25rem 0 calc(250px - 0.5rem);
  box-sizing: border-box;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}

.task-header {
  border-bottom: 1px solid #e0e0e0;
  padding: 1rem 0;
  margin-bottom: 1.5rem;
}

.app-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.task-input-section {
  margin-bottom: 1.5rem;
  width: 100%;
}

.task-form {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  width: 100%;
}

.task-input {
  flex: 1;
  border: 1px solid #e0e0e0;
  border-radius: 0.25rem;
  padding: 0.75rem 1rem;
  font-size: 0.875rem;
  outline: none;
  transition: border-color 0.2s;
  width: 100%;
}

.task-input:focus {
  border-color: #8b53ff;
}

.add-task-btn {
  background-color: #8b53ff;
  color: white;
  border: none;
  border-radius: 0.25rem;
  padding: 0.75rem 1rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: background-color 0.2s;
  white-space: nowrap;
}

.add-task-btn:hover:not(:disabled) {
  background-color: #7b46e6;
}

.add-task-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.tasks-section {
  background-color: #f9f9fb;
  border-radius: 0.5rem;
  padding: 1rem;
  width: 100%;
}

.tasks-heading {
  font-size: 1rem;
  color: #333;
  margin: 0 0 1rem 0;
  font-weight: 600;
}

.task-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  width: 100%;
}

.task-item {
  display: block;
  background-color: white;
  border-radius: 0.25rem;
  padding: 1rem;
  margin-bottom: 0.5rem;
}

.task-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin: 0;
  font-size: 0.875rem;
  font-weight: 500;
  color: #333;
}

.notebook-icon-small {
  color: #8b53ff;
  flex-shrink: 0;
}

.task-meta {
  margin-top: 0.5rem;
}

.task-meta dl {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
  margin: 0;
}

.task-meta dt {
  font-weight: 500;
  color: #666;
}

.task-meta dd {
  margin: 0;
  color: #888;
  background-color: #f5f5f5;
  padding: 0.125rem 0.375rem;
  border-radius: 0.25rem;
  border: 1px solid #e0e0e0;
}

.loading-indicator {
  text-align: center;
  padding: 1rem;
  color: #666;
}

.empty-state-icon {
  margin-bottom: 1rem;
}

.empty-state-text {
  color: #666;
}

.error-message {
  background-color: #ffebee;
  color: #c62828;
  padding: 0.75rem 1rem;
  border-radius: 0.25rem;
  margin-bottom: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.error-close-btn {
  background: none;
  border: none;
  color: #c62828;
  font-size: 1.25rem;
  cursor: pointer;
  padding: 0;
  margin-left: 0.5rem;
}

/* Responsive styles */
@media (max-width: 1024px) {
  .task-container {
    padding-left: calc(200px - 0.5rem);
  }
  .task-meta dl {
    flex-direction: column;
    gap: 0.25rem;
  }
}

@media (max-width: 768px) {
  .task-container {
    padding: 0 0.9375rem;
  }
  .task-header {
    padding: 0.75rem 0;
  }
  .app-title {
    font-size: 1.25rem;
  }
  .task-form {
    flex-direction: column;
    align-items: stretch;
  }
  .task-input {
    margin-bottom: 0.5rem;
  }
  .add-task-btn {
    width: 100%;
    padding: 0.75rem;
  }
  .task-item {
    padding: 0.75rem;
  }
}

@media (max-width: 480px) {
  .task-container {
    padding: 0 0.625rem;
  }
  .task-header {
    margin-bottom: 1rem;
  }
  .app-title {
    font-size: 1.125rem;
  }
  .tasks-section {
    padding: 0.75rem;
  }
  .empty-state-icon {
    width: 36px;
    height: 36px;
  }
}

@media (max-width: 768px) and (orientation: landscape) {
  .task-form {
    flex-direction: row;
  }
  .task-input {
    margin-bottom: 0;
  }
  .add-task-btn {
    width: auto;
  }
  .task-meta dl {
    flex-direction: row;
  }
}

/* High contrast mode support */
@media (forced-colors: active) {
  .task-item {
    border: 1px solid ButtonText;
  }
  .task-meta dd {
    border: 1px solid ButtonText;
  }
}

/* Print styles */
@media print {
  .task-container {
    padding: 0;
  }
  .add-task-btn,
  .task-input-section {
    display: none;
  }
  .task-item {
    break-inside: avoid;
    page-break-inside: avoid;
    border: 1px solid #ddd;
    margin-bottom: 0.5rem;
  }
  .task-header {
    text-align: center;
    margin-bottom: 1rem;
  }
}
</style>
