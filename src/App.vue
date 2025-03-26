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
    <router-view />
  </section>
</template>

<style scoped>
.app-layout {
  display: flex;
}
</style>
