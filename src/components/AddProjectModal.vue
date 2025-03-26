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
});
const newProjectName = ref<string>("");
const description = ref<string>("");

const closeModal = () => {
  newProjectName.value = "";
  description.value = "";
  props.onClose();
};
const handleAction = () => {};
</script>

<template>
  <section v-if="isVisible" class="project-modal">
    <article class="modal-content">
      <h3 class="modal-header">Please enter project name to add a new project</h3>
      <input
        v-model="newProjectName"
        type="text"
        placeholder="Project Name"
        class="modal-input"
        required
      />
      <h3 class="modal-header">Add a description if you wish to the new project</h3>
      <textarea
        v-model="description"
        maxlength="500"
        rows="5"
        cols="40"
        class="modal-input"
        placeholder="Enter description"
      ></textarea>
      <footer class="modal-footer">
        <button @click="handleAction" class="modal-btn">Add Project</button>
        <button @click="closeModal" class="modal-btn">Cancel</button>
      </footer>
    </article>
  </section>
</template>

<style>
.project-modal {
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
  text-align: center;
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

textarea.modal-input {
  resize: none;
}

@media (max-width: 700px) {
  .modal-content {
    min-width: unset;
    margin: 1em;
  }
}
</style>
