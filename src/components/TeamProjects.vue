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

const props = defineProps<{
  projects: Project[];
  loggedInMemberId: string;
  teamMembers: TeamMember[];
  addProject: Function;
  deleteProject: Function;
  addProjectAssignee: Function;
  removeProjectAssignee: Function;
}>();

const hoveredAssigneeKey = ref<string | null>(null);

const getInitials = (email: string | undefined): string | undefined => {
  if (!email) return undefined;

  const localPart = email.split("@")[0];

  return localPart
    .split(".")
    .map((part) => part.charAt(0).toUpperCase())
    .join("");
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
      <li v-for="project in projects" :key="project.projectID" class="project-item">
        <RouterLink class="project-name" :to="'/projects/' + project.projectID">
          {{ project.projectName }}
        </RouterLink>
        <p class="project-description">{{ project.projectDescription }}</p>

        <h3>Project Members</h3>
        <ul class="project-assignees">
          <li v-for="assignee in project.projectAssignees" :key="assignee.githubID">
            <section
              class="assignee"
              @mouseenter="hoveredAssigneeKey = assignee.githubID + '-' + project.projectID"
              @mouseleave="hoveredAssigneeKey = null"
            >
              <p class="initials">{{ getInitials(assignee.githubID) }}</p>
              <p
                class="full-name"
                v-if="hoveredAssigneeKey === assignee.githubID + '-' + project.projectID"
              >
                {{ assignee.githubID }}
              </p>
            </section>
          </li>
        </ul>

        <section class="buttons-container">
          <button
            v-if="loggedInMemberId === teamMembers.find((m) => m.teamLeader)?.githubID"
            class="manage-btn"
            aria-label="Manage Project Members"
            @click="openMembersModal(project.projectID)"
          >
            Manage Members
          </button>
          <button
            v-if="loggedInMemberId === teamMembers.find((m) => m.teamLeader)?.githubID"
            class="delete-btn"
            aria-label="Delete Project"
            @click="deleteProject(project.projectID)"
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
        </section>
      </li>
    </ul>
    <AddButton
      v-if="loggedInMemberId === teamMembers.find((m) => m.teamLeader)?.githubID"
      @click="openModal"
    />
    <AddProjectModal
      :isVisible="isModalVisible"
      :onClose="closeModal"
      :addProject="props.addProject"
    />
    <ProjectMembersModal
      v-if="selectedProject !== null"
      :isVisible="isMembersModalVisible"
      :onClose="closeMembersModal"
      :projectID="selectedProject"
      :projectName="projects.find((p) => p.projectID === selectedProject)?.projectName || ''"
      :projectMembers="
        projects.find((p) => p.projectID === selectedProject)?.projectAssignees || []
      "
      :teamMembers="teamMembers"
      :addProjectAssignee="addProjectAssignee"
      :removeProjectAssignee="removeProjectAssignee"
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
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 1em;
  max-height: calc(100vh - 24em);
  overflow-y: auto;
  justify-content: left;
}

.project-item {
  position: relative;
  display: flex;
  flex-direction: column;
  padding: 0.5em;
  border: 0.1em solid lightgrey;
  border-radius: 1em;
  box-shadow: 0.2em 0.1em 0.7em rgba(0, 0, 0, 0.2);
  margin-bottom: 1em;
  min-width: 20em;
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
  flex-wrap: wrap;
  justify-content: left;
  gap: 0.5em;
  margin: 1em 0;
}

.team-projects ul.project-assignees li {
  margin-right: 0.1em;
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
  word-wrap: break-word;
  background-color: transparent;
  color: var(--light-purple);
}

.buttons-container {
  display: flex;
  justify-content: space-between;
  padding-top: 1em;
  margin-top: auto;
}

.delete-btn,
.manage-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-weight: bold;
}

.delete-btn svg {
  stroke: red;
}

.delete-btn:hover svg {
  stroke: darkred;
}

.manage-btn:hover {
  opacity: 0.75;
}

@container (max-width: 450px) {
  .team-projects h2 {
    font-size: 18pt;
  }
  .team-projects h3 {
    font-size: 12pt;
  }
  .team-projects ul {
    flex-direction: row;
  }
  .project-item {
    min-width: 100%;
  }
}
</style>
