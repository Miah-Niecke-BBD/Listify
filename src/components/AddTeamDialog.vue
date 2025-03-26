<script setup lang="ts">
import { ref, defineProps, defineEmits } from 'vue';

// Use destructuring to avoid the unused variable warning
const { isOpen } = defineProps<{ isOpen: boolean }>();
const emit = defineEmits(['close', 'save-team']);

const teamName = ref('');
const teamColor = ref('#8b53ff');

function closeDialog() {
  emit('close');
}

function saveTeam() {
  if (teamName.value.trim()) {
    emit('save-team', {
      name: teamName.value,
      color: teamColor.value
    });
    teamName.value = '';
    teamColor.value = '#8b53ff';
    emit('close');
  }
}
</script>

<template>
  <div v-if="isOpen" class="dialog-overlay">
    <div class="dialog">
      <header class="dialog-header">
        <h2 class="dialog-title">Create New Team</h2>
        <p class="dialog-subtitle">Add a new team to organize your projects and collaborate with others.</p>
      </header>

      <form class="dialog-form" @submit.prevent="saveTeam">
        <div class="form-group">
          <label for="teamName" class="form-label">Team Name</label>
          <input
            v-model="teamName"
            id="teamName"
            type="text"
            placeholder="Enter team name"
            required
            class="form-input"
          />
        </div>

        <div class="form-group">
          <label for="teamColor" class="form-label">Team Color</label>
          <div class="color-picker">
            <input
              v-model="teamColor"
              id="teamColor"
              type="color"
              class="color-input"
            />
            <span class="color-preview" :style="{ backgroundColor: teamColor }"></span>
            <span class="color-value">{{ teamColor }}</span>
          </div>
        </div>

        <footer class="dialog-footer">
          <button type="button" @click="closeDialog" class="cancel-btn">Cancel</button>
          <button type="submit" class="save-btn">Create Team</button>
        </footer>
      </form>
    </div>
  </div>
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
  border-radius: 12px;
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: var(--shadow-lg);
  animation: slideIn 0.3s ease;
}

.dialog-header {
  padding: 24px 24px 0;
  margin-bottom: 16px;
}

.dialog-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--text-primary);
}

.dialog-subtitle {
  color: var(--text-secondary);
  font-size: 14px;
}

.dialog-form {
  padding: 0 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 8px;
  color: var(--text-primary);
}

.form-input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  font-size: 14px;
  background-color: var(--card-bg);
  transition: border-color 0.2s;
}

.form-input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(139, 83, 255, 0.1);
}

.color-picker {
  display: flex;
  align-items: center;
  gap: 12px;
}

.color-input {
  width: 40px;
  height: 40px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background: none;
}

.color-preview {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  border: 1px solid var(--border-color);
}

.color-value {
  font-size: 14px;
  color: var(--text-secondary);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  padding: 16px 24px;
  border-top: 1px solid var(--border-color);
  margin-top: 24px;
  gap: 12px;
}

.cancel-btn {
  padding: 10px 16px;
  background-color: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
  transition: background-color 0.2s ease;
}

.cancel-btn:hover {
  background-color: var(--hover-color);
}

.save-btn {
  padding: 10px 16px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.2s ease;
}

.save-btn:hover {
  background-color: var(--primary-dark);
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideIn {
  from { transform: translateY(20px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

@media (max-width: 768px) {
  .dialog {
    width: 90%;
  }
}
</style>
