<script setup lang="ts">
import { ref } from "vue";
import AssigneesHandler from "@/api/AssigneesHandler.ts";

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
}

input {
  margin-right: 0.5rem;
}

button {
  margin-left: 0.5rem;
  cursor: pointer;
}
</style>
