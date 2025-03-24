<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { GetJwtToken } from '@/api/OAuth2Api';
import type { LoginResponse } from '@/models/LoginResponse';

const loading = ref(false);
const router = useRouter();

onMounted(async () => {
  const code:string|null = new URLSearchParams(window.location.search).get('code');
  if (code) {
    loading.value = true;
    try {
      const response:LoginResponse|null  = await GetJwtToken(code);
      if (response) {
        localStorage.setItem('jwtToken',response.jwtToken );
        router.push('/home');
      }
    } catch (error) {
      console.error("Error during login:", error);
    } finally {
      loading.value = false;
    }
  }
});
</script>

<template>
  <section v-if="loading" class="loading-overlay">
    <section class="loading-spinner"></section>
  </section>
</template>

<style scoped>
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5); 
  z-index: 9999;
}

.loading-spinner {
  border: 10px solid #f3f3f3;
  border-top: 10px solid #3498db;
  border-radius: 50%;
  width: 100px;
  height: 100px;
  animation: spin 1.5s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
