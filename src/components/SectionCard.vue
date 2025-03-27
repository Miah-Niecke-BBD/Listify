<script setup lang="ts">
import { onMounted, ref, computed } from "vue";
import TaskCard from "@/components/TaskCard.vue";
import TaskDetailsPopup from "@/components/TaskDetailsPopup.vue";
import SectionDetailsPopup from "@/components/SectionDetailsPopup.vue";
import {updateTaskPosition, useTasks} from "@/api/TasksHandler.ts";
import {jwtToken} from "@/models/JWTToken.ts";

const props = defineProps<{
  sectionID: number;
  sectionName: string;
  sectionPosition: number;
  createdAt: Date;
  updatedAt: Date | null;
}>();

const emits = defineEmits(["section-updated"]);

const isSectionPopupVisible = ref(false);
const section = ref({
  sectionID: props.sectionID,
  sectionName: props.sectionName,
  sectionPosition: props.sectionPosition,
});

const {
  sectionTask,
  selectedTask,
  showForm,
  newTask,
  addTask,
  projectID,
  getTasksForSection,
  cancelTask,
  openTaskPopup,
} = useTasks(props.sectionID);

onMounted(() => {
  getTasksForSection();
});

const sortedTasks = computed(() => {
  return [...sectionTask.value].sort((a, b) => a.taskPosition - b.taskPosition);
});

const toggleSectionPopup = () => {
  isSectionPopupVisible.value = !isSectionPopupVisible.value;
};

const updateSectionHandler = (updatedSection: any) => {
  section.value = updatedSection;
  emits("section-updated", updatedSection);
};

const handleAddTask = async () => {
  if (!newTask.value.taskName) return;
  try {
    await addTask({
      ...newTask.value,
      sectionID: props.sectionID,
      projectID: projectID.value,
    });
    getTasksForSection();
    showForm.value = false;
  } catch (error) {
    console.log("Failed to add task:", error);
  }
};

const handleDragEnd = async (event: DragEvent, targetTaskID: number) => {
  const draggedTaskID = Number(event.dataTransfer?.getData("taskID"));
  if (draggedTaskID === targetTaskID) return;

  const draggedTask = sectionTask.value.find(task => task.taskID === draggedTaskID);
  const targetTask = sectionTask.value.find(task => task.taskID === targetTaskID);

  if (draggedTask && targetTask) {
    const oldPosition = draggedTask.taskPosition;
    const newPosition = targetTask.taskPosition;

    sectionTask.value.forEach(task => {
      if (oldPosition < newPosition && task.taskPosition > oldPosition && task.taskPosition <= newPosition) {
        task.taskPosition -= 1;
      } else if (oldPosition > newPosition && task.taskPosition < oldPosition && task.taskPosition >= newPosition) {
        task.taskPosition += 1;
      }
    });

    draggedTask.taskPosition = newPosition;

    await updateTaskPosition(draggedTaskID, newPosition, props.sectionID, jwtToken);
    getTasksForSection();
  }
};


</script>

<template>
  <section class="section-card">
    <h2 class="section-title">{{ section.sectionName }}</h2>

    <button class="expand-button" aria-label="Expand Section details" @click="toggleSectionPopup">
      ▼
    </button>

    <button v-if="!showForm" @click="showForm = true">Add Task</button>
    <form v-if="showForm" @submit.prevent="handleAddTask" class="add-task-form">
      <input v-model="newTask.taskName" placeholder="Task Name" required />
      <textarea v-model="newTask.taskDescription" placeholder="Task Description"></textarea>

      <select v-model="newTask.taskPriority">
        <option :value="null">None</option>
        <option value="1">Low</option>
        <option value="2">Medium</option>
        <option value="3">High</option>
      </select>

      <input type="date" v-model="newTask.dueDate" />

      <div class="form-actions">
        <button type="submit">Create</button>
        <button type="button" @click="cancelTask">Cancel</button>
      </div>
    </form>

    <section class="tasks-container">
      <TaskCard
        v-for="task in sortedTasks"
        :key="task.taskID"
        :taskID="task.taskID"
        :taskName="task.taskName"
        :parentTaskID="task.parentTaskID"
        :taskPosition="task.taskPosition"
        :dueDate="task.dueDate"
        :createdAt="task.createdAt"
        draggable="true"
        @dragstart="(e: DragEvent) => e.dataTransfer?.setData('taskID', task.taskID.toString())"
        @dragover.prevent
        @drop="(e: DragEvent) => handleDragEnd(e, task.taskID)"
        @task-clicked="openTaskPopup"
      />

      <TaskDetailsPopup v-if="selectedTask" :task="selectedTask" @close="selectedTask = null" />
    </section>

    <SectionDetailsPopup
      v-if="isSectionPopupVisible"
      :section="props"
      @close="toggleSectionPopup"
      @update="updateSectionHandler"
    />
  </section>
</template>

<style scoped>
* {
  font-family: ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial,
  sans-serif;
}

/* Form Styling */
.add-task-form {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.add-task-form textarea,
.add-task-form input,
.add-task-form select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 5px;
}

/* Button row styling */
.form-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

/* Section card styling */
.section-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1rem;
  background: #f9f9f9;
  width: 300px;
}

.section-title {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.tasks-container {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-top: 1rem;
}

/* Expand button */
.expand-button {
  background-color: #ca6de8;
  border: none;
  color: #fff;
  padding: 0.4rem;
  cursor: pointer;
  border-radius: 5px;
  font-size: 0.9rem;
}
</style>
