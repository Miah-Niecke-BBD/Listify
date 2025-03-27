<script setup lang="ts">
import { ref } from 'vue';
import SideBar from 'src/components/SideBar.vue'
import TaskList from '@/views/TaskList.vue';
import AddTaskDialog from '@/components/AddTaskDialog.vue';
import AddTeamDialog from '@/components/AddTeamDialog.vue';
 
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
    priority: 'high',
  },
  {
    id: '2',
    title: 'Review code changes',
    completed: true,
    description: 'Review pull requests from the team',
    dueDate: '2023-12-10',
    priority: 'medium',
  },
]);
 
const projects = ref([
  { id: 'p1', name: 'Work' },
  { id: 'p2', name: 'Personal' },
]);
 
const teams = ref<Team[]>([
  { id: 't1', name: 'My Project Team', color: '#8b53ff' },
]);
 
const isAddTaskDialogOpen = ref(false);
const isAddTeamDialogOpen = ref(false);
 
function toggleTaskCompletion(id: string) {
  const taskIndex = tasks.value.findIndex(task => task.id === id);
  if (taskIndex !== -1) {
    tasks.value[taskIndex].completed = !tasks.value[taskIndex].completed;
  }
}
 
function openAddTaskDialog() {
  isAddTaskDialogOpen.value = true;
}
 
function closeAddTaskDialog() {
  isAddTaskDialogOpen.value = false;
}
 
function openAddTeamDialog() {
  isAddTeamDialogOpen.value = true;
}
 
function closeAddTeamDialog() {
  isAddTeamDialogOpen.value = false;
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
    project: taskData.project !== 'none' ? taskData.project : undefined,
  };
  tasks.value.push(newTask);
  closeAddTaskDialog();
}
 
function quickAddTask(newTask: Task) {
  tasks.value.push(newTask);
}
 
interface TeamData {
  name: string;
  color: string;
}
 
function saveTeam(teamData: TeamData) {
  const newTeam: Team = {
    id: Date.now().toString(),
    name: teamData.name,
    color: teamData.color,
  };
  teams.value.push(newTeam);
  closeAddTeamDialog();
}
</script>
 
<template>
  <section class="app-container">
    <SideBar
      :teams="teams"
      @open-team-dialog="openAddTeamDialog"
    />
    <main class="main-content">
      <header class="task-header">
        <h1>My Tasks</h1>
        <button @click="openAddTaskDialog" class="add-task-btn">+ Add Task</button>
      </header>
      <section class="task-content">
        <TaskList
          :tasks="tasks"
          :projects="projects"
          @toggle-completion="toggleTaskCompletion"
          @add-task="openAddTaskDialog"
          @quick-add-task="quickAddTask"
        />
      </section>
    </main>
 
    <dialog
      class="dialog-overlay"
      :open="isAddTaskDialogOpen"
      @click.self="closeAddTaskDialog"
    >
      <article class="dialog-container">
        <AddTaskDialog
          :is-open="isAddTaskDialogOpen"
          :projects="projects"
          @close="closeAddTaskDialog"
          @save-task="saveTask"
        />
      </article>
    </dialog>
 
    <dialog
      class="dialog-overlay"
      :open="isAddTeamDialogOpen"
      @click.self="closeAddTeamDialog"
    >
      <article class="dialog-container">
        <AddTeamDialog
          :is-open="isAddTeamDialogOpen"
          @close="closeAddTeamDialog"
          @save-team="saveTeam"
        />
      </article>
    </dialog>
  </section>
</template>
 
<style scoped>
.app-container {
  display: flex;
  height: 100vh;
  width: 100%;
}
 
.main-content {
  flex: 1;
  padding: 1.5rem;
  overflow-y: auto;
}
 
.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}
 
h1 {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}
 
.add-task-btn {
  padding: 0.625rem 1rem;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  transition: background-color 0.2s ease;
}
 
.add-task-btn:hover {
  background-color: var(--primary-dark);
}
 
.task-content {
  width: 100%;
}
 
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
  border-radius: 0.75rem;
  width: 100%;
  max-width: 31.25rem;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: var(--shadow-lg);
  animation: slideIn 0.3s ease;
}
 
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}
 
@keyframes slideIn {
  from { transform: translateY(1.25rem); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}
 
@media (max-width: 48rem) {
  .main-content {
    padding: 1rem;
  }
 
  .dialog-container {
    width: 90%;
    max-height: 80vh;
  }
}
</style>