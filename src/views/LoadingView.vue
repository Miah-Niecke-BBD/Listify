<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { GetJwtToken } from "@/api/OAuth2";
import type { LoginResponse } from "@/models/LoginResponse";
import { GetUser, CreateUser } from "@/api/User";
import type { User } from "@/models/User";
const loading = ref(false);
const router = useRouter();
const newUser = ref(false);

onMounted(async () => {
  const code: string | null = new URLSearchParams(window.location.search).get("code");
  if (code) {
    loading.value = true;
    try {
      const response: LoginResponse | null = await GetJwtToken(code);
      if (response) {
        localStorage.setItem("jwtToken", response.jwtToken);

        const { user, error } = await GetUser(response.jwtToken);
        if (user) {
          console.log(user)
          localStorage.setItem("loggedInUser", (user as User).githubID);
        }

        if (error) {
          newUser.value = true;
          const { user, error } = await CreateUser(response.jwtToken);
          console.log("User:", user);
          router.push("/tutorial");
        } else if (user) {
          newUser.value = false;
          console.log("User:", user);
          router.push("/tutorial");
        }
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
    <h1>Loading...</h1>
  </section>
</template>

<style scoped>
.loading-overlay {
  display: flex;
  flex-direction: column;
  text-align: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 0;
}

h1 {
  justify-content: center;
  text-align: center;
}

.loading-spinner {
  border: 10px solid #f3f3f3;
  border-top: 10px solid #8b53ff;
  border-radius: 50%;
  justify-content: center;
  text-align: center;
  width: 100px;
  height: 100px;
  animation: spin 1.5s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
