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

const emit = defineEmits(["close", "update"]);
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
    emit("close");
  }
};
</script>

<template>
  <section class="popup-overlay" @click.self="emit('close')">
    <article class="popup-content" role="dialog" aria-modal="true" aria-labelledby="edit-section-title">
      <header>
        <h2 id="edit-section-title">Edit Section</h2>
      </header>

      <form @submit.prevent="updateSection">
        <label for="section-name">Section Name:</label>
        <input v-model="sectionName" id="section-name" type="text" />

        <footer class="form-actions">
          <button type="submit">Save Changes</button>
          <button type="button" @click="deleteSection" id="delete-button">Delete Section</button>
          <button type="button" @click="emit('close')" id="close-button">Close</button>
        </footer>
      </form>
    </article>
  </section>
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
  border-radius: 8pt;
  width: 30rem;
  box-shadow: 0 4pt 15pt rgba(0, 0, 0, 0.3);
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
  border-radius: 5pt;
  border: 1pt solid #ddd;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.form-actions button{
  width: 10em;
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


#delete-button {
  background-color: red;
  color: white;
}
#close-button {
  background-color: rgb(163, 163, 163);
  color: white;
}


button:not(.delete-button) {
  background-color: #ca6de8;
  color: white;
}
</style>
