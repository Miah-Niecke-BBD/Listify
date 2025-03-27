<script setup lang="ts">
import { ref } from "vue";
import AssigneesHandler from "@/api/AssigneesHandler";

const props = defineProps<{ taskID: number; assignees: { userID: number; githubID: string }[] | null }>();
const emit = defineEmits(["assignee-added", "assignee-removed"]);

const newGithubID = ref("");

const addAssignee = async () => {
  if (!newGithubID.value) return;
  const newAssignee = await AssigneesHandler.assignTask(props.taskID, newGithubID.value);
  if (newAssignee) emit("assignee-added", { userID: newAssignee.userID, githubID: newGithubID.value });
  newGithubID.value = "";
};

const removeAssignee = async (githubID: string) => {
  const success = await AssigneesHandler.deleteAssignment(props.taskID, githubID);
  if (success) emit("assignee-removed", githubID);
};
</script>

<template>
  <section class="task-assignment">
    <h3>Assign User</h3>
    <input v-model="newGithubID" type="text" placeholder="GitHub Username" />
    <button @click="addAssignee">Assign</button>

    <h3>Assigned Users</h3>
    <ul>
      <li v-if="assignees?.length" v-for="assignee in assignees" :key="assignee.userID">
        {{ assignee.githubID }}
        <button @click="removeAssignee(assignee.githubID)">Remove</button>
      </li>
      <li v-else>No assignees yet</li>
    </ul>
  </section>
</template>

<style scoped>
.task-assignment {
  margin-top: 1rem;
  padding: 1rem;
  background-color: var(--background-color);
  border-radius: 0.8rem;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
}

h3 {
  color: var(--heading-purple);
  font-size: 16pt;
  margin-bottom: .5rem;
}

.assign-form {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

input {
  padding: 4pt 8pt;
  border-radius: 0.3rem;
  border: 0.1rem solid #ccc;
  margin-bottom: 0.5em;
  width: 100%;
  font-size: 1rem;
}

input:focus {
  outline: none;
  border-color: var(--primary-color);
}

button {
  background-color: var(--background-color);
  border: 1pt solid black;
  border-radius: 0.3rem;
  padding: 2pt 12pt;
  text-align: center;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: var(--primary-color);
}

ul {
  list-style: none;
  padding: 0;
}

li {
  background-color: var(--faq-bg);
  padding: .5rem;
  border-radius: 0.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

li button {
  background-color: var(--dark-purple);
  padding: 0.4rem 0.8rem;
  border-radius: 0.3rem;
  cursor: pointer;
  color: var(--white-color);
}

li button:hover {
  background-color: var(--light-purple);
}

p {
  color: var(--light-text-color);
  font-size: 1rem;
}
</style>
