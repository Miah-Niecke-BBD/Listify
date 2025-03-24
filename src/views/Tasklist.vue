<!-- src/views/TaskList.vue -->
<template>
    <main class="main-content">
      <header class="main-header">
        <h1 class="page-title">My List</h1>
        <section class="header-actions">
          <button @click="openProjectDialog" class="new-project-btn">New Project</button>
        </section>
      </header>
      <section class="content-area">
        <form @submit.prevent="addNewTask" class="add-task-form">
          <section class="form-controls">
            <input v-model="newTaskTitle" type="text" placeholder="Add a new task..." class="task-input">
            <button type="submit" class="add-task-btn">+ Add Task</button>
            <button @click.prevent="openTaskDialog" type="button" class="advanced-btn">+ Add Advanced Task</button>
          </section>
        </form>
        <section class="tasks-container">
          <header class="tasks-header">
            <h2 class="tasks-title">Tasks</h2>
          </header>
          <section class="tasks-grid">
            <task-card v-for="task in tasks" :key="task.id" :task="task" @toggle-completion="toggleTaskCompletion" />
          </section>
        </section>
        <add-task-dialog :is-open="isTaskDialogOpen" :projects="projects" @close="closeTaskDialog" @save-task="saveNewTask" />
        <add-project-dialog :is-open="isProjectDialogOpen" @close="closeProjectDialog" @save-project="saveNewProject" />
      </section>
    </main>
  </template>
  
  <script>
  import { ref } from 'vue'
  import TaskCard from '../components/TaskCard.vue'
  import AddTaskDialog from '../components/AddTaskDialog.vue'
  import AddProjectDialog from '../components/AddProjectDialog.vue'
  
  export default {
    name: 'TaskList',
    components: { TaskCard, AddTaskDialog, AddProjectDialog },
    setup() {
      const initialTasks = [
        { id: "1", title: "Complete project proposal", completed: false },
        { id: "2", title: "Schedule team meeting", completed: false }
      ]
      const initialProjects = [
        { id: "1", name: "Personal Project", color: "blue", type: "personal" }
      ]
      const tasks = ref([...initialTasks])
      const projects = ref([...initialProjects])
      const nextTaskId = ref(3)
      const nextProjectId = ref(2)
      const newTaskTitle = ref('')
      const isTaskDialogOpen = ref(false)
      const isProjectDialogOpen = ref(false)
      const newTask = ref({ title: '', description: '', dueDate: '', priority: 'medium', project: 'none' })
      const newProject = ref({ name: '', description: '', color: 'blue', type: 'personal' })
  
      function toggleTaskCompletion(taskId) {
        tasks.value = tasks.value.map(task => 
          task.id === taskId ? { ...task, completed: !task.completed } : task
        )
      }
      function addNewTask() {
        if (newTaskTitle.value.trim()) {
          tasks.value.push({ id: String(nextTaskId.value++), title: newTaskTitle.value.trim(), completed: false })
          newTaskTitle.value = ''
        }
      }
      function openTaskDialog() {
        newTask.value = { title: '', description: '', dueDate: '', priority: 'medium', project: 'none' }
        isTaskDialogOpen.value = true
      }
      function closeTaskDialog() { isTaskDialogOpen.value = false }
      function saveNewTask(task) {
        tasks.value.push({ ...task, id: String(nextTaskId.value++), completed: false })
        closeTaskDialog()
      }
      function openProjectDialog() {
        newProject.value = { name: '', description: '', color: 'blue', type: 'personal' }
        isProjectDialogOpen.value = true
      }
      function closeProjectDialog() { isProjectDialogOpen.value = false }
      function saveNewProject(project) {
        projects.value.push({ ...project, id: String(nextProjectId.value++) })
        closeProjectDialog()
      }
      function formatDate(dateString) {
        return dateString ? new Date(dateString).toLocaleDateString() : ''
      }
  
      return {
        tasks,
        projects,
        newTaskTitle,
        isTaskDialogOpen,
        isProjectDialogOpen,
        newTask,
        newProject,
        toggleTaskCompletion,
        addNewTask,
        openTaskDialog,
        closeTaskDialog,
        saveNewTask,
        openProjectDialog,
        closeProjectDialog,
        saveNewProject,
        formatDate
      }
    }
  }
  </script>
  
  <style scoped>
  .main-content { flex: 1; overflow: auto; }
  .main-header { display: flex; height: 64px; align-items: center; justify-content: space-between; border-bottom: 1px solid #e5e7eb; padding: 0 24px; }
  .page-title { font-size: 24px; font-weight: 700; color: var(--heading-purple); }
  .header-actions { display: flex; align-items: center; gap: 16px; }
  .new-project-btn { display: inline-flex; align-items: center; font-size: 14px; font-weight: 500; padding: 6px 12px; border: 1px solid #e5e7eb; border-radius: 6px; background-color: transparent; transition: background-color 0.2s ease; }
  .new-project-btn:hover { background-color: var(--button-hover-bg); }
  .content-area { padding: 24px; }
  .add-task-form { margin-bottom: 24px; }
  .form-controls { display: flex; gap: 8px; }
  .task-input { flex: 1; padding: 8px 12px; border: 1px solid #e5e7eb; border-radius: 6px; font-size: 14px; }
  .add-task-btn { display: inline-flex; align-items: center; font-size: 14px; font-weight: 500; padding: 8px 16px; background-color: var(--primary-color); color: white; border: none; border-radius: 6px; transition: background-color 0.2s ease; }
  .add-task-btn:hover { background-color: var(--dark-purple); }
  .advanced-btn { display: inline-flex; align-items: center; font-size: 14px; font-weight: 500; padding: 8px 16px; border: 1px solid #e5e7eb; background-color: transparent; border-radius: 6px; transition: background-color 0.2s ease; }
  .advanced-btn:hover { background-color: var(--button-hover-bg); }
  .tasks-container { border: 1px solid #e5e7eb; border-radius: 8px; background-color: var(--white-color); box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
  .tasks-header { border-bottom: 1px solid #e5e7eb; background-color: var(--button-hover-bg); padding: 12px 16px; }
  .tasks-title { font-size: 18px; font-weight: 500; color: var(--heading-purple); }
  .tasks-grid { padding: 16px; display: grid; gap: 16px; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); }
  @media (max-width: 768px) { .tasks-grid { grid-template-columns: 1fr; } }
  </style>