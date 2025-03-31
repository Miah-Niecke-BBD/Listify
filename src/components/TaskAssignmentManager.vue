<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router"; 
import AssigneesHandler from "@/api/AssigneesHandler";
import type { taskAssignee } from "@/models/TaskAssignees";

const assignees = ref<taskAssignee[]>([]) || null;
const props = defineProps<{ taskID: number }>();
const emit = defineEmits(["assignee-added", "assignee-removed"]);

const newGitHubID = ref("");
const members = ref<{ gitHubID: string }[]>([]);
const isDropdownVisible = ref(false);

const route = useRoute();
const projectID = route.params.id;

const fetchProjectMembers = async () => {
  if (projectID) {
    try {
      const jwtToken = localStorage.getItem("jwtToken");
      const data = await AssigneesHandler.GetProjectMembers(jwtToken, projectID as string);
      
    
      members.value = data.map((member: any) => ({
        ...member,
        gitHubID: member.githubID,  
      }));
    } catch (error) {
      console.error("Error fetching project members:", error);
    }
  }
};

const addAssignee = async () => {
  if (!newGitHubID.value) return;
  const newAssignee = await AssigneesHandler.assignTask(props.taskID, newGitHubID.value);
  if (newAssignee) {
    const assigneeWithCorrectCase: taskAssignee = {
      userID: newAssignee.userID,
      gitHubID: newGitHubID.value, 
    };

    emit("assignee-added", assigneeWithCorrectCase);
    assignees.value.push(assigneeWithCorrectCase);
    newGitHubID.value = "";
  }
};

const removeAssignee = async (gitHubID: string) => {
  const success = await AssigneesHandler.deleteAssignment(props.taskID, gitHubID);
  if (success) {
    emit("assignee-removed", gitHubID);
    assignees.value = assignees.value.filter(a => a.gitHubID !== gitHubID);
  }
};

const fetchTaskAssignees = async () => {
  try {
    console.log("working");
    const jwtToken = localStorage.getItem("jwtToken");
    const data: taskAssignee[] | null = await AssigneesHandler.GetTaskMembers(jwtToken, props.taskID.toString());
    assignees.value = data || [];
  } catch (error) {
    console.error("Error fetching task assignees:", error);
  }
};

const toggleDropdown = () => {
  isDropdownVisible.value = !isDropdownVisible.value;
};

onMounted(() => {
  fetchProjectMembers();
  fetchTaskAssignees();
});
</script>

<template>
  <section class="task-assignment">
    <h3>Assign User</h3>
    <section class="input-container">
      <input 
        v-model="newGitHubID" 
        type="text" 
        placeholder="GitHub Username" 
        @focus="toggleDropdown"
      />
      <ul v-if="isDropdownVisible" class="dropdown">
        <li 
          v-for="member in members" 
          :key="member.gitHubID"
          @click="newGitHubID = member.gitHubID; isDropdownVisible = false"
        >
          {{ member.gitHubID }}
        </li>
      </ul>
    </section>
    <button id="addButton" @click="addAssignee">Assign</button>

    <h3>Assigned Users</h3>
    <ul>
      <template v-if="assignees.length">
        <li v-for="assignee in assignees" :key="assignee.userID">
          {{ assignee.gitHubID }}
          <button id="removeBtn" @click="removeAssignee(assignee.gitHubID)">Remove</button>
        </li>
      </template>
      <li v-else>No assignees yet</li>
    </ul>
  </section>
</template>

<style scoped>
.task-assignment {
  display: flex;
  flex-flow: wrap column;
  align-items: center;
  margin-top: .5rem;
  padding: .5rem;
  width: 25rem;
  background-color: var(--background-color);
  border: 0.1rem solid rgba(0, 0, 0, 0.134);
  border-radius: 0.5rem;
  box-shadow: 1pt 1pt 1pt rgba(0, 0, 0, 0.1);
}

h3 {
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

#addButton {
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
  margin-left: 1em;
  width: 5rem;
  border-radius: 0.3rem;
  cursor: pointer;
}

#removeBtn:hover {
  background-color: darkred;
}

.dropdown {
  max-height: 5em;
  overflow-y: auto; 
  width: 20em;
  border: 1pt solid #ccc;
  border-radius: 0.5rem;
  background-color: var(--background-color);
}

.dropdown li {
  background-color: white;
  border: 1pt solid black;
  padding: .5rem;
  margin: 0;
  gap: 0;
  border-radius: 0;
  cursor: pointer;
}

.dropdown li:hover {
  background-color: rgb(244, 244, 244);
}


</style>
