<script setup lang="ts">
import "@/assets/base.css";
import TeamHeader from "@/components/TeamHeader.vue";
import { onMounted, ref, computed } from "vue";
import TeamMember from "@/components/TeamMembers.vue";
import TeamProjects from "@/components/TeamProjects.vue";
import type { Team } from "@/models/Team";

const team = ref<Team | null>(null);

onMounted(async () => {
  const teamId = "1";

  team.value = { id: teamId, name: "Dream Team" };
});

const teamName = computed(() => team.value?.name ?? "Loading...");

const activeTab = ref("members");
const teamMembers = ref([
  { id: 1, name: "John Doe", isLeader: true },
  { id: 2, name: "Jane Smith", isLeader: false },
  { id: 3, name: "Jane Smith", isLeader: false },
  { id: 4, name: "Jane Smith", isLeader: false },
]);
const teamProjects = ref([
  {
    id: 1,
    name: "Project A",
    description: "testing",
    projectAssignees: [
      { id: 1, name: "John Doe", isLeader: true },
      { id: 2, name: "Jane Smith", isLeader: false },
    ],
  },
  {
    id: 2,
    name: "Project B",
    description: "developing",
    projectAssignees: [
      { id: 1, name: "John Doe", isLeader: true },
      { id: 3, name: "Jane Smith", isLeader: false },
      { id: 4, name: "Jane Smith", isLeader: false },
    ],
  },
]);
</script>

<template>
  <section class="team-view">
    <TeamHeader :teamName="teamName" />

    <nav class="tabs">
      <button :class="{ active: activeTab === 'members' }" @click="activeTab = 'members'">
        <svg
          width="20"
          height="20"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"
            fill="currentColor"
          />
        </svg>
        Team Members
      </button>

      <button :class="{ active: activeTab === 'projects' }" @click="activeTab = 'projects'">
        <svg
          width="20"
          height="20"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M5 3H19C20.1 3 21 3.9 21 5V19C21 20.1 20.1 21 19 21H5C3.9 21 3 20.1 3 19V5C3 3.9 3.9 3 5 3ZM5 19H19V5H5V19ZM7 7H17V9H7V7ZM7 11H17V13H7V11ZM7 15H17V17H7V15Z"
            fill="currentColor"
          />
        </svg>
        Team Projects
      </button>
    </nav>
    <hr />
    <TeamMember v-if="activeTab === 'members'" :members="teamMembers" :loggedInMemberId="1" />
    <TeamProjects
      v-if="activeTab === 'projects'"
      :projects="teamProjects"
      :loggedInMemberId="1"
      :teamMembers="teamMembers"
    />
  </section>
</template>

<style scoped>
.team-view {
  display: flex;
  flex-direction: column;
  padding: 0.5em;
  margin: 2em 5em;
}

.tabs {
  display: flex;
  gap: 1em;
}

.tabs button {
  font-size: 13pt;
  padding: 0.8em;
  cursor: pointer;
  border: none;
  background: none;
  display: flex;
  align-items: center;
  gap: 0.5em;
}

.tabs button.active {
  font-size: 14pt;
  text-decoration: underline;
  font-weight: bold;
}

.tabs button svg {
  fill: currentColor;
}

.team-view svg {
  color: var(--primary-color);
}
@media (max-width: 900px) {
  .team-view {
    margin: 2em;
  }
}
</style>
