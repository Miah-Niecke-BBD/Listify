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
  addProject: {
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

const createProject = () => {
  props.addProject(newProjectName.value, description.value);
  closeModal();
};
</script>

<template>
  <section v-if="isVisible" class="project-modal">
    <form class="modal-content">
      <label class="modal-header">Please enter project name to add a new project</label>
      <input
        v-model="newProjectName"
        maxlength="100"
        type="text"
        placeholder="Project Name"
        class="modal-input"
        required
      />
      <label class="modal-header">Add a description if you wish to the new project</label>
      <textarea
        v-model="description"
        maxlength="500"
        rows="5"
        cols="40"
        class="modal-input"
        placeholder="Enter description"
      ></textarea>
      <footer class="modal-footer">
        <button for="add-project" @click="createProject" class="modal-btn">Add Project</button>
        <button @click="closeModal" class="modal-btn">Cancel</button>
      </footer>
    </form>
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

.modal-content label {
  font-size: 13pt;
  font-weight: bold;
  color: var(--heading-purple);
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
