<script setup lang="ts">
import { ref, watch } from 'vue';

// Define props
const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true
  },
  projects: {
    type: Array,
    required: true
  }
});

// Define emits
const emit = defineEmits(['close', 'save-task']);

// Form state
const title = ref('');
const description = ref('');
const dueDate = ref('');
const priority = ref<number>(0);
const projectID = ref<number | null>(null);
const error = ref<string | null>(null);
const isSubmitting = ref(false);

// API base URL
const API_BASE_URL = 'http://localhost:8080';

// Reset form when dialog opens/closes
watch(() => props.isOpen, (isOpen) => {
  if (isOpen) {
    // Initialize with default values when dialog opens
    title.value = '';
    description.value = '';
    dueDate.value = '';
    priority.value = 0;
    projectID.value = null;
    error.value = null;
  }
});

// Close dialog
function closeDialog() {
  emit('close');
}

// Save task
async function saveTask() {
  if (!title.value.trim()) {
    error.value = 'Task title is required';
    return;
  }

  if (!projectID.value) {
    error.value = 'Please select a project';
    return;
  }

  isSubmitting.value = true;
  error.value = null;

  try {
    const taskData = {
      taskName: title.value,
      taskDescription: description.value,
      dueDate: dueDate.value || null,
      taskPriority: priority.value,
      projectID: projectID.value,
      completed: false
    };

    const response = await fetch(`${API_BASE_URL}/tasks`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(taskData),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => null);
      throw new Error(`Failed to create task: ${JSON.stringify(errorData) || response.statusText}`);
    }

    const createdTask = await response.json();
    emit('save-task', createdTask);
    closeDialog();
  } catch (err) {
    console.error('Error adding task:', err);
    error.value = `Error adding task: ${err instanceof Error ? err.message : String(err)}`;
  } finally {
    isSubmitting.value = false;
  }
}
</script>

<template>
  <div v-if="isOpen" class="dialog-overlay" @click.self="closeDialog">
    <div class="dialog-container">
      <div class="dialog-header">
        <h2 class="dialog-title">Add New Task</h2>
        <p class="dialog-subtitle">Create a new task to keep track of your work</p>
      </div>

      <!-- Error message -->
      <div v-if="error" class="error-message">
        {{ error }}
        <button @click="error = null" class="error-close-btn">×</button>
      </div>

      <form @submit.prevent="saveTask" class="dialog-form">
        <div class="form-group">
          <label for="task-title" class="form-label">Task Title *</label>
          <input
            id="task-title"
            v-model="title"
            type="text"
            class="form-input"
            placeholder="Enter task title"
            required
          />
        </div>

        <div class="form-group">
          <label for="task-description" class="form-label">Description</label>
          <textarea
            id="task-description"
            v-model="description"
            class="form-textarea"
            placeholder="Enter task description"
          ></textarea>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label for="task-due-date" class="form-label">Due Date</label>
            <input
              id="task-due-date"
              v-model="dueDate"
              type="date"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label for="task-priority" class="form-label">Priority</label>
            <select id="task-priority" v-model="priority" class="form-select">
              <option :value="0">Low</option>
              <option :value="1">Medium</option>
              <option :value="2">High</option>
            </select>
          </div>
        </div>

        <div class="form-group">
          <label for="task-project" class="form-label">Project *</label>
          <select id="task-project" v-model="projectID" class="form-select" required>
            <option :value="null" disabled>Select a project</option>
            <option v-for="project in projects" :key="project.id" :value="project.id">
              {{ project.name }}
            </option>
          </select>
          <p class="form-help">Tasks must be associated with a project</p>
        </div>
      </form>

      <div class="dialog-footer">
        <button @click="closeDialog" class="cancel-btn">Cancel</button>
        <button
          @click="saveTask"
          class="save-btn"
          :disabled="isSubmitting"
        >
          {{ isSubmitting ? 'Saving...' : 'Save Task' }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  animation: fadeIn 0.3s ease;
}

.dialog-container {
  background-color: white;
  border-radius: 12px;
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  animation: slideIn 0.3s ease;
}

.dialog-header {
  padding: 24px 24px 0;
  margin-bottom: 16px;
}

.dialog-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
}

.dialog-subtitle {
  color: #666;
  font-size: 14px;
}

.dialog-form {
  padding: 0 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 8px;
  color: #333;
}

.form-input,
.form-select,
.form-textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 14px;
  background-color: white;
  transition: border-color 0.2s;
}

.form-input:focus,
.form-select:focus,
.form-textarea:focus {
  outline: none;
  border-color: #8b53ff;
  box-shadow: 0 0 0 2px rgba(139, 83, 255, 0.1);
}

.form-textarea {
  resize: none;
  min-height: 100px;
}

.form-help {
  color: #666;
  font-size: 12px;
  margin-top: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  padding: 16px 24px;
  border-top: 1px solid #e0e0e0;
  margin-top: 24px;
  gap: 12px;
}

.cancel-btn {
  padding: 10px 16px;
  background-color: #f5f5f5;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  transition: background-color 0.2s ease;
  cursor: pointer;
}

.cancel-btn:hover {
  background-color: #eeeeee;
}

.save-btn {
  padding: 10px 16px;
  background-color: #8b53ff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.2s ease;
  cursor: pointer;
}

.save-btn:hover:not(:disabled) {
  background-color: #7b46e6;
}

.save-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.error-message {
  background-color: #ffebee;
  color: #c62828;
  padding: 0.75rem 1rem;
  margin: 0 24px 16px;
  border-radius: 6px;
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

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideIn {
  from { transform: translateY(20px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }

  .dialog-container {
    width: 90%;
    max-height: 80vh;
  }
}
</style>
