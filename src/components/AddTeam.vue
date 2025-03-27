<script setup lang="ts">
import { ref, defineProps } from "vue";
import {useRouter} from "vue-router";

const router = useRouter()

const props = defineProps<{
  toggleModal: Function;
  isOpen: boolean;
  addTeam: Function;
}>();

const teamName = ref("");
const jwtToken: string | null = localStorage.getItem("jwtToken");

function closeDialog() {
  props.toggleModal();
}

async function saveTeam() {
  if (teamName.value.trim()) {
    try {
      const newTeamID: number = await props.addTeam(jwtToken, teamName.value);
      router.push(`/team/${newTeamID}`);
    } catch (error) {
      throw error;
    } finally {
      props.toggleModal();
    }
  }
}
</script>

<template>
  <dialog v-if="isOpen" class="dialog-overlay" @click.self="closeDialog">
    <article class="dialog">
      <header class="dialog-header">
        <h2 class="dialog-title">Create New Team</h2>
        <p class="dialog-subtitle">
          Add a new team to organize your projects and collaborate with others.
        </p>
      </header>

      <form class="dialog-form" @submit.prevent="saveTeam">
        <fieldset class="form-group">
          <label for="teamName" class="form-label">Team Name</label>
          <input
            v-model="teamName"
            id="teamName"
            maxlength="100"
            type="text"
            placeholder="Enter team name"
            required
            class="form-input"
          />
        </fieldset>

        <footer class="dialog-footer">
          <button type="button" @click="closeDialog" class="cancel-btn">Cancel</button>
          <button type="submit" class="save-btn">Create Team</button>
        </footer>
      </form>
    </article>
  </dialog>
</template>

<style scoped>
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

.dialog {
  background-color: var(--card-bg);
  border-radius: 0.75rem;
  width: 100%;
  max-width: 31.25rem;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: var(--shadow-lg);
  animation: slideIn 0.3s ease;
}

.dialog-header {
  padding: 1.5rem 1.5rem 0;
  margin-bottom: 1rem;
}

.dialog-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: var(--text-primary);
}

.dialog-subtitle {
  color: var(--text-secondary);
  font-size: 0.875rem;
}

.dialog-form {
  padding: 0 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-label {
  display: block;
  font-size: 0.875rem;
  font-weight: 500;
  margin-bottom: 0.5rem;
  color: var(--text-primary);
}

.form-input {
  width: 100%;
  padding: 0.625rem 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 0.375rem;
  font-size: 0.875rem;
  background-color: var(--card-bg);
  transition: border-color 0.2s;
}

.form-input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 0.125rem rgba(139, 83, 255, 0.1);
}

.color-picker {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.color-input {
  width: 2.5rem;
  height: 2.5rem;
  border: none;
  border-radius: 0.25rem;
  cursor: pointer;
  background: none;
}

.color-preview {
  width: 1.5rem;
  height: 1.5rem;
  border-radius: 50%;
  border: 1px solid var(--border-color);
}

.color-value {
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  padding: 1rem 1.5rem;
  border-top: 1px solid var(--border-color);
  margin-top: 1.5rem;
  gap: 0.75rem;
}

.cancel-btn {
  padding: 0.625rem 1rem;
  background-color: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--text-primary);
  transition: background-color 0.2s ease;
}

.cancel-btn:hover {
  background-color: var(--hover-color);
}

.save-btn {
  padding: 0.625rem 1rem;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  transition: background-color 0.2s ease;
}

.save-btn:hover {
  background-color: var(--primary-dark);
}

button {
  cursor: pointer;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    transform: translateY(1.25rem);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@media (max-width: 768px) {
  .dialog {
    width: 90%;
  }
}
</style>
