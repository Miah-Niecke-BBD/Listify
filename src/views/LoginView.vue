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
      <img id="logo" src="../assets/logo.png" alt="Logo" />
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
  border: 1px solid var(--light-purple);
  border-radius: 0.5em;
  padding: 1em 2em;
  margin-top: 6rem;

}


h1 {
  color: var(--heading-purple);
  font-size: 32pt;
  font-weight: 700;
  margin-bottom: 0.1em;
}

main p {
  color: var(--light-text-color);
  font-weight: 500;
  font-size: 8pt;
  margin-bottom: 1em;
}

button {
  border: 1px solid var(--light-purple);
  border-radius: 0.5em;
  padding: 0.4em;
  width: 100%;
  color: var(--light-text-color);
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.8em;
  cursor: pointer;
  margin-bottom: 1em;
}

button:hover {
  background-color: var(--button-hover-bg);
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
  #body{
    margin: 0.5rem;
  }
  
  h1{
    font-size: 23pt;
  }

  #logo{
    width: 100%;
  }
}
</style>
