
<template>
  <aside class="sidebar">
    <header class="sidebar-header">
      <img :src="logo" alt="Listify Logo" class="logo-image" />
    </header>
    <nav class="sidebar-nav">
      <section class="nav-section">
        <h2 class="nav-section-title">MY LISTS</h2>
        <ul class="nav-list">
          <li class="nav-item">
            <router-link to="/" class="nav-link">
              <span class="nav-icon list-icon">≡</span>
              All Tasks
            </router-link>
          </li>
        </ul>
      </section>
      <section class="nav-section">
        <h2 class="nav-section-title">CALENDAR</h2>
        <ul class="nav-list">
          <li class="nav-item">
            <a href="#" class="nav-link">
              <span class="nav-icon calendar-icon">□</span>
              Personal Calendar
            </a>
          </li>
        </ul>
      </section>
      <section class="nav-section">
        <h2 class="nav-section-title">
          MY PROJECT TEAMS
          <button class="add-button" @click="$emit('open-project-dialog')">+</button>
        </h2>
        <ul class="nav-list">
          <li class="nav-item">
            <a href="#" class="nav-link collapsible">
              <span class="collapse-icon">▼</span>
              My Project Team
            </a>
          </li>
        </ul>
      </section>
    </nav>
    <footer class="sidebar-footer">
      <router-link to="/" class="nav-link active">
        <span class="nav-icon home-icon">⌂</span>
        Home
      </router-link>
    </footer>
  </aside>
</template>

<script>
import logo from '@/assets/logo-removebg-preview.png'
export default {
  name: 'Sidebar',
  emits: ['open-project-dialog'],
  setup() {
    return { logo }
  }
}
</script>

<style scoped>
.logo-image { width: 120px; height: 40px; vertical-align: middle; margin-right: 8px; }
.sidebar { width: 250px; background-color: white; border-right: 1px solid #e5e7eb; display: flex; flex-direction: column; height: 100vh; margin-right: 350px; }
.sidebar-header { padding: 16px; border-bottom: 1px solid #e5e7eb; }
.sidebar-nav { flex: 1; padding: 16px 0; overflow-y: auto; }
.nav-section { margin-bottom: 24px; }
.nav-section-title { padding: 0 16px; margin-bottom: 8px; font-size: 12px; font-weight: 600; color: var(--light-text-color); text-transform: uppercase; letter-spacing: 0.5px; display: flex; justify-content: space-between; align-items: center; }
.add-button { width: 20px; height: 20px; display: flex; align-items: center; justify-content: center; font-size: 16px; color: var(--light-text-color); cursor: pointer; }
.nav-list { list-style: none; }
.nav-item { margin-bottom: 2px; }
.nav-link { display: flex; align-items: center; padding: 8px 16px; font-size: 14px; color: var(--light-text-color); transition: background-color 0.2s ease; }
.nav-link:hover { background-color: var(--button-hover-bg); }
.nav-link.active { background-color: #f0f0f5; color: var(--primary-color); font-weight: 500; }
.nav-icon { width: 20px; height: 20px; display: inline-flex; align-items: center; justify-content: center; margin-right: 8px; font-size: 16px; }
.list-icon { color: var(--primary-color); }
.calendar-icon { color: var(--dark-purple); }
.home-icon { color: var(--primary-color); }
.collapsible { font-weight: 500; }
.collapse-icon { font-size: 10px; margin-right: 8px; }
.sidebar-footer { padding: 16px 0; border-top: 1px solid #f0f0f5; margin-top: auto; }
@media (max-width: 768px) { .sidebar { width: 100%; height: auto; max-height: 300px; } }
</style>