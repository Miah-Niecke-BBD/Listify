<script setup lang="ts">
import type { AuthCodeResponse } from '@/models/AuthCodeResponse.ts';
import { GetAuthCode } from '../api/OAuth2Api.ts';
import IconGoogle from '@/components/icons/IconGoogle.vue';

async function handleLogin(): Promise<void> {
  try {
    const result:any = await GetAuthCode();

   
    if (result && result.authorization_url) {
      const data: AuthCodeResponse = result; 
      window.location.href = data.authorization_url; 
    } else {
      console.log("No authorization URL received.");
    }
  } catch (error) {
    console.error("Error fetching data:", error);
  }
}
</script>

<template>
  <section id="body">
    <header>
      <img src="../assets/logo.png" alt="Logo" />
    </header>
    <main>
      <section>
        <h1>Welcome to Listify</h1>
        <p>Sign in to access your lists</p>
        <button @click="handleLogin"><IconGoogle/>Continue with Google</button>
        <p>By continuing, you agree to Listify's Terms and Privacy Policy</p>
      </section>
    </main>
    <footer>
      <p>@ 2025 Listify. All rights reserved.</p>
    </footer>
  </section>
</template>

<style scoped>
#body {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

header {
  margin-top: 1rem;
}

main {
  border: 1px solid rgb(240, 239, 239);
  padding: 1em 2em;
  margin-top: 6rem;
}

h1 {
  color: black;
  font-size: 32pt;
  font-weight: 700;
  margin: 0;
}

main p {
  color: grey;
  font-weight: 500;
  font-size: 8pt;
}

button {
  background-color: white;
  border: 1px solid lightgrey;
  border-radius: 0.5em;
  padding: 0.4em;
  width: 80%;
  color: black;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.8em;
  cursor: pointer;
}

button:hover {
  background-color: rgb(237, 238, 239);
}

footer {
  position: fixed;
  bottom: 0;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

@media (max-width: 480px) {
  h1 {
    font-size: 24pt;
  }
}
</style>
