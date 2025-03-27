<script setup lang="ts">
import { onMounted, ref, computed } from "vue";
import TaskCard from "@/components/TaskCard.vue";
import TaskDetailsPopup from "@/components/TaskDetailsPopup.vue";
import SectionDetailsPopup from "@/components/SectionDetailsPopup.vue";
import {updateTaskPosition, useTasks} from "@/api/TasksHandler";
import {jwtToken} from "@/models/JWTToken.ts";
import IconEllipse from "./icons/IconEllipse.vue";

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
  <article class="section-card">
    <header>
        <h2 class="section-title">{{ section.sectionName }}</h2>
        <button class="expand-button" @click="toggleSectionPopup" aria-label="More options">
          <IconEllipse /><IconEllipse /><IconEllipse />
        </button>
    </header>

  
    <ul class="tasks-container" aria-label="Sorted Tasks List">
      <li>
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
      </li>

      <form v-if="showForm" @submit.prevent="handleAddTask" class="add-task-form" aria-label="Add Task Form">
      <input v-model="newTask.taskName" placeholder="Task Name" required />
      <textarea v-model="newTask.taskDescription" placeholder="Task Description"></textarea>
      <label>
        Priority:
        <select v-model="newTask.taskPriority">
          <option :value="null">None</option>
          <option value="1">Low</option>
          <option value="2">Medium</option>
          <option value="3">High</option>
        </select>
      </label>

      <label>
        Due Date:
        <input type="date" v-model="newTask.dueDate" />
      </label>

      <footer class="form-actions">
        <button type="submit">Create</button>
        <button type="button" @click="cancelTask">Cancel</button>
      </footer>
    </form>


      <li>
        <button  v-if="!showForm" @click="showForm = true" id="AddBtn" aria-label="Add Task" > <pre>+ </pre> Add Task </button> 
      </li>
    </ul>

    <SectionDetailsPopup
      v-if="isSectionPopupVisible"
      :section="props"
      @close="toggleSectionPopup"
      @update="updateSectionHandler"
    />

    <TaskDetailsPopup
      v-if="selectedTask"
      :task="selectedTask"
      @close="selectedTask = null"
    />
  </article>
</template>


<style scoped>

.add-task-form {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-top: 0.5rem;
  margin: .8em;
}

.add-task-form textarea,
.add-task-form input,
.add-task-form select {
  padding: 0.5rem;
  border: 1pt solid #ddd;
  border-radius: 5pt;
}


.form-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: space-evenly;
}
.form-actions button{
  border: solid 1pt var(--light-text-color);
  padding: 2pt 10pt;
  border-radius: 2pt;
 
}

.form-actions button:hover{
 background-color: #ddd;
}


.tasks-container {
  display: flex;
  flex-direction: column;
  gap: 1pt;
  margin-top: 1rem;
  width: 15rem;
  flex-grow: 1; 
  overflow-y: auto;             
}

.section-card {
  border: 1pt solid #ddd;
  border-radius: 8pt; 
  padding: 8pt;
  background: #f9f9f9;
  display: flex;
  flex-direction: column;
  height: 90vh; 
}

#taskCard{
  margin: .2em;
}

.section-title {
  width: 100%;
  font-size: 12pt;
  font-weight: bold;
  margin-bottom: 0.5rem;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: left;  
}

header{
  display: flex;
  flex-flow: row;
  justify-content: space-between;
  text-align: center;
  width: 100%;
}

 header section{
  display: flex;
  flex-flow: row;
  justify-content: space-between;
  text-align: flex-start;
}

#AddBtn {
  align-self: flex-start;
  margin-top: auto; 
  display: flex;
  font-size: 12pt;
  color: var(--light-text-color);
  flex-direction: row;
  border: none;
  justify-content: start;
  align-items: center;
  background-color: #f9f9f9;
}

#AddBtn pre {
  align-items: start;
  font-size: 20pt;
}

.expand-button {
  display: flex;
  cursor: pointer;
  border: none;
  background-color: #f9f9f9;
  text-align: start;
}
</style>
