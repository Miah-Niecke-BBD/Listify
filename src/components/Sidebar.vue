<script setup lang="ts">
import { GetTeams, GetProjects } from "@/api/SidebarApi";
import { onMounted, ref, defineEmits } from "vue";
import type { TeamInterface, ProjectInterface } from "@/models/TeamInterface";
import NavItem from "@/components/NavItem.vue";
import IconCollapse from "./icons/IconCollapse.vue";
import AddTeam from "@/components/AddTeam.vue";
import { addNewTeam } from "@/api/SidebarApi";

const teams = ref<TeamInterface[]>([]);
const myList = ref<ProjectInterface | null>(null);
const emit = defineEmits(["select"]);
const collapsed = ref(false);
const modalOpen = ref(false);

onMounted(async () => {
  const jwtToken: string | null = localStorage.getItem("jwtToken");

  if (jwtToken) {
    try {
      const response: TeamInterface[] | null = await GetTeams(jwtToken);

      if (response) {
        teams.value = response;

        for (const team of teams.value) {
          try {
            const projectResponse: ProjectInterface[] | null = await GetProjects(
              jwtToken,
              team.teamID.toString(),
            );

            if (projectResponse) {
              team.projects = projectResponse;

              const myListProjectIndex = team.projects.findIndex(
                (project) => project.projectName === "MyList",
              );
              if (myListProjectIndex !== -1) {
                if (!myList.value) {
                  myList.value = team.projects.splice(myListProjectIndex, 1)[0];
                }
              }
            }
            GetAllTeams();
          } catch (projectError) {
            console.error(`Error fetching projects for team ${team.teamName}:`, projectError);
          }
        }
      }
    } catch (error) {
      console.error("Error fetching teams:", error);
    }
  }
});

const GetAllTeams = () => {
  emit("select", teams.value);
};

const toggleSidebar = () => {
  collapsed.value = !collapsed.value;
};

const toggleModal = () => {
  modalOpen.value = !modalOpen.value;
};
</script>

<template>
  <aside :class="['sidebar', { collapsed }]">
    <header class="sidebar-header">
      <img src="@/assets/logo.png" alt="Listify Logo" class="logo-image" v-if="!collapsed" />
      <button class="collapse-toggle" @click="toggleSidebar">
        <IconCollapse />
      </button>
    </header>

    <nav class="sidebar-nav" aria-label="Main navigation" v-if="!collapsed">
      <section class="nav-section" v-if="!collapsed">
        <h2 class="nav-section-title">My Lists</h2>
        <ul class="nav-list">
          <li class="nav-item">
            <RouterLink to="/tasklist" class="nav-link" title="My List">
              <span v-if="!collapsed">All Tasks</span>
            </RouterLink>
          </li>
        </ul>
      </section>

      <section class="nav-section" v-if="!collapsed">
        <h2 class="nav-section-title">Calendar</h2>
        <ul class="nav-list">
          <li class="nav-item">
            <RouterLink to="/calendar" class="nav-link" title="Personal Calendar">
              <span v-if="!collapsed">Personal Calendar</span>
            </RouterLink>
          </li>
        </ul>
      </section>

      <section class="nav-section" v-if="!collapsed">
        <section class="nav-section-title">
          <h2 id="teamTitle">Teams</h2>
          <button class="add-button" @click="toggleModal">+</button>
        </section>
        <NavItem :teams="teams" />
      </section>
      <AddTeam :isOpen="modalOpen" :toggleModal="toggleModal" :addTeam="addNewTeam" />
    </nav>
  </aside>
</template>

<style scoped>
.sidebar {
  min-width: 14em;
  background-color: var(--nav-bg-color);
  border-right: 1pt solid var(--card-bg);
  box-shadow: 1pt 1pt 1pt var(--card-bg);
  display: flex;
  flex-direction: column;
  max-height: 100;
  min-height: 100vh;
  transition: width 0.3s ease;
  overflow-y: auto;
}

.sidebar.collapsed {
  min-width: 2em;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12pt;
  border-bottom: 1pt solid #e5e7eb;
  border-bottom: 1pt solid #bfc2c6;
  gap: 10pt;
}

.logo-image {
  width: 10em;
}

.collapse-toggle {
  background-color: var(--nav-bg-color);
  border: none;
  width: 20%;
}

.sidebar-nav {
  flex: 1;
  padding: 16pt 0;
  overflow-y: auto;
}

.nav-section {
  margin-bottom: 1em;
}

.nav-section-title {
  padding: 0 10pt;
  margin-bottom: 8px;
  font-size: 11pt;
  font-weight: bold;
  color: var(--heading-purple);
  text-transform: uppercase;
  letter-spacing: 0.5pt;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nav-section:nth-of-type(2) {
  border-bottom: 1pt solid #cfd0d2;
  padding-bottom: 10pt;
}

.nav-list {
  list-style: none;
  padding: 0;
}

.nav-item {
  margin-bottom: 2px;
}
.nav-item span {
  margin-left: 5pt;
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 8px 16px;
  font-size: 14px;
  color: var(--light-text-color);
  transition: background-color 0.2s ease;
  white-space: nowrap;
  text-decoration: none;
}

.nav-link2 {
  display: flex;
  align-items: center;
  padding: 8px 16px;
  font-size: 10pt;
  color: var(--light-text-color);
  transition: background-color 0.2s ease;
  white-space: nowrap;
  text-decoration: none;
  margin-left: 1em;
}

.nav-link:hover {
  background-color: var(--button-hover-bg);
}

.nav-link.active {
  background-color: #f0f0f5;
  color: var(--primary-color);
  font-weight: 500;
}

.nav-link2:hover {
  background-color: var(--button-hover-bg);
}

.nav-link2.active {
  background-color: #f0f0f5;
  color: var(--primary-color);
  font-weight: 500;
}

.nav-icon {
  width: 20px;
  height: 20px;
  align-items: center;
  justify-content: center;
  margin-right: 8px;
  font-size: 16px;
  text-decoration: none;
}

.sidebar.collapsed .nav-link {
  justify-content: center;
  padding: 8px 0;
}

.sidebar.collapsed .nav-icon {
  margin: 0;
}

.sidebar.collapsed .nav-section-title,
.sidebar.collapsed .nav-link span:not(.nav-icon),
.sidebar.collapsed .add-button {
  display: none;
}

.add-button {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: var(--light-text-color);
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.inline {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.inline button {
  display: flex;
  justify-content: right;
  border: none;
  margin-right: 1em;
  align-items: flex-end;
  padding: 0;
  margin: 0;
}

.inline button:hover {
  background-color: var(--button-hover-bg);
}

.inline button.active {
  color: var(--primary-color);
  font-weight: 500;
}
</style>
