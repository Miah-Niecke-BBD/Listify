<script setup lang="ts">
import { ref } from "vue";
import "@/assets/base.css";
import AddTeamMemberModal from "@/components/EditTeamModal.vue";
import AddButton from "@/components/AddButton.vue";
import type { TeamMember } from "@/models/TeamMember";

const isModalVisible = ref(false);

defineProps<{
  members: TeamMember[];
  loggedInMemberId: string;
  addAMember: Function;
  deleteAMember: Function;
}>();

const openModal = () => {
  isModalVisible.value = true;
};

const closeModal = () => {
  isModalVisible.value = false;
};
</script>

<template>
  <section class="team-members">
    <h2>Team Members ({{ members.length }})</h2>
    <ul>
      <li v-for="member in members" :key="member.githubID" class="member-item">
        <h3 class="member-name">{{ member.name }}</h3>
        <p class="member-id">ID: {{ member.githubID }}</p>
        <p v-if="member.teamLeader" class="leader-label">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="#d4af37"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M3 10l4.5 4L12 4l4.5 10 4.5-4v10H3V10z" />
            <path d="M2 20h20" />
          </svg>

          Team Leader
        </p>
        <button
          v-if="
            loggedInMemberId === members.find((m) => m.teamLeader)?.githubID && !member.teamLeader
          "
          class="delete-btn"
          @click="deleteAMember(member.githubID)"
          aria-label="Delete TeamMember"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="red"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path
              d="M3 6h18M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6m4 5v6m6-6v6"
            />
          </svg>
        </button>
      </li>
    </ul>
    <AddButton
      v-if="loggedInMemberId === members.find((m) => m.teamLeader)?.githubID"
      @click="openModal"
    />
    <AddTeamMemberModal
      mode="addMember"
      :isVisible="isModalVisible"
      :onClose="closeModal"
      :onAddMember="addAMember"
    />
  </section>
</template>

<style scoped>
.team-members {
  margin-top: 1.5em;
}

.team-members h2 {
  margin-bottom: 1em;
}

.team-members h3 {
  color: black;
}

.team-members ul {
  padding: 0.5em;
  display: flex;
  flex-direction: column;
  max-height: calc(100vh - 18em);
  overflow-y: auto;
}

.member-item {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 1em;
  border: 0.1em solid lightgrey;
  border-radius: 1em;
  box-shadow: 0.2em 0.1em 0.7em rgba(0, 0, 0, 0.2);
  margin-bottom: 1em;
  position: relative;
}

.member-name {
  font-size: 15pt;
  font-weight: bold;
  margin-bottom: 0.5em;
}

.member-id {
  font-size: 12pt;
  margin-bottom: 0.5em;
  color: var(--dark-purple);
  margin-left: 0.5em;
}

.leader-label {
  font-size: 12pt;
  color: #d4af37;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 0.3em;
  position: absolute;
  top: 1em;
  right: 1.5em;
}

.delete-btn {
  background: none;
  border: none;
  cursor: pointer;
  position: absolute;
  bottom: 1.2em;
  right: 1.8em;
}

.delete-btn svg {
  stroke: red;
}

.delete-btn:hover svg {
  stroke: darkred;
}

@container (max-width: 500px) {
  .team-members {
    word-wrap: break-word;
  }
  .leader-label {
    position: relative;
    top: 0em;
    right: 0em;
  }

  .delete-btn {
    position: relative;
    bottom: 0em;
    right: 0em;
    margin-left: auto;
    bottom: 1.1em;
  }
}
</style>
