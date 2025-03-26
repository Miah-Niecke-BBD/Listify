<script setup lang="ts">
import { ref, defineProps, defineEmits } from 'vue'

interface Team {
  id: string;
  name: string;
  color: string;
}


const { teams } = defineProps<{
  teams: Team[];
}>()

const emit = defineEmits(['open-team-dialog'])

function openTeamDialog() {
  emit('open-team-dialog')
}
const expandedSections = ref<Record<string, boolean>>({
  myLists: true,
  calendar: true,
  projectTeams: true
})

function toggleSection(section: string) {
  if (section in expandedSections.value) {
    expandedSections.value[section] = !expandedSections.value[section]
  }
}
</script>

<template>
  <aside class="sidebar">
    <header class="sidebar-header">
      <h1 class="sidebar-logo">LISTIFY</h1>
    </header>

    <nav class="sidebar-nav">
      <section class="nav-section">
        <div class="section-header" @click="toggleSection('myLists')">
          <h2 class="nav-section-title">MY LISTS</h2>
          <span class="section-toggle">{{ expandedSections.myLists ? '−' : '+' }}</span>
        </div>
        <ul v-if="expandedSections.myLists" class="nav-list">
          <li class="nav-item">
            <router-link to="/" class="nav-link">
              <span class="nav-icon list-icon">≡</span>
              <span>All Tasks</span>
            </router-link>
          </li>
        </ul>
      </section>

      <section class="nav-section">
        <div class="section-header" @click="toggleSection('calendar')">
          <h2 class="nav-section-title">CALENDAR</h2>
          <span class="section-toggle">{{ expandedSections.calendar ? '−' : '+' }}</span>
        </div>
        <ul v-if="expandedSections.calendar" class="nav-list">
          <li class="nav-item">
            <a href="#" class="nav-link">
              <span class="nav-icon calendar-icon">□</span>
              <span>Personal Calendar</span>
            </a>
          </li>
        </ul>
      </section>

      <section class="nav-section">
        <div class="section-header" @click="toggleSection('projectTeams')">
          <h2 class="nav-section-title">MY PROJECT TEAMS</h2>
          <span class="section-toggle">{{ expandedSections.projectTeams ? '−' : '+' }}</span>
        </div>
        <div v-if="expandedSections.projectTeams" class="project-teams-container">
          <ul class="nav-list">
            <li v-for="team in teams" :key="team.id" class="nav-item">
              <a href="#" class="nav-link">
                <span class="team-color" :style="{ backgroundColor: team.color }"></span>
                <span>{{ team.name }}</span>
              </a>
            </li>
          </ul>
          <button @click="openTeamDialog" class="create-team-btn">
            <span class="plus-icon">+</span>
            <span>Create Team</span>
          </button>
        </div>
      </section>
    </nav>

    <footer class="sidebar-footer">
      <router-link to="/" class="nav-link active">
        <span class="nav-icon home-icon">⌂</span>
        <span>Home</span>
      </router-link>
    </footer>
  </aside>
</template>

<style scoped>
.sidebar {
  width: 280px;
  height: 100vh;
  background-color: var(--bg-sidebar);
  border-right: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  justify-content: center;
}

.sidebar-logo {
  font-size: 24px;
  font-weight: 700;
  color: var(--primary-color);
  margin: 0;
  letter-spacing: 1px;
}

.sidebar-nav {
  flex: 1;
  padding: 16px 0;
  overflow-y: auto;
}

.nav-section {
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  margin-bottom: 8px;
  cursor: pointer;
}

.section-toggle {
  font-size: 16px;
  color: var(--text-secondary);
  font-weight: bold;
}

.nav-section-title {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-secondary);
  letter-spacing: 0.5px;
}

.nav-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-item {
  margin-bottom: 2px;
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  color: var(--text-primary);
  text-decoration: none;
  font-size: 14px;
  transition: background-color 0.2s;
  border-radius: 4px;
  margin: 0 8px;
}

.nav-link:hover {
  background-color: var(--hover-color);
}

.nav-link.active {
  background-color: var(--hover-color);
  color: var(--primary-color);
  font-weight: 500;
}

.nav-icon {
  margin-right: 12px;
  font-size: 16px;
  width: 20px;
  text-align: center;
  color: var(--primary-color);
}

.calendar-icon {
  color: var(--primary-dark);
}

.team-color {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  margin-right: 12px;
  display: inline-block;
}

.home-icon {
  color: var(--primary-color);
}

.project-teams-container {
  display: flex;
  flex-direction: column;
}

.create-team-btn {
  display: flex;
  align-items: center;
  margin: 8px 20px;
  padding: 8px 12px;
  background-color: transparent;
  border: 1px dashed var(--border-color);
  border-radius: 4px;
  color: var(--text-secondary);
  font-size: 13px;
  transition: all 0.2s;
  cursor: pointer;
}

.create-team-btn:hover {
  background-color: var(--hover-color);
  color: var(--primary-color);
  border-color: var(--primary-light);
}

.plus-icon {
  margin-right: 8px;
  font-weight: bold;
}

.sidebar-footer {
  padding: 16px 8px;
  border-top: 1px solid var(--border-color);
}

@media (max-width: 768px) {
  .sidebar {
    width: 100%;
    height: auto;
    max-height: 300px;
  }
}
</style>
