<script setup lang="ts">
import { useRoute } from "vue-router";
import Sidebar from "./components/Sidebar.vue";
import { computed } from "vue";

const route = useRoute();
const isLoginPage = computed(() => route.name === "login");
const isLoading = computed(() => route.name === "loading");
</script>

<template>
  <section class="app-layout">
    <Sidebar v-if="!isLoginPage && !isLoading" />
    <router-view class="main-content" />
  </section>
</template>

<style scoped>
.app-layout {
  display: flex;
}
.main-content {
  overflow-x: auto;
}

@media (max-width: 700px) {
  .main-content {
    padding-left: 2em;
  }
}
</style>
