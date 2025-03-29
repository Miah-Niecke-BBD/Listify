<script setup lang="ts">
import { ref } from 'vue'
import { useRoute } from "vue-router";
import type { TeamInterface, ProjectInterface } from '@/models/TeamInterface'

import IconDownTriangle from '@/components/icons/IconDownTraingle.vue'
import IconRightTriangle from '@/components/icons/IconRightTraingle.vue'
import IconCalendar from './icons/IconCalendar.vue'


const props = defineProps<{
  teams: TeamInterface[]
}>()

const route = useRoute();

const activeDropdowns = ref<Set<number>>(new Set<number>())

const toggleDropdown = (index: number) => {
  if (activeDropdowns.value.has(index)) {
    activeDropdowns.value.delete(index)
  } else {
    activeDropdowns.value.add(index)
  }
}

const isTeamActive = (path: string) => {
  return route.path.startsWith(path);
}
</script>

<template>
  <ul class="nav-list">
    <li v-for="(team, index) in props.teams" :key="team.teamID" class="nav-item">
      <header class="inline">
        <nav>
        <RouterLink :to="'/team/' + team.teamID" class="nav-link" :class="{ active: isTeamActive(`/team/${team.teamID}`) }">
          {{ team.teamName }}
        </RouterLink>
      </nav>
        <button @click="toggleDropdown(index)" :class="{ active: activeDropdowns.has(index) }">
          <span v-if="!activeDropdowns.has(index)"> <IconRightTriangle /></span>
          <span v-else><IconDownTriangle /></span>
        </button>
      </header>

      <ul v-if="activeDropdowns.has(index)" class="nav-list">
        <li>
          <span>
            <RouterLink :to="'/team/' + team.teamID + '/calendar'" class="nav-link2" id="calendar-txt">
             Team Calendar
            </RouterLink>
          </span>
        </li>
        <li v-if="team.projects && team.projects.length > 0"v-for="(project, projectIndex) in team.projects":key="project.projectID">
          <RouterLink :to="'/projects/' + project.projectID" class="nav-link2">
            <pre># </pre> {{ project.projectName }}
          </RouterLink>
        </li>
      </ul>
    </li>
  </ul>
</template>

<style scoped>
.nav-item {
  margin-bottom: 2pt;
  background-color: var(--nav-bg-color);
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 8pt 16pt;
  font-size: 10pt;
  color: var(--light-text-color);
  transition: background-color 0.2s ease;
  white-space: nowrap;
  text-decoration: none;
}

.nav-link2 {
  display: flex;
  align-items: center;
  padding: 6pt 16pt;
  font-size: 10pt;
  color: var(--light-text-color);
  transition: background-color 0.2s ease;
  white-space: nowrap;
  text-decoration: none;
  margin-left: 1em;
}

.nav-link:hover,
.nav-link2:hover {
  background-color: rgb(241, 241, 241);
}

.nav-link.active {
  background-color: var(--button-hover-bg);
  color: var(--primary-color);
  font-weight: bold;
  border-radius: 5px;
}

.inline {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.inline button {
  display: flex;
  justify-content: flex-end;
  background-color: var(--nav-bg-color);
  border: none;
  margin-right: 1em;
  align-items: flex-end;
  width: 15%;
}


header nav{
  width: 100%;
}

#calendar-txt {
  color: var(--primary-color);
  font-weight: bold;
  width: 100%;
  text-align: center;
}

span {
  display: flex;
  align-items: flex-start;
  flex-direction: row;
}

pre{
  font-weight: bold;
  color: rgb(92, 92, 92);
}
</style>
