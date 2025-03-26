<script setup lang="ts">
import { ref } from 'vue'
import SideBar from '@/components/icons/SideBar.vue'
import TaskList from '@/views/TaskList.vue'
import AddTaskDialog from '@/components/AddTaskDialog.vue'
import AddTeamDialog from '@/components/AddTeamDialog.vue'

interface Task {
  id: string;
  title: string;
  completed: boolean;
  description?: string;
  dueDate?: string;
  priority?: string | null;
  project?: string;
}

interface Team {
  id: string;
  name: string;
  color: string;
}

const tasks = ref<Task[]>([
  {
    id: '1',
    title: 'Complete project proposal',
    completed: false,
    description: 'Write up the project proposal for the new client',
    dueDate: '2023-12-15',
    priority: 'high'
  },
  {
    id: '2',
    title: 'Review code changes',
    completed: true,
    description: 'Review pull requests from the team',
    dueDate: '2023-12-10',
    priority: 'medium'
  }
])

const projects = ref([
  { id: 'p1', name: 'Work' },
  { id: 'p2', name: 'Personal' }
])

const teams = ref<Team[]>([
  { id: 't1', name: 'My Project Team', color: '#8b53ff' }
])

const isAddTaskDialogOpen = ref(false)
const isAddTeamDialogOpen = ref(false)

function toggleTaskCompletion(id: string) {
  const taskIndex = tasks.value.findIndex(task => task.id === id)
  if (taskIndex !== -1) {
    tasks.value[taskIndex].completed = !tasks.value[taskIndex].completed
  }
}

function openAddTaskDialog() {
  isAddTaskDialogOpen.value = true
}

function closeAddTaskDialog() {
  isAddTaskDialogOpen.value = false
}

function openAddTeamDialog() {
  isAddTeamDialogOpen.value = true
}

function closeAddTeamDialog() {
  isAddTeamDialogOpen.value = false
}

interface AddTaskDialogData {
  title: string;
  description: string;
  dueDate: string;
  priority: string | null;
  project: string;
}

function saveTask(taskData: AddTaskDialogData) {
  const newTask: Task = {
    id: Date.now().toString(),
    title: taskData.title,
    completed: false,
    description: taskData.description,
    dueDate: taskData.dueDate,
    priority: taskData.priority,
    project: taskData.project !== 'none' ? taskData.project : undefined
  }
  tasks.value.push(newTask)
  closeAddTaskDialog()  // Ensure dialog closes after saving
}

function quickAddTask(newTask: Task) {
  tasks.value.push(newTask)
}

interface TeamData {
  name: string;
  color: string;
}

function saveTeam(teamData: TeamData) {
  const newTeam: Team = {
    id: Date.now().toString(),
    name: teamData.name,
    color: teamData.color
  }
  teams.value.push(newTeam)
  closeAddTeamDialog()  // Ensure dialog closes after saving
}
</script>

<template>
  <div class="app-container">
    <SideBar
      :teams="teams"
      @open-team-dialog="openAddTeamDialog"
    />
    <main class="main-content">
      <div class="task-header">
        <h1>My Tasks</h1>
        <button @click="openAddTaskDialog" class="add-task-btn">+ Add Task</button>
      </div>
      <TaskList
        :tasks="tasks"
        :projects="projects"
        @toggle-completion="toggleTaskCompletion"
        @add-task="openAddTaskDialog"
        @quick-add-task="quickAddTask"
      />
    </main>

    <AddTaskDialog
      :is-open="isAddTaskDialogOpen"
      :projects="projects"
      @close="closeAddTaskDialog"
      @save-task="saveTask"
    />

    <AddTeamDialog
      :is-open="isAddTeamDialogOpen"
      @close="closeAddTeamDialog"
      @save-team="saveTeam"
    />
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
  background-color: var(--card-bg);
  border-radius: 12px;
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: var(--shadow-lg);
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
  color: var(--text-primary);
}

.dialog-subtitle {
  color: var(--text-secondary);
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
  color: var(--text-primary);
}

.form-input,
.form-select,
.form-textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  font-size: 14px;
  background-color: var(--card-bg);
  transition: border-color 0.2s;
}

.form-input:focus,
.form-select:focus,
.form-textarea:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(139, 83, 255, 0.1);
}

.form-textarea {
  resize: none;
  min-height: 100px;
}

.form-help {
  color: var(--text-secondary);
  font-size: 12px;
  margin-top: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  padding: 16px 24px;
  border-top: 1px solid var(--border-color);
  margin-top: 24px;
  gap: 12px;
}

.cancel-btn {
  padding: 10px 16px;
  background-color: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
  transition: background-color 0.2s ease;
}

.cancel-btn:hover {
  background-color: var(--hover-color);
}

.save-btn {
  padding: 10px 16px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.2s ease;
}

.save-btn:hover {
  background-color: var(--primary-dark);
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
