<script setup lang="ts">
import Sidebar from '@/components/Sidebar.vue';

</script>


<template>
  <main>
  <nav>
    <Sidebar />
  </nav>
  </main>

</template>

<style scoped>

main{
  overflow-x: hidden;
    overflow-y: hidden;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: left;
}
</style>