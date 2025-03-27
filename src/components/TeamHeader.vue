<script setup lang="ts">
import "@/assets/base.css";
import AddTeamMemberModal from "@/components/EditTeamModal.vue";
import { ref } from "vue";
import ReassignLeaderModal from "./ReassignLeaderModal.vue";
import type { TeamMember } from "@/models/TeamMember";

const props = defineProps<{
  teamName: string;
  teamID: string;
  loggedInMemberId: string;
  updateTeamName: Function;
  teamMembers: TeamMember[];
  changeTeamLeader: Function;
  deleteTeam: Function;
}>();

const showOptions = ref(false);
const isAddTeamMemberModalVisible = ref(false);
const isReassignLeaderModalVisible = ref(false);

const emit = defineEmits(["handleUpdateTeam"]);

const openAddTeamMemberModal = () => {
  isAddTeamMemberModalVisible.value = true;
};

const openReassignLeaderModal = () => {
  isReassignLeaderModalVisible.value = true;
};

const closeModal = () => {
  isAddTeamMemberModalVisible.value = false;
  isReassignLeaderModalVisible.value = false;
};

const deleteATeam = async () => {
  await props.deleteTeam();
};
</script>

<template>
  <section class="team-heading">
    <h1>{{ teamName }}</h1>
    <RouterLink to="/team/1/calendar" class="calendar-btn">
      <svg
        width="20"
        height="20"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M3 8H21M16 2V5M8 2V5M7 11H17M7 15H14M5 22H19C20.1 22 21 21.1 21 20V6C21 4.9 20.1 4 19 4H5C3.9 4 3 4.9 3 6V20C3 21.1 3.9 22 5 22Z"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
      </svg>
      Team Calendar
    </RouterLink>
    <section
      v-if="loggedInMemberId.trim() === teamMembers.find((m) => m.teamLeader)?.githubID.trim()"
      @mouseenter="showOptions = true"
      @mouseleave="showOptions = false"
      class="more-btn"
    >
      <i class="more-btn-icon" role="button" aria-label="More options">...</i>
      <section v-if="showOptions" class="more-options">
        <button @click="openAddTeamMemberModal">Edit Team</button>
        <button @click="openReassignLeaderModal">Edit Roles</button>
        <button @click="deleteATeam">Delete Team</button>
      </section>
    </section>
    <AddTeamMemberModal
      mode="updateTeamName"
      :isVisible="isAddTeamMemberModalVisible"
      :onClose="closeModal"
      :onUpdateTeamName="updateTeamName"
    />
    <ReassignLeaderModal
      :isVisible="isReassignLeaderModalVisible"
      :onClose="closeModal"
      :teamName="teamName"
      :teamMembers="teamMembers"
      :changeTeamLeader="changeTeamLeader"
    />
  </section>
</template>

<style scoped>
.team-heading {
  display: flex;
  flex-direction: row;
  margin-bottom: 2em;
  align-items: center;
  border: 0.1em solid var(--dark-purple);
  border-radius: 1em;
  padding: 1em;
  justify-content: center;
}

.team-heading h1 {
  font-size: 26pt;
}

.team-heading .calendar-btn {
  display: flex;
  align-items: center;
  color: black;
  text-decoration-line: none;
  border: 0.1rem solid black;
  border-radius: 0.4em;
  cursor: pointer;
  text-align: center;
  gap: 0.2em;
  margin-left: auto;
  margin-right: 1.5em;
  padding: 0.5em 0.8em;
}

.team-heading .calendar-btn:hover {
  background-color: var(--button-hover-bg);
}

.team-heading .calendar-btn svg {
  background-color: transparent;
}

.more-btn {
  position: relative;
  display: inline-block;
}

.more-btn-icon {
  font-size: 30pt;
  cursor: pointer;
  background: none;
  display: inline-block;
  user-select: none;
}

.more-options {
  position: absolute;
  top: 100%;
  left: -2.3em;
  width: 6em;
  background: rgb(228, 227, 227);
  border: 0.1em solid #ccc;
  border-radius: 0.5em;
  box-shadow: 0 0.3em 0.6em rgba(0, 0, 0, 0.1);
  padding: 0.5em;
  display: flex;
  flex-direction: column;
  gap: 0.2em;
  z-index: 1000;
}

.more-options button {
  background: none;
  border: 0.1em solid transparent;
  border-radius: 0.4em;
  cursor: pointer;
  padding: 0.2em;
}

.more-options button:hover {
  background-color: #f0f0f0;
}

svg {
  color: var(--primary-color);
}

@container (max-width: 580px) {
  .team-heading {
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 0.5em;
  }
  .team-heading h1 {
    font-size: 20pt;
    text-align: center;
  }

  .team-heading .calendar-btn {
    font-size: 10pt;
    padding: 0.2em 0.8em;
    margin: 1em 0;
  }

  .team-heading .more-btn {
    font-size: 20pt;
  }

  .more-btn-icon {
    text-align: center;
    line-height: 0.6em;
  }

  .more-options {
    left: -1.5em;
    width: 4em;
    padding: 0.3em;
    gap: 0.1em;
  }

  .more-options button {
    padding: 0em;
  }
}
</style>
