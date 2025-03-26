<script setup lang="ts">
import "@/assets/base.css";
import AddButton from "@/components/AddButton.vue";
import AddProjectModal from "@/components/AddProjectModal.vue";
import ProjectMembersModal from "@/components/ProjectMembersModal.vue";
import { defineProps, ref } from "vue";
import type { TeamMember } from "@/models/TeamMember";
import type { Project } from "@/models/Project";

const isModalVisible = ref(false);
const isMembersModalVisible = ref(false);
const selectedProject = ref<number | null>(null);

defineProps<{
  projects: Project[];
  loggedInMemberId: number;
  teamMembers: TeamMember[];
}>();

const hoveredAssigneeKey = ref<string | null>(null);

const getInitials = (name: string): string => {
  const nameParts = name.split(" ");
  return nameParts.map((part) => part.charAt(0).toUpperCase()).join("");
};

const emit = defineEmits(["addProject"]);

const openModal = () => {
  isModalVisible.value = true;
};

const closeModal = () => {
  isModalVisible.value = false;
};

const openMembersModal = (projectId: number) => {
  selectedProject.value = projectId;
  isMembersModalVisible.value = true;
};

const closeMembersModal = () => {
  isMembersModalVisible.value = false;
};
</script>

<template>
  <section class="team-projects">
    <h2>Team Projects ({{ projects.length }})</h2>
    <ul>
      <li v-for="project in projects" :key="project.id" class="project-item">
        <RouterLink class="project-name" :to="'/projects/' + project.id">
          {{ project.name }}
        </RouterLink>
        <p class="project-description">{{ project.description }}</p>
        <button
          v-if="loggedInMemberId === teamMembers.find((m) => m.isLeader)?.id"
          class="delete-btn"
          aria-label="Delete Project"
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

        <h3>Project Members</h3>
        <ul class="project-assignees">
          <li v-for="assignee in project.projectAssignees" :key="assignee.id">
            <section
              class="assignee"
              @mouseenter="hoveredAssigneeKey = assignee.id + '-' + project.id"
              @mouseleave="hoveredAssigneeKey = null"
            >
              <p class="initials">{{ getInitials(assignee.name) }}</p>
              <p class="full-name" v-if="hoveredAssigneeKey === assignee.id + '-' + project.id">
                {{ assignee.name }}
              </p>
            </section>
          </li>
        </ul>
        <button
          v-if="loggedInMemberId === teamMembers.find((m) => m.isLeader)?.id"
          class="manage-btn"
          aria-label="Manage Project Members"
          @click="openMembersModal(project.id)"
        >
          Manage Members
        </button>
      </li>
    </ul>
    <AddButton
      v-if="loggedInMemberId === teamMembers.find((m) => m.isLeader)?.id"
      @click="openModal"
    />
    <AddProjectModal :isVisible="isModalVisible" :onClose="closeModal" />
    <ProjectMembersModal
      v-if="selectedProject !== null"
      :isVisible="isMembersModalVisible"
      :onClose="closeMembersModal"
      :projectName="projects.find((p) => p.id === selectedProject)?.name || ''"
      :projectMembers="projects.find((p) => p.id === selectedProject)?.projectAssignees || []"
      :teamMembers="teamMembers"
    />
  </section>
</template>

<style scoped>
.team-projects {
  margin-top: 1.5em;
}

.team-projects h2 {
  margin-bottom: 1em;
}

.team-projects ul {
  list-style-type: none;
  padding: 0.5em;
  display: flex;
  flex-direction: column;
  max-height: calc(100vh - 18em);
  overflow-y: auto;
  height: 100%;
}

.project-item {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 1em 0.5em 1em 1em;
  border: 0.1em solid lightgrey;
  border-radius: 1em;
  box-shadow: 0.2em 0.1em 0.7em rgba(0, 0, 0, 0.2);
  margin-bottom: 1em;
  position: relative;
}

.project-name {
  text-decoration: none;
  color: var(--heading-purple);
  font-size: 15pt;
  font-weight: bold;
}

.project-description {
  font-size: 12pt;
  margin-bottom: 0.8em;
  color: var(--dark-purple);
}

.team-projects h3 {
  font-size: 11.5pt;
}

.team-projects ul.project-assignees {
  margin: 0;
  padding: 0;
  list-style: none;
  display: flex;
  flex-direction: row;
  gap: 1em;
}

.team-projects ul.project-assignees li {
  margin: 1em 0;
}

.assignee .initials {
  border-radius: 50%;
  background-color: var(--primary-color);
  color: white;
  padding: 0.5em;
  font-weight: bold;
  cursor: default;
}

.assignee .initials:hover {
  background-color: var(--light-purple);
}

.assignee .full-name {
  position: absolute;
  background-color: transparent;
  color: var(--light-purple);
}

.delete-btn {
  position: absolute;
  background: none;
  border: none;
  top: 1.5em;
  right: 2em;
  cursor: pointer;
}
.delete-btn svg {
  stroke: red;
}

.delete-btn:hover svg {
  stroke: darkred;
}

.manage-btn {
  position: absolute;
  font-weight: bold;
  background-color: none;
  border: none;
  bottom: 1.5em;
  right: 2em;
  cursor: pointer;
}

.manage-btn:hover {
  opacity: 0.75;
}
</style>
