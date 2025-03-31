<script setup lang="ts">
import { GetTeams, GetProjects } from "@/api/SidebarApi";
import { onMounted, ref, defineEmits, onUnmounted } from "vue";
import { useRoute } from "vue-router";
import type { TeamInterface, ProjectInterface } from "@/models/TeamInterface";
import NavItem from "@/components/NavItem.vue";
import IconCollapse from "./icons/IconCollapse.vue";
import AddTeam from "@/components/AddTeam.vue";
import { addNewTeam } from "@/api/SidebarApi";

const teams = ref<TeamInterface[]>([]);
const myList = ref<ProjectInterface | null>(null);
const emit = defineEmits(["select"]);
const collapsed = ref(window.innerWidth <= 700);
const modalOpen = ref(false);
const route = useRoute();

const handleResize = () => {
  collapsed.value = window.innerWidth <= 700;
};

onMounted(async () => {
  window.addEventListener("resize", handleResize)

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

onUnmounted(() => {
  window.removeEventListener("resize", handleResize);
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

const isActive = (path: string) => {
  return route.path.startsWith(path);
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
      <section class="nav-section">
        <h2 class="nav-section-title">Home Page</h2>
        <ul class="nav-list">
          <li class="nav-item">
            <RouterLink to="/tutorial" class="nav-link" :class="{ active: isActive('/tutorial') }" title="My List">
              <span>Introduction</span>
            </RouterLink>
          </li>
        </ul>
      </section>

      <section class="nav-section">
        <h2 class="nav-section-title">Calendar</h2>
        <ul class="nav-list">
          <li class="nav-item">
            <RouterLink to="/calendar" class="nav-link" :class="{ active: isActive('/calendar') }" title="Personal Calendar">
              <span>Personal Calendar</span>
            </RouterLink>
          </li>
        </ul>
      </section>

      <section class="teams-container">
        <section class="teams-header">
          <h2 id="teamTitle">TEAMS</h2>
          <button class="add-button" @click="toggleModal">+</button>
        </section>

        <section class="teams-scrollable">
          <NavItem :teams="teams" />
        </section>
      </section>
    </nav>

    <AddTeam :isOpen="modalOpen" :toggleModal="toggleModal" :addTeam="addNewTeam" />

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
  max-height: 100vh;
  min-height: 100vh;
  transition: width 0.3s ease;
  overflow-y: auto;
}

.sidebar.collapsed {
  min-width: 2em;}

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
  cursor: pointer;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  height: 100%;
  flex: 1;
  padding: 16pt 0;
  overflow-y: auto;
}

.teams-container {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  min-height: 0;
}

.teams-header {
  position: sticky;
  top: 0;
  background: var(--nav-bg-color);
  z-index: 10;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10pt;
}

.teams-scrollable {
  flex-grow: 1;
  overflow-y: auto;
}

.nav-section {
  margin-bottom: 1em;
}

.nav-section-title {
  padding: 0 10pt;
  margin-bottom: 0.5em;
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
  margin-bottom: 0.13em;
}

.nav-item span {
  margin-left: 5pt;
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 0.5em 1em;
  font-size: 14px;
  color: var(--light-text-color);
  transition: background-color 0.2s ease;
  white-space: nowrap;
  text-decoration: none;
}

.nav-link.active {
  background-color: var(--button-hover-bg);
  color: var(--primary-color);
  font-weight: bold;
}

.nav-link2 {
  display: flex;
  align-items: center;
  padding: 0.5em 1em;
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
  width: 1.25em;
  height: 1.25em;
  align-items: center;
  justify-content: center;
  margin-right: 0.5em;
  font-size: 1em;
  text-decoration: none;
}

.sidebar.collapsed .nav-link {
  justify-content: center;
  padding: 0.53em 0;
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
  font-size: 1.13em;
  cursor: pointer;
  color: var(--light-text-color);
  width: 2.2em;
  height: 2.2em;
  display: flex;
  align-items: center;
  justify-content: center;
}

.add-button:hover {
  color: var(--primary-color);
  background-color: rgba(202, 109, 232, 0.1);
  border-radius: 0.25em;
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


@media (max-width: 700px) {
  .sidebar {
    position: fixed;
    z-index: 1000;
  }
}
</style>
