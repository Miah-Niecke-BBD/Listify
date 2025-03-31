<script setup lang="ts">
import { ref, onMounted, nextTick, computed } from 'vue';
import { useRoute } from 'vue-router';

interface Task {
  taskID: number;
  taskName: string;
  taskDescription?: string | null;
  createdAt?: string;
  projectID: number;
  sectionID: number;
  completed?: boolean;
  dateCompleted?: string | null;
}

interface Project {
  projectID: number;
  projectName: string;
  lastSectionID: number;
}

interface Team {
  teamID: number;
  teamName: string;
}

const route = useRoute();
const tasks = ref<Task[]>([]);
const newTaskInput = ref('');
const newTaskDescription = ref('');
const isLoading = ref(false);
const error = ref<string | null>(null);
const myListProject = ref<Project | null>(null);
const myListTeam = ref<Team | null>(null);
const userID = ref<number | null>(null);
const isInitialized = ref(false);
const showTaskDescription = ref(false);
const taskSource = ref('api'); // Default to 'api' since offline is removed

const getJwtToken = () => localStorage.getItem('jwtToken');

const validateTokenAndGetUserInfo = async () => {
  try {
    const token = getJwtToken();
    if (!token) throw new Error('Authentication token is missing');

    const response = await fetch('http://localhost:8080/user', {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      if (response.status === 401) {
        localStorage.removeItem('jwtToken');
        throw new Error('Session expired. Please log in again.');
      }
      throw new Error('Failed to validate user session');
    }

    const userData = await response.json();
    userID.value = userData.userID;
    console.log('User authenticated with ID:', userID.value);
    return true;
  } catch (err) {
    console.error('Authentication error:', err);
    error.value = `Authentication error: ${err instanceof Error ? err.message : String(err)}`;
    return false;
  }
};

const setupMyListProject = async () => {
  if (!await validateTokenAndGetUserInfo()) return false;

  try {
    const token = getJwtToken();
    if (!token || !userID.value) return false;

    const teamsResponse = await fetch('http://localhost:8080/teams', {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (teamsResponse.ok) {
      const teams: Team[] = await teamsResponse.json();
      myListTeam.value = teams.find(t => t.teamName === 'My List') || teams[0] || null;
    }

    if (!myListTeam.value) {
      const createTeamResponse = await fetch('http://localhost:8080/teams?teamName=My%20List', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });
      myListTeam.value = createTeamResponse.ok ? await createTeamResponse.json() : null;
    }

    if (myListTeam.value) {
      const projectsUrl = `http://localhost:8080/teams/${myListTeam.value.teamID}/projects`;
      const projectsResponse = await fetch(projectsUrl, {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });

      if (projectsResponse.ok) {
        const projects: Project[] = await projectsResponse.json();
        const existingProject = projects.find(p => p.projectName === 'Home') || projects[0] || null;
        myListProject.value = existingProject ? {
          projectID: existingProject.projectID,
          projectName: 'Home',
          lastSectionID: existingProject.lastSectionID || 0
        } : null;
      }

      if (!myListProject.value) {
        const createProjectUrl = `http://localhost:8080/projects?teamID=${myListTeam.value.teamID}&projectName=Home&teamLeaderID=${userID.value}`;
        const createProjectResponse = await fetch(createProjectUrl, {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });

        if (createProjectResponse.ok) {
          const newProject = await createProjectResponse.json();
          myListProject.value = {
            projectID: newProject.projectID,
            projectName: 'Home',
            lastSectionID: 0
          };
        }
      }
    }

    if (myListProject.value) {
      localStorage.setItem(`myListProjectID_${userID.value}`, String(myListProject.value.projectID));
      return true;
    }
    throw new Error('Could not set up project');
  } catch (err) {
    console.error('Error in project setup:', err);
    return false;
  }
};

const loadTasks = async () => {
  if (!myListProject.value) return;

  isLoading.value = true;
  try {
    const token = getJwtToken();
    if (!token) throw new Error('Authentication token is missing');

    const response = await fetch('http://localhost:8080/tasks', {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) throw new Error('Failed to fetch tasks');

    const data = await response.json();
    tasks.value = data
      .filter((task: any) => task.projectID === myListProject.value!.projectID)
      .map((task: any) => ({
        taskID: task.taskID,
        taskName: task.taskName,
        taskDescription: task.taskDescription || null,
        createdAt: task.createdAt || null,
        projectID: task.projectID,
        sectionID: task.sectionID || 0,
        completed: task.dateCompleted !== null,
        dateCompleted: task.dateCompleted
      }));
  } catch (err) {
    console.error('Error fetching tasks:', err);
    error.value = 'Failed to load tasks. Please try again.';
  } finally {
    isLoading.value = false;
  }
};

const createTaskOnServer = async (taskName: string, taskDescription: string | null = null) => {
  const token = getJwtToken();
  if (!token || !myListProject.value) throw new Error('Authentication token or project missing');

  const projectID = myListProject.value.projectID;
  let sectionID = 1;

  try {
    const sectionParams = new URLSearchParams();
    sectionParams.append('projectID', String(projectID));
    sectionParams.append('sectionName', 'Default');
    sectionParams.append('sectionPosition', '0');
    const sectionUrl = `http://localhost:8080/sections?${sectionParams.toString()}`;
    const sectionResponse = await fetch(sectionUrl, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (sectionResponse.ok) {
      const sectionData = await sectionResponse.json();
      sectionID = sectionData.sectionID;
      console.log('Section created with ID:', sectionID);
    } else {
      console.error('Section creation failed:', await sectionResponse.text());
    }

    const taskParams = new URLSearchParams();
    taskParams.append('projectID', String(projectID));
    taskParams.append('sectionID', String(sectionID));
    taskParams.append('taskName', taskName);
    if (taskDescription) taskParams.append('taskDescription', taskDescription);

    const taskUrl = `http://localhost:8080/tasks?${taskParams.toString()}`;
    const response = await fetch(taskUrl, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) throw new Error(await response.text());
    return await response.json();
  } catch (err) {
    console.error('Error creating task:', err);
    throw err;
  }
};

const quickAddTask = async () => {
  if (!newTaskInput.value.trim()) {
    error.value = 'Please enter a task name before adding.';
    return;
  }

  error.value = null; // Clear any previous error

  if (!myListProject.value || !myListTeam.value) {
    await setupMyListProject();
    if (!myListProject.value || !myListTeam.value) return;
  }

  const newTask = {
    taskID: 0, // Temporary, will be replaced by server
    taskName: newTaskInput.value,
    taskDescription: newTaskDescription.value.trim() || null,
    createdAt: new Date().toISOString(),
    projectID: myListProject.value.projectID,
    sectionID: 1,
    completed: false
  };

  tasks.value.unshift(newTask);
  newTaskInput.value = '';
  newTaskDescription.value = '';
  showTaskDescription.value = false;

  try {
    const createdTask = await createTaskOnServer(newTask.taskName, newTask.taskDescription);
    const index = tasks.value.findIndex(t => t.createdAt === newTask.createdAt); // Match by createdAt since taskID is temp
    if (index !== -1) {
      tasks.value[index] = { ...createdTask, taskDescription: newTask.taskDescription, completed: false };
    }
  } catch (err) {
    error.value = 'Failed to add task. Please try again.';
  }
};

const deleteTask = async (taskId: number) => {
  try {
    const token = getJwtToken();
    if (!token) throw new Error('Authentication token is missing');

    const response = await fetch(`http://localhost:8080/tasks/${taskId}`, {
      method: 'DELETE',
      headers: { 'Authorization': `Bearer ${token}` }
    });

    if (response.ok) {
      tasks.value = tasks.value.filter(t => t.taskID !== taskId);
    } else {
      throw new Error('Delete failed');
    }
  } catch (err) {
    console.error('Error deleting task:', err);
    error.value = 'Failed to delete task. Please try again.';
  }
};

const toggleTaskCompletion = async (task: Task) => {
  task.completed = !task.completed;

  if (task.completed) {
    setTimeout(() => {
      tasks.value = tasks.value.filter(t => t.taskID !== task.taskID);
    }, 3500);
  }

  try {
    const token = getJwtToken();
    if (!token) throw new Error('Authentication token is missing');

    const updateData = {
      taskName: task.taskName,
      taskDescription: task.taskDescription || null,
      dateCompleted: task.completed ? new Date().toISOString() : null
    };

    const response = await fetch(`http://localhost:8080/tasks/${task.taskID}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(updateData)
    });

    if (!response.ok) throw new Error('Update failed');
  } catch (err) {
    console.error('Error updating task:', err);
    error.value = 'Failed to update task. Please try again.';
  }
};

const formatDate = (dateString: string | null) => {
  if (!dateString) return 'N/A';
  return new Date(dateString).toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
};

const isFadingOut = computed(() => {
  return tasks.value.reduce((acc, task) => {
    acc[task.taskID] = task.completed;
    return acc;
  }, {} as Record<number, boolean>);
});

const toggleTaskDescription = () => {
  showTaskDescription.value = !showTaskDescription.value;
  if (showTaskDescription.value) {
    nextTick(() => {
      const descriptionElement = document.querySelector('.task-description-input') as HTMLTextAreaElement;
      if (descriptionElement) descriptionElement.focus();
    });
  }
};

const initialize = async () => {
  await validateTokenAndGetUserInfo();
  if (!userID.value) return;

  const savedProjectID = localStorage.getItem(`myListProjectID_${userID.value}`);
  if (savedProjectID) {
    myListProject.value = { projectID: Number(savedProjectID), projectName: 'Home', lastSectionID: 0 };
  } else {
    await setupMyListProject();
  }

  if (myListProject.value) await loadTasks();

  nextTick(() => {
    const inputElement = document.querySelector('.task-input') as HTMLInputElement;
    if (inputElement) inputElement.focus();
  });

  isInitialized.value = true;
};

onMounted(initialize);
</script>

<template>
  <main class="task-container">
    <header class="task-header">
      <h1 class="app-title">Home</h1>
      <p class="task-source">Tasks from: {{ taskSource }}</p>
    </header>

    <section class="error-message" v-if="error">
      {{ error }}
      <button @click="error = null" class="error-close-btn">×</button>
    </section>

    <section class="task-input-section">
      <form @submit.prevent="quickAddTask" class="task-form">
        <input v-model="newTaskInput" type="text" placeholder="Add a new task..." class="task-input" />
        <button type="button" @click="toggleTaskDescription" class="toggle-description-btn" :class="{ 'active': showTaskDescription }">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
            <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
          </svg>
        </button>
        <button type="submit" class="add-task-btn" :disabled="isLoading">Add</button>
        <textarea v-if="showTaskDescription" v-model="newTaskDescription" placeholder="Add a description (optional)" class="task-description-input" />
      </form>
    </section>

    <section class="tasks-section">
      <h2 class="tasks-heading">Tasks</h2>

      <p class="loading-indicator" v-if="isLoading">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="loading-icon">
          <line x1="12" y1="2" x2="12" y2="6" />
          <line x1="12" y1="18" x2="12" y2="22" />
          <line x1="4.93" y1="4.93" x2="7.76" y2="7.76" />
          <line x1="16.24" y1="16.24" x2="19.07" y2="19.07" />
          <line x1="2" y1="12" x2="6" y2="12" />
          <line x1="18" y1="12" x2="22" y2="12" />
          <line x1="4.93" y1="19.07" x2="7.76" y2="16.24" />
          <line x1="16.24" y1="7.76" x2="19.07" y2="4.93" />
        </svg>
      </p>

      <p class="empty-state" v-else-if="tasks.length === 0">
        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" stroke-linecap="round" stroke-linejoin="round" class="notebook-icon">
          <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20" />
          <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z" />
          <path d="M8 7h6" />
          <path d="M8 11h8" />
          <path d="M8 15h5" />
        </svg>
        No tasks found. Add your first task above!
      </p>

      <ul class="task-list" v-else>
        <li v-for="task in tasks" :key="task.taskID" class="task-item" :class="{ 'completed': task.completed, 'fade-out': isFadingOut[task.taskID] }">
          <button @click="toggleTaskCompletion(task)" class="task-checkbox" :class="{ 'checked': task.completed }">
            <svg v-if="task.completed" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="check-icon">
              <polyline points="20 6 9 17 4 12" />
            </svg>
          </button>
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="notebook-icon-small">
            <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20" />
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z" />
          </svg>
          <h3 class="task-title">{{ task.taskName }}</h3>
          <p class="task-description" v-if="task.taskDescription">{{ task.taskDescription }}</p>
          <p class="task-date" v-if="task.createdAt">{{ formatDate(task.createdAt) }}</p>
          <button @click="deleteTask(task.taskID)" class="delete-btn">
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="delete-icon">
              <path d="M3 6h18" />
              <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6" />
              <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2" />
              <line x1="10" y1="11" x2="10" y2="17" />
              <line x1="14" y1="11" x2="14" y2="17" />
            </svg>
          </button>
        </li>
      </ul>
    </section>
  </main>
</template>

<style scoped>
.task-container {
  width: 100%;
  max-width: 50rem;
  margin: 3.125rem auto 0;
  padding: 0 1rem;
  box-sizing: border-box;
  font-family: ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}

.task-header {
  width: 100%;
  border-bottom: 0.0625rem solid #e0e0e0;
  padding: 1rem 0;
  margin-bottom: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.app-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.task-source {
  font-size: 0.75rem;
  color: #666;
}

.task-input-section {
  margin-bottom: 1rem;
  width: 100%;
}

.task-form {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  width: 100%;
}

.task-input {
  flex: 1;
  border: 0.0625rem solid #e0e0e0;
  border-radius: 0.25rem;
  padding: 0.5rem 0.75rem;
  font-size: 0.875rem;
  outline: none;
  transition: border-color 0.2s;
}

.task-input:focus {
  border-color: #8b53ff;
}

.toggle-description-btn {
  background: none;
  border: 0.0625rem solid #e0e0e0;
  border-radius: 0.25rem;
  padding: 0.5rem;
  cursor: pointer;
  color: #666;
  transition: all 0.2s;
}

.toggle-description-btn:hover {
  background-color: #f5f5f5;
}

.toggle-description-btn.active {
  background-color: #f0e6ff;
  color: #8b53ff;
  border-color: #d4c4ff;
}

.task-description-input {
  width: 100%;
  border: 0.0625rem solid #e0e0e0;
  border-radius: 0.25rem;
  padding: 0.5rem 0.75rem;
  font-size: 0.875rem;
  outline: none;
  transition: border-color 0.2s;
  resize: vertical;
  min-height: 3.75rem;
  max-height: 7.5rem;
}

.task-description-input:focus {
  border-color: #8b53ff;
}

.add-task-btn {
  background-color: #8b53ff;
  color: white;
  border: none;
  border-radius: 0.25rem;
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: background-color 0.2s;
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
  margin: 0 0 0.75rem 0;
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
  display: flex;
  align-items: center;
  background-color: white;
  border-radius: 0.25rem;
  padding: 0.75rem;
  transition: opacity 0.3s ease;
  border-left: 0.1875rem solid #8b53ff;
  width: 100%;
}

.task-item.completed {
  background-color: #f0e6ff;
}

.task-item.fade-out {
  opacity: 0;
  transition: opacity 3.5s ease;
}

.task-checkbox {
  width: 1.25rem;
  height: 1.25rem;
  border: 0.0625rem solid #ccc;
  border-radius: 50%;
  margin-right: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  cursor: pointer;
  transition: all 0.2s;
}

.task-checkbox:hover {
  border-color: #8b53ff;
}

.task-checkbox.checked {
  background-color: #8b53ff;
  border-color: #8b53ff;
}

.check-icon {
  color: white;
}

.task-title {
  margin: 0;
  font-size: 0.875rem;
  font-weight: 500;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

.task-description {
  margin: 0.25rem 0 0 2rem;
  font-size: 0.75rem;
  color: #666;
  max-height: 3em;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.task-date {
  margin: 0.25rem 0 0 2rem;
  font-size: 0.7rem;
  color: #888;
}

.task-item.completed .task-title {
  text-decoration: line-through;
  color: #888;
}

.delete-btn {
  background-color: #f5f5f5;
  border: none;
  color: #888;
  cursor: pointer;
  padding: 0.5rem;
  margin-left: 0.5rem;
  border-radius: 0.25rem;
  transition: all 0.2s;
}

.delete-btn:hover {
  background-color: #ffebee;
  color: #ff5252;
}

.notebook-icon-small {
  color: #8b53ff;
  margin-right: 0.5rem;
}

.loading-indicator {
  text-align: center;
  padding: 1rem;
  color: #666;
  display: flex;
  justify-content: center;
}

.loading-icon {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-state {
  text-align: center;
  padding: 1.5rem;
  color: #666;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
}

.notebook-icon {
  color: #8b53ff;
  opacity: 0.5;
}

.error-message {
  background-color: #ffebee;
  color: #c62828;
  padding: 0.5rem 0.75rem;
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

@media (max-width: 48rem) {
  .task-container {
    padding: 0 0.75rem;
    margin-left: 0;
  }

  nav {
    display: none;
  }

  .task-header {
    padding: 0.75rem 0;
  }

  .app-title {
    font-size: 1.25rem;
  }

  .add-task-btn {
    padding: 0.5rem 0.75rem;
  }

  .task-item {
    padding: 0.5rem;
  }
}

@media (max-width: 30rem) {
  .task-container {
    padding: 0 0.5rem;
  }

  .task-header {
    margin-bottom: 0.75rem;
  }

  .app-title {
    font-size: 1.125rem;
  }

  .tasks-section {
    padding: 0.75rem;
  }
}
</style>
