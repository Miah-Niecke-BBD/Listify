<script setup lang="ts">
import { ref, onMounted } from "vue"
import SectionCard from "@/components/SectionCard.vue";
import type { Section } from "@/models/Section.ts";
import SectionsHandler from "@/api/SectionsHandler.ts";
import {useRoute} from "vue-router";

const route = useRoute()
const sections = ref<Section[]>([])
const projectID = route.params.id as string;
const showForm = ref(false);

const newSection = ref({
    sectionName: "",
    sectionPosition: 0,
})

const loadSections = async () => {
  sections.value = await SectionsHandler.fetchSections(parseInt(projectID))
}

const updateSection = async (updatedSection: Section) => {
  const result = await SectionsHandler.updateSection(updatedSection);
  if (result) {
    const index = sections.value.findIndex(
      (section) => section.sectionID === updatedSection.sectionID
    );
    if (index !== -1) sections.value[index] = result;
  }
};

const addSection = async () => {
  const createdSection = await SectionsHandler.createSection(parseInt(projectID), {
    sectionName: newSection.value.sectionName,
    sectionPosition: newSection.value.sectionPosition,
  });

  if (createdSection) {
    sections.value.push(createdSection);
    newSection.value.sectionName = "";
    newSection.value.sectionPosition = 0;
    showForm.value = false;
  }
};

onMounted(() => {
    loadSections()
})
</script>

<template>
  <main>
<div class="project-container">
    <h1 class="project-title">Project Name</h1>

  <button v-if="!showForm" @click="showForm = true">Add Section</button>

  <form v-if="showForm" @submit.prevent="addSection">
    <input v-model="newSection.sectionName" placeholder="Section Name" required />
    <section>
      <label for="section-position">Section Position</label>
      <input v-model="newSection.sectionPosition" type="number" min="0" required />
    </section>

    <section>
      <button type="submit">Create</button>
      <button type="button" @click="showForm = false">Cancel</button>
    </section>
  </form>

    <div class="sections-container">
      <SectionCard
        v-for="section in sections"
        :key="section.sectionID"
        :sectionID="section.sectionID"
        :sectionName="section.sectionName"
        :sectionPosition="section.sectionPosition"
        :createdAt="section.createdAt"
        :updatedAt="section.updatedAt"
        @section-updated="updateSection"
      />
    </div>
  </div>
</main>
</template>

<style scoped>
* {
  font-family: ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-seri;
}

main{
  display: flex;
  flex-direction: row;
}

.project-container {
  padding: 1rem;
}

.sections-container {
  display: flex;
  flex-direction: row;
  gap: 1rem;
}

form {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-top: 1rem;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #f9f9f9;
  max-width: 300px;
}

button {
  padding: 0.5rem;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  font-weight: bold;
  color: #fff;
  background-color: #ca6de8;
}

button[type="submit"] {
  background-color: #4caf50;
}

button[type="button"] {
  background-color: #ca6de8;
}
</style>
