<script setup lang="ts">
import { ref } from "vue";
import Calendar from "@/components/Calendar.vue";
import Sidebar from "@/components/Sidebar.vue";
import type { TeamInterface } from "@/models/TeamInterface";

const AllTeamsData = ref<TeamInterface[]>([]);

const GetAllTeams = (data: TeamInterface[]) => {
  AllTeamsData.value = data;
};
</script>
<template>
  <main>
    <!-- <nav>
            <Sidebar @select="GetAllTeams" />
        </nav> -->
    <Calendar :teams="AllTeamsData" />
  </main>
</template>

<style scoped>
main {
  overflow-y: hidden;
  display: flex;
  justify-content: left;
  width: 100%;
}
</style>
