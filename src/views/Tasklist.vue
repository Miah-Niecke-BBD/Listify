<script setup lang="ts">
import Sidebar from '@/components/Sidebar.vue';
import { GetTeams, GetProjects } from '@/api/SIdebarApi';
import { onMounted, ref } from 'vue';
import type { TeamInterface, ProjectInterface } from '@/models/TeamInterface';

const teams = ref<TeamInterface[]>([]);
const myList = ref<ProjectInterface | null>(null); 
const collapsed = ref(false);

onMounted(async () => {
  const jwtToken: string | null = localStorage.getItem('jwtToken');

  if (jwtToken) {
    try {
      const response: TeamInterface[] | null = await GetTeams(jwtToken);
      if (response) {
        teams.value = response;

        const myProjectsTeam = teams.value
          .filter(team => team.teamName === "MyProjects")
          .sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime())[0];

        if (myProjectsTeam) {
          const projectResponse: ProjectInterface[] | null = await GetProjects(jwtToken, myProjectsTeam.teamID.toString());
          
          if (projectResponse) {
            myProjectsTeam.projects = projectResponse;

            const myListProjectIndex = myProjectsTeam.projects.findIndex(project => project.projectName === "MyList");
            
            if (myListProjectIndex !== -1) {
              myList.value = myProjectsTeam.projects.splice(myListProjectIndex, 1)[0];
            }
          }
        }
      }
    } catch (error) {
      console.error(error);
    }
  }
});
</script>



<!-- <script setup >
import { ref } from "vue";
import TaskCard from "../components/TaskCard.vue";
import AddTaskDialog from "../components/AddTaskDialog.vue";
import Sidebar from "@/components/Sidebar.vue";


 {
  name: "TaskList",
  components: { TaskCard, AddTaskDialog },
  setup() {
    const initialTasks = [
      { id: "1", title: "Complete project proposal", completed: false },
      { id: "2", title: "Schedule team meeting", completed: false },
    ];
    const tasks = ref([...initialTasks]);
    const nextTaskId = ref(3);
    const newTaskTitle = ref("");
    const isTaskDialogOpen = ref(false);
    const newTask = ref({
      title: "",
      description: "",
      dueDate: "",
      priority: "medium",
    });
  

    function toggleTaskCompletion(taskId) {
      tasks.value = tasks.value.map((task) =>
        task.id === taskId ? { ...task, completed: !task.completed } : task,
      );
    }
    function addNewTask() {
      if (newTaskTitle.value.trim()) {
        tasks.value.push({
          id: String(nextTaskId.value++),
          title: newTaskTitle.value.trim(),
          completed: false,
        });
        newTaskTitle.value = "";
      }
    }
    function openTaskDialog() {
      newTask.value = {
        title: "",
        description: "",
        dueDate: "",
        priority: "medium",
        project: "none",
      };
      isTaskDialogOpen.value = true;
    }
    function closeTaskDialog() {
      isTaskDialogOpen.value = false;
    }
    function saveNewTask(task) {
      tasks.value.push({ ...task, id: String(nextTaskId.value++), completed: false });
      closeTaskDialog();
    }
    
    function formatDate(dateString) {
      return dateString ? new Date(dateString).toLocaleDateString() : "";
    }

    return {
      tasks,
      newTaskTitle,
      isTaskDialogOpen,
      newTask,
      toggleTaskCompletion,
      addNewTask,
      openTaskDialog,
      closeTaskDialog,
      saveNewTask,
      formatDate,
    };
  },
};
</script>

<template>
  <Sidebar />
  <main class="main-content">
    <header class="main-header">
      <h1 class="page-title">My List</h1>
      <section class="header-actions">
        
      </section>
    </header>
    <section class="content-area">
      <form @submit.prevent="addNewTask" class="add-task-form">
        <section class="form-controls">
          <input
            v-model="newTaskTitle"
            type="text"
            placeholder="Add a new task..."
            class="task-input"
          />
          <button type="submit" class="add-task-btn">+ Add Task</button>
          <button @click.prevent="openTaskDialog" type="button" class="advanced-btn">
            + Add Advanced Task
          </button>
        </section>
      </form>
      <section class="tasks-container">
        <header class="tasks-header">
          <h2 class="tasks-title">Tasks</h2>
        </header>
        <section class="tasks-grid">
          <task-card
            v-for="task in tasks"
            :key="task.id"
            :task="task"
            @toggle-completion="toggleTaskCompletion"
          />
        </section>
      </section>
      <add-task-dialog
        :is-open="isTaskDialogOpen"
        @close="closeTaskDialog"
        @save-task="saveNewTask"
      />
    </section>
  </main>
</template>

<style scoped>
.main-content {
  flex: 1;
  overflow: auto;
}
.main-header {
  display: flex;
  height: 64px;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #e5e7eb;
  padding: 0 24px;
}
.page-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--heading-purple);
}
.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}
.new-project-btn {
  display: inline-flex;
  align-items: center;
  font-size: 14px;
  font-weight: 500;
  padding: 6px 12px;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  background-color: transparent;
  transition: background-color 0.2s ease;
}
.new-project-btn:hover {
  background-color: var(--button-hover-bg);
}
.content-area {
  padding: 24px;
}
.add-task-form {
  margin-bottom: 24px;
}
.form-controls {
  display: flex;
  gap: 8px;
}
.task-input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  font-size: 14px;
}
.add-task-btn {
  display: inline-flex;
  align-items: center;
  font-size: 14px;
  font-weight: 500;
  padding: 8px 16px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 6px;
  transition: background-color 0.2s ease;
}
.add-task-btn:hover {
  background-color: var(--dark-purple);
}
.advanced-btn {
  display: inline-flex;
  align-items: center;
  font-size: 14px;
  font-weight: 500;
  padding: 8px 16px;
  border: 1px solid #e5e7eb;
  background-color: transparent;
  border-radius: 6px;
  transition: background-color 0.2s ease;
}
.advanced-btn:hover {
  background-color: var(--button-hover-bg);
}
.tasks-container {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background-color: var(--white-color);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}
.tasks-header {
  border-bottom: 1px solid #e5e7eb;
  background-color: var(--button-hover-bg);
  padding: 12px 16px;
}
.tasks-title {
  font-size: 18px;
  font-weight: 500;
  color: var(--heading-purple);
}
.tasks-grid {
  padding: 16px;
  display: grid;
  gap: 16px;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
}
@media (max-width: 768px) {
  .tasks-grid {
    grid-template-columns: 1fr;
  }
}
</style> -->

<template>
  <main>
  <nav>
    <Sidebar />
  </nav>
      {{ teams }}
      {{ myList }}
  </main>

</template>

<style scoped>

main{
  overflow-x: hidden;
    overflow-y: hidden;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: left;
}
</style>