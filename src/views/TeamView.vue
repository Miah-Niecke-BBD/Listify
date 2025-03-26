<script setup lang="ts">
import "@/assets/base.css";
import { useRoute } from "vue-router";
import TeamHeader from "@/components/TeamHeader.vue";
import { onMounted, ref } from "vue";
import TeamMemberList from "@/components/TeamMembersList.vue";
import TeamProjects from "@/components/TeamProjects.vue";
import type { Team } from "@/models/Team";
import type { TeamMember } from "@/models/TeamMember";
import type { Project } from "@/models/Project";
import {
  fetchTeamByID,
  fetchTeamMembers,
  fetchTeamProject,
  updateTeam,
  addTeamMember,
  removeMemberFromTeam,
  updateTeamLeader,
  createProject,
  deleteProject,
  addProjectAssignee,
  deleteProjectAssignee,
} from "@/api/TeamApiCalls";

const route = useRoute();
const teamID = route.params.id as string;

const team = ref<Team | null>(null);
const teamMembers = ref<TeamMember[]>([]);
const teamProjects = ref<Project[]>([]);
const teamName = ref("Loading...");

onMounted(async () => {
  try {
    team.value = await fetchTeamByID(teamID);
    teamMembers.value = await fetchTeamMembers(teamID);
    teamProjects.value = await fetchTeamProject(teamID, teamMembers.value);
    if (team.value?.teamName) {
      teamName.value = team.value.teamName;
    }
  } catch (error) {
    console.error("Error fetching:", error);
  }
});

const updateTeamName = async (newTeamName: string) => {
  if (!newTeamName.trim()) {
    return;
  }

  try {
    await updateTeam(teamID, newTeamName);
    teamName.value = newTeamName;
  } catch (error) {
    throw error;
  }
};

const addMemberToTeam = async (googleID: string) => {
  try {
    const updatedMembers: TeamMember[] = await addTeamMember(teamID, googleID);
    teamMembers.value = updatedMembers;
  } catch (error) {
    throw error;
  }
};

const removeMember = async (googleID: string) => {
  try {
    await removeMemberFromTeam(teamID, googleID);
    teamMembers.value = teamMembers.value.filter((member) => member.githubID != googleID);
  } catch (error) {
    throw error;
  }
};

const changeTeamLeader = async (googleID: string) => {
  try {
    await updateTeamLeader(teamID, googleID);
  } catch (error) {
    throw error;
  }
};

const addProject = async (projectName: string, projectDescription: string) => {
  const loggedInUser: TeamMember = teamMembers.value.filter((m) => m.teamLeader)[0];

  try {
    const newProject: Project = await createProject(
      teamID,
      projectName,
      projectDescription,
      loggedInUser,
    );

    teamProjects.value.push(newProject);
  } catch (error) {
    throw error;
  }
};

const removeProject = async (projectID: number) => {
  try {
    await deleteProject(projectID);

    teamProjects.value = teamProjects.value.filter((p) => p.projectID !== projectID);
  } catch (error) {
    throw error;
  }
};

const addAProjectAssignee = async (projectID: number, googleID: string) => {
  const updatedProject: Project | undefined = teamProjects?.value.find(
    (project) => project.projectID === projectID,
  );

  if (updatedProject?.projectAssignees.filter((assignee) => assignee.githubID == googleID)[0])
    return;

  try {
    await addProjectAssignee(projectID, googleID);
    const member: TeamMember | undefined = teamMembers?.value.filter(
      (m) => m.githubID == googleID,
    )[0];
    updatedProject?.projectAssignees.push(member);
  } catch (error) {
    throw error;
  }
};

const removeAProjectAssignee = async (projectID: number, googleID: string) => {
  const updatedProject: Project | undefined = teamProjects?.value.find(
    (project) => project.projectID === projectID,
  );

  if (!updatedProject?.projectAssignees.filter((assignee) => assignee.githubID == googleID)[0])
    return;

  try {
    await deleteProjectAssignee(projectID, googleID);
    teamProjects.value = teamProjects.value.map((p) =>
      p.projectID === projectID
        ? {
            ...p,
            projectAssignees: p.projectAssignees.filter(
              (assignee) => assignee.githubID !== googleID,
            ),
          }
        : p,
    );
  } catch (error) {
    throw error;
  }
};

const activeTab = ref("members");
</script>

<template>
  <section class="team-view">
    <TeamHeader
      :teamName="teamName"
      :teamID="teamID"
      loggedInMemberId="102994491153243422001"
      :updateTeamName="updateTeamName"
      :teamMembers="teamMembers"
      :changeTeamLeader="changeTeamLeader"
    />

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
    <TeamMemberList
      v-if="activeTab === 'members'"
      :members="teamMembers"
      loggedInMemberId="102994491153243422001"
      :addAMember="addMemberToTeam"
      :deleteAMember="removeMember"
    />
    <TeamProjects
      v-if="activeTab === 'projects'"
      :projects="teamProjects"
      loggedInMemberId="102994491153243422001"
      :teamMembers="teamMembers"
      :addProject="addProject"
      :deleteProject="removeProject"
      :addProjectAssignee="addAProjectAssignee"
      :removeProjectAssignee="removeAProjectAssignee"
    />
  </section>
</template>

<style scoped>
.team-view {
  display: flex;
  flex-direction: column;
  padding: 0.5em;
  margin: 2em 5em;
  width: 100%;
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
