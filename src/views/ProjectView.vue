<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import SectionCard from "@/components/SectionCard.vue";
import type { Section } from "@/models/Section.ts";
import SectionsHandler from "@/api/SectionsHandler";
import { useRoute } from "vue-router";

const route = useRoute();
const sections = ref<Section[]>([]);
const projectID = route.params.id as string;
const showForm = ref(false);

const newSection = ref({
  sectionName: "",
  sectionPosition: 0,
});

const loadSections = async () => {
  sections.value = await SectionsHandler.fetchSections(parseInt(projectID));
};

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

const removeSection = (sectionID: number) => {
  sections.value = sections.value.filter(section => section.sectionID !== sectionID);
};

onMounted(() => {
  loadSections();
});

const sortedSections = computed(() => {
  return [...sections.value].sort((a, b) => a.sectionPosition - b.sectionPosition);
});

const handleDragStart = (event: DragEvent, sectionID: number) => {
  event.dataTransfer?.setData("sectionID", sectionID.toString());
};

const handleDragOver = (event: DragEvent) => {
  event.preventDefault();
};

const handleDrop = async (event: DragEvent, targetPosition: number) => {
  const draggedSectionID = Number(event.dataTransfer?.getData("sectionID"));
  const draggedSection = sections.value.find(
    (section) => section.sectionID === draggedSectionID
  );

  if (!draggedSection) return;

  const targetSection = sections.value.find(
    (section) => section.sectionPosition === targetPosition
  );

  if (draggedSection && targetSection && draggedSection.sectionID !== targetSection.sectionID) {
    const updatedSections = [...sections.value];

    if (draggedSection.sectionPosition < targetPosition) {
      updatedSections.forEach((section) => {
        if (section.sectionPosition > draggedSection.sectionPosition && section.sectionPosition <= targetPosition) {
          section.sectionPosition -= 1;
        }
      });
    } else {
      updatedSections.forEach((section) => {
        if (section.sectionPosition < draggedSection.sectionPosition && section.sectionPosition >= targetPosition) {
          section.sectionPosition += 1;
        }
      });
    }
    draggedSection.sectionPosition = targetPosition;

    await SectionsHandler.updateSectionPosition(draggedSection.sectionID, draggedSection.sectionPosition);

    sections.value = updatedSections.sort((a, b) => a.sectionPosition - b.sectionPosition);
  }
};
</script>

<template>
  <main>
    <section class="project-container">
      <section class="sections-container" @dragover="handleDragOver">

        <SectionCard
          v-for="section in sortedSections"
          :key="section.sectionID"
          :sectionID="section.sectionID"
          :sectionName="section.sectionName"
          :sectionPosition="section.sectionPosition"
          :createdAt="section.createdAt"
          :updatedAt="section.updatedAt"
          draggable="true"
          @dragstart="(e: DragEvent) => handleDragStart(e, section.sectionID)"
          @drop="(e: DragEvent) => handleDrop(e, section.sectionPosition)"
          @section-updated="updateSection"
          @section-deleted="removeSection"
        />
        <button id="addSectionBtn" v-if="!showForm" @click="showForm = true">
        <pre>+ </pre>Add Section
        </button>

        <form v-if="showForm" @submit.prevent="addSection">
        <input v-model="newSection.sectionName" placeholder="Section Name" required />
        <fieldset>
          <section>
            <button type="submit">Add Section</button>
            <button type="button" @click="showForm = false">Cancel</button>
          </section>
        </fieldset>
      </form>

      </section>

    </section>
 
  </main>
</template>

<style scoped>

main{
  display: flex;
  flex-flow:row;
}

#project-title{
  display: flex;
  align-items: center;
  justify-content: left;
  margin-bottom: 1em;
}

.project-container {
  padding: 1rem;
}


.sections-container {
  display: flex;
  flex-direction: row;
  gap: 1rem;
  overflow-x: auto;
  width: max-content;
  height: 90vh;
}

#addSectionBtn {
  display: flex;
  cursor: pointer;
  width: 15rem;
  color: var(--light-text-color);
  background: #f9f9f9;
  border: 1pt solid #ddd;
  border-radius: 8pt;
  padding: 1rem;
  font-size: 13pt;
  align-items: start;
  text-align: center;
  flex: 0 0 auto;
  justify-content: left;
}

#addSectionBtn pre{
  align-items: start;
  font-size: 13pt;
}

form {
  padding: 0.5rem;
  cursor: pointer;
  border-radius: 5pt;
  color: var(--light-text-color);
  background: #f9f9f9;
  border: 1pt solid #ddd;
  border-radius: 8pt;
  padding: 1rem;
  font-size: 13pt;
  width: 15rem;
}
form input{
  margin-bottom: 1em;
  padding: 3pt;
  font-size: small;
  border-radius: 3pt;
}
form section,fieldset{
  display: flex;
  flex-direction: row;
  border: none;
}

form button{
  font-size: 9pt;
  border-radius: 4pt;
  align-items: center;
  padding: 4pt;
  border: none;
}

button[type="submit"] {
margin-right: 1em;
color: white;
background-color: lightgrey;
}

button[type="submit"]:hover {
  background-color: #969696;
}

button[type="button"] {
  background-color: #f9f9f9;
}

button[type="button"]:hover {
  background-color: #f3f3f3;
}
</style>


