<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import TaskDependencyHandler from "@/api/TaskDependencyHandler";
import { GetDueTasksForProject } from "@/api/Calendar"; 

const props = defineProps<{ taskID: number; dependencies: { taskID: number; taskName: string }[] }>();
const emit = defineEmits(["dependency-added", "dependency-deleted"]);

const newDependencyID = ref<number | null>(null);
const newDependencyName = ref<string | null>('');  // Initialize as empty string
const dueTasks = ref<{ taskID: number; taskName: string }[]>([]);
const showDropdown = ref(false);

const route = useRoute();

const fetchDueTasks = async () => {
  const projectID = route.params.id as string;
  const jwtToken = localStorage.getItem("jwtToken");
  if (jwtToken && projectID) {
    const tasks = await GetDueTasksForProject(jwtToken, projectID);
    if (tasks) {
      dueTasks.value = tasks as { taskID: number; taskName: string }[];
    }
  }
};

const addDependency = async () => {
  if (newDependencyID.value) {
    const newDependency = await TaskDependencyHandler.createDependency(props.taskID, newDependencyID.value);
    if (newDependency) emit("dependency-added", newDependency);
    newDependencyID.value = null;
    newDependencyName.value = '';  // Reset task name after adding the dependency
    showDropdown.value = false;
  }
};

const removeDependency = async (dependencyID: number) => {
  const success = await TaskDependencyHandler.deleteDependency(dependencyID, props.taskID);
  if (success) emit("dependency-deleted", dependencyID);
};
</script>

<template>
  <section class="task-dependency-manager">
    <!-- Input field that binds to newDependencyName -->
    <input
      v-model="newDependencyName"
      placeholder="Click to select task"
      type="text"
      @focus="fetchDueTasks(); showDropdown = true"
      :readonly="true" 
    />

    <!-- Dropdown showing task options -->
    <ul v-if="showDropdown" class="dropdown">
      <li
        v-for="task in dueTasks"
        :key="task.taskID"
        @click="newDependencyID = task.taskID; newDependencyName = task.taskName; showDropdown = false"
      >
        {{ task.taskName }}
      </li>
    </ul>

    <button @click="addDependency">Add Dependency</button>


    <h3>Dependencies</h3>
    <ul v-if="props.dependencies.length">
      <li v-for="dependency in props.dependencies" :key="dependency.taskID">
        {{ dependency.taskName }}
        <button id="removeBtn" @click="removeDependency(dependency.taskID)">Remove</button>
      </li>
    </ul>
  </section>
</template>

<style scoped>
.task-dependency-manager {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 0.5rem;
  padding: 1rem;
  width: 25rem;
  background-color: var(--background-color);
  border: 0.1rem solid rgba(0, 0, 0, 0.134);
  border-radius: 0.5rem;
  box-shadow: 1pt 1pt 1pt rgba(0, 0, 0, 0.1);
  position: relative;
}

h3{
  color: var(--heading-purple);
  font-size: 16pt;
  margin-bottom: .5rem;
}

input {
  padding: 0.6rem 1rem;
  border-radius: 0.23rem;
  border: 0.1rem solid #ccc;
  width: 15em;
  margin-bottom: .5rem;
}

input:focus {
  outline: none;
  border-color: var(--primary-color);
}

button {
  display: flex;
  flex-flow: row wrap;
  justify-content: space-evenly;
  width: 12rem;
  margin-top: .5rem;
  padding: 0.5rem;
  background-color: var(--primary-color);
  border: 0.05rem solid rgba(8, 1, 42, 0.678);
  box-shadow: 1pt 1pt 1pt rgb(47, 0, 255);
  color: white;
  border-radius: 0.5rem;
  cursor: pointer;
}   

button:hover {
  background-color: rgb(25, 0, 137);
}

ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0.75rem;
  border: 0.05rem solid #e2e2e2;
  border-radius: 0.5rem;
  background-color: #fafafa;
  margin-bottom: 0.5rem;
}

#removeBtn {
  background-color: red;
  border: 0.05rem solid rgb(105, 1, 1);
  box-shadow: 1pt 1pt 1pt rgb(100, 0, 0);
  color: white;
  border: none;
  padding: 0.3rem 0.6rem;
  border-radius: 0.3rem;
  margin-left: 1em;
  width: 5rem;
  cursor: pointer;
}

#removeBtn:hover {
  background-color: darkred;
}

.dropdown {
  max-height: 200px;
  overflow-y: auto;
  width: 100%;
  border: 1pt solid #ccc;
  border-radius: 0.5rem;
  background-color: var(--background-color);
}

.dropdown li {
  padding: 0.5rem;
  cursor: pointer;
}

.dropdown li:hover {
  background-color: rgb(244, 244, 244);
}
</style>
