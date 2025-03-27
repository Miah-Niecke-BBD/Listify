<script setup lang="ts">
import { ref } from "vue";
import TaskDependencyHandler from "@/api/TaskDependencyHandler";

const props = defineProps<{ taskID: number; dependencies: { taskID: number; taskName: string }[] }>();
const emit = defineEmits(["dependency-added", "dependency-deleted"]);
const newDependencyID = ref<number | null>(null);

const addDependency = async () => {
  if (newDependencyID.value) {
    const newDependency = await TaskDependencyHandler.createDependency(props.taskID, newDependencyID.value);
    if (newDependency) emit("dependency-added", newDependency);
    newDependencyID.value = null;
  }
};

const removeDependency = async (dependencyID: number) => {
  const success = await TaskDependencyHandler.deleteDependency(dependencyID, props.taskID);
  if (success) emit("dependency-deleted", dependencyID);
};
</script>

<template>
  <section class="task-dependency-manager">
    <h3>Manage Task Dependencies</h3>
    <ul v-if="dependencies.length">
      <li v-for="dependency in dependencies" :key="dependency.taskID">
        {{ dependency.taskName }}
        <button @click="removeDependency(dependency.taskID)">Remove</button>
      </li>
    </ul>
    <p v-else>No dependencies</p>

    <input v-model="newDependencyID" placeholder="Enter task ID" type="number" />
    <button @click="addDependency">Add Dependency</button>
  </section>
</template>

<style scoped>
.task-dependency-manager {
  margin-top: 2rem;
  padding: 1rem;
  background-color: var(--background-color);
  border: 0.1rem solid black;
  border-radius: 0.5rem;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
}

h3 {
  color: var(--heading-purple);
  font-size: 1.5rem;
  margin-bottom: 1rem;
}

ul {
  list-style: none;
  padding: 0;
  margin-bottom: 1rem;
}

li {
  background-color: var(--faq-bg);
  border-radius: 0.3rem;
  padding: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.8rem;
}

button {
  background-color: var(--background-color);
  color: black;
  border: 1pt solid black;
  border-radius: 0.3rem;
  padding: 0.4rem 0.8rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: var(--primary-color);
}

input {
  padding: 0.6rem 1rem;
  border-radius: 0.3rem;
  border: 0.1rem solid #ccc;
  width: 100%;
  margin-bottom: 1rem;
}

input:focus {
  outline: none;
  border-color: var(--primary-color);
}

p {
  color: var(--light-text-color);
  font-size: 1.2rem;
}
</style>


