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
  margin-top: 1rem;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 8px;
}
button {
  margin-left: 0.5rem;
  color: #fff;
  background-color: #ca6de8;
  border: none;
  border-radius: 5px;
  padding: 0.2rem 0.5rem;
  cursor: pointer;
}
button:hover {
  background-color: #a150c7;
}
</style>
