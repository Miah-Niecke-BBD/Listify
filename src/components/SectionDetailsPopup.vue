<script setup lang="ts">
import { ref } from "vue";
import SectionsHandler from "@/api/SectionsHandler";

const props = defineProps<{
  section: {
    sectionID: number;
    sectionName: string;
    sectionPosition: number;
  };
}>();

const emit = defineEmits(["close", "update", "section-deleted"]);
const sectionName = ref(props.section.sectionName);

const updateSection = async () => {
  const updatedSection = { ...props.section, sectionName: sectionName.value };
  const result = await SectionsHandler.updateSection(updatedSection);

  if (result) {
    emit("update", result);
    emit("close");
  }
};

const deleteSection = async () => {
  const success = await SectionsHandler.deleteSection(props.section.sectionID);
  if (success) {
    emit("section-deleted", props.section.sectionID);
    emit("close");
  }
};
</script>

<template>
  <div class="popup-overlay" @click.self="emit('close')">
    <div class="popup-content">
      <h2>Edit Section</h2>
      <label for="section-name">Section Name:</label>
      <input v-model="sectionName" id="section-name" type="text" />

      <div class="form-actions">
        <button @click="updateSection">Save Changes</button>
        <button @click="deleteSection" class="delete-button">Delete Section</button>
        <button @click="emit('close')">Close</button>
      </div>
    </div>
  </div>
</template>

<style scoped>

.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}


.popup-content {
  background: #fff;
  padding: 1.5rem;
  border-radius: 8px;
  width: 300px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
  text-align: center;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: bold;
}

input {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 1rem;
  border-radius: 5px;
  border: 1px solid #ddd;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  opacity: 0.8;
}


.delete-button {
  background-color: red;
  color: white;
}

button:not(.delete-button) {
  background-color: #ca6de8;
  color: white;
}
</style>
