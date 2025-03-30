<script setup lang="ts">
import { ref } from "vue";

const props = defineProps({
  isVisible: {
    type: Boolean,
    required: true,
  },
  onClose: {
    type: Function,
    required: true,
  },
  onUpdateTeamName: {
    type: Function,
    required: true,
  },
});

const newTeamName = ref<string>("");
const errorMessage = ref<string | null>(null);

const closeModal = () => {
  newTeamName.value = "";
  errorMessage.value = null;
  props.onClose();
};

const handleUpdateTeamName = async () => {
  if (props.onUpdateTeamName) {
    try {
      await props.onUpdateTeamName(newTeamName.value);
      closeModal();
    } catch (error: any) {
      errorMessage.value = error.message;
    }
  }
};
</script>

<template>
  <section v-if="isVisible" class="modal">
    <article class="modal-content">
      <header class="modal-header">
        <h3>Update Team Name</h3>
      </header>

      <input
        v-model="newTeamName"
        type="text"
        placeholder="New Team Name"
        maxlength="100"
        class="modal-input"
        required
      />
      <p class="error-message">{{ errorMessage }}</p>
      <footer class="modal-footer">
        <button @click="handleUpdateTeamName" class="modal-btn" type="submit">Update Name</button>
        <button @click="closeModal" class="modal-btn">Cancel</button>
      </footer>
    </article>
  </section>
</template>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 2em;
  border-radius: 1em;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 30em;
}

.modal-header {
  margin-bottom: 1em;
}

.modal-input {
  padding: 0.5em;
  margin-bottom: 1em;
  width: 80%;
  font-size: 1em;
  border: 0.1em solid lightgray;
  border-radius: 0.5em;
}

.modal-btn {
  padding: 0.5em 1em;
  margin: 0.5em;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 0.4em;
  cursor: pointer;
}

.modal-btn:hover {
  opacity: 0.8;
}

.error-message {
  color: red;
  margin-bottom: 0.5em;
}

@media (max-width: 700px) {
  .modal-content {
    min-width: unset;
    margin: 1em;
  }
}
</style>
