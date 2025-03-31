<script setup lang="ts">
import { ref, watch,computed } from "vue";
import { jwtToken } from "@/models/JWTToken.ts";
import TaskDependencyManager from "@/components/TaskDependencyManager.vue";
import TaskAssignmentManager from "@/components/TaskAssignmentManager.vue";
import TasksHandler from "@/api/TasksHandler.ts";

const emit = defineEmits(["close", "task-updated", "task-deleted"]);
const minDate = ref(new Date().toISOString().split('T')[0]);

const closePopup = () => {
  emit("close");
};

const props = defineProps<{
  task: {
    taskID: number;
    taskName: string;
    taskDescription: string | null;
    taskPriority: string | null;
    createdAt: string | Date;
    updatedAt: string | Date | null;
    dueDate: string | Date | null;
    dateCompleted: string | Date | null;
    taskAssignees: { userID: number; githubID: string }[] | null;
    dependantTask: { taskID: number; taskName: string } | null;
  } | null;
}>();

const customFormatDate = (date: Date | string | null): string => {
  if (!date) return "No date";
  const parsedDate = typeof date === "string" ? new Date(date) : date;
  const day = String(parsedDate.getDate()).padStart(2, "0");
  const month = parsedDate.toLocaleString("default", { month: "long" });
  const year = parsedDate.getFullYear();
  return `${day} ${month} ${year}`;
};

const isEditing = ref(false);
const editableTask = ref({ ...props.task });
const showDependencies = ref(false);
const showAssignments = ref(false);

watch(() => props.task, (newTask) => {
  editableTask.value = { ...newTask };
}, { deep: true });

const startEdit = () => {
  isEditing.value = true;
  showDependencies.value = false;
  showAssignments.value = false;

  editableTask.value = { ...props.task };
  if (editableTask.value && editableTask.value.taskPriority === undefined) {
    editableTask.value.taskPriority = null; 
  }

};

const toggleDependencies = () => {
  showDependencies.value = !showDependencies.value;
  if (showDependencies.value) {
    isEditing.value = false;
    showAssignments.value = false;
  }
};

const toggleAssignments = () => {
  showAssignments.value = !showAssignments.value;
  if (showAssignments.value) {
    isEditing.value = false;
    showDependencies.value = false;
  }
};

const cancelEdit = () => {
  isEditing.value = false;
};


const handleSave = async (): Promise<void> => {
  if (!editableTask.value) return;

  try {
 
    await TasksHandler.updateTask(editableTask.value, jwtToken);

    const fullUpdatedTask = await TasksHandler.openTaskPopup(editableTask.value.taskID);
    emit("task-updated", fullUpdatedTask);
    isEditing.value = false;
  } catch (error) {
    console.error("Error updating task:", error);
  }
};

const handleDelete = async (): Promise<void> => {
  if (!props.task) return;

  try {
    await TasksHandler.deleteTask(props.task.taskID, jwtToken);
    emit("task-deleted", props.task.taskID);
    closePopup()
  } catch (error) {
    console.error("Error deleting task:", error);
  }
};

const taskDependencies = computed(() => {
  return props.task?.dependantTask ? [props.task.dependantTask] : [];
});


const onDependencyAdded = (newDependency: any) => {

  if (props.task) {
    props.task.dependantTask = newDependency; 
  }
};


const onDependencyDeleted = (dependencyID: number) => {
  if (props.task && props.task.dependantTask?.taskID === dependencyID) {
    props.task.dependantTask = null; 
  }
};
const taskState = ref({ ...props.task });

const onAssigneeAdded = (newAssignee: { userID: number; githubID: string }) => {
  if (taskState.value?.taskAssignees) {
    taskState.value.taskAssignees.push(newAssignee);
  } else if (taskState.value) {
    taskState.value.taskAssignees = [newAssignee];
  }
};

const onAssigneeRemoved = (githubID: string) => {

  if (taskState.value?.taskAssignees) {
    taskState.value.taskAssignees = taskState.value.taskAssignees.filter(
      (assignee) => assignee.githubID !== githubID
    );
  }
};

const formattedDueDate = computed({
  get: () => {

    return editableTask.value.dueDate ? new Date(editableTask.value.dueDate).toISOString().split('T')[0] : '';
  },
  set: (newValue: string) => {
    editableTask.value.dueDate = new Date(newValue);
  }
});
</script>

<template>
  <dialog class="task-popup" v-if="task" open>
    <article class="popup-content">
      <header class="popup-header">
        <h2 v-if="!isEditing">{{ task.taskName }}</h2>
        <button @click="closePopup">&times;</button>
      </header>

      <section class="popup-details" v-if="!isEditing && !showDependencies && !showAssignments" >
        <p><strong>Description:</strong> {{ task.taskDescription }}</p>
        <p :data-priority="task.taskPriority"><strong>Priority:</strong> {{ task.taskPriority }}</p>
        <p><strong>Due Date:</strong> <time>{{ customFormatDate(task.dueDate) }}</time></p>
        <p><strong>Created:</strong> <time>{{ customFormatDate(task.createdAt) }}</time></p>
        <p v-if="task.dateCompleted">
          <strong>Completed:</strong> <time>{{ customFormatDate(task.dateCompleted) }}</time>
        </p>

        <section v-if="task.dependantTask">
          <h3>Dependant Task:</h3>
          <p>{{ task.dependantTask.taskName }}</p>
        </section>

        <section v-if="task.taskAssignees && task.taskAssignees.length">
          <p>
          <h3>Assignees:</h3>
          <ul>
            <li v-for="user in task.taskAssignees" :key="user.userID">
              {{ user.githubID }}
            </li>
          </ul>
        </p>
        </section>
      </section>
    
      <section v-else>
  <form v-if="!showDependencies && !showAssignments">
    <section class="form-group" v-if="!showDependencies && !showAssignments">
      <label for="task-name">Task Name</label>
      <input id="task-name" v-model="editableTask.taskName" type="text"/>
    </section>

    <section class="form-group">
      <label for="task-description">Description</label>
      <textarea id="task-description" v-model="editableTask.taskDescription" ></textarea>
    </section>

    <section class="form-group">
      <label for="task-priority">Priority</label>
      <select id="task-priority" v-model="editableTask.taskPriority" >
        <option :value="null">None</option>
        <option value="1">Low</option>
        <option value="2">Medium</option>
        <option value="3">High</option>
      </select>
    </section>

    <section class="form-group">
      <label for="due-date">Due Date</label>
      <input id="due-date" v-model="formattedDueDate" type="date" :min="minDate" />
    </section>
  </form>
</section>

<section id="EditAndDeleteBtn">
  <button v-if="!isEditing" @click="startEdit" class="edit-button" >Edit</button>
  <button v-if="isEditing" @click="handleSave" class="save-button">Save</button>
  <button v-if="isEditing" @click="cancelEdit" class="cancel-button">Cancel</button>
  <button @click="handleDelete" class="delete-button">Delete</button>
</section>

      <footer id="assignmentAndDependency" class="popup-footer">
        <section class="task-toggle">
          <button :class="{ active: showDependencies }" @click="toggleDependencies">Dependencies</button>
          <button :class="{ active: showAssignments }" @click="toggleAssignments">Assign Members</button>

          <TaskDependencyManager
            v-if="showDependencies"
            :taskID="task.taskID"
            :dependencies="taskDependencies"
            @dependency-added="onDependencyAdded"
            @dependency-deleted="onDependencyDeleted" 
          />
          <TaskAssignmentManager
            v-if="showAssignments"
            :taskID="task.taskID"
            :assignees="task.taskAssignees"
            @assignee-added="onAssigneeAdded"
            @assignee-removed="onAssigneeRemoved"
          />
        </section>
    </footer>
    </article>
  </dialog>
</template>

<style scoped>

.task-popup {
  position: fixed;
  top: 45%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
  border: 0.05rem solid #ccc;
  z-index: 100;
  width: 30rem;
  padding: 1rem;
  border-radius: 0.75rem;
  box-shadow: 0 0.25rem 1rem rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: .2rem;
  margin-left: 1em;
}

.popup-header button {
  background: none; 
  border: none;
  font-size: 20pt;
  color: #5c2e91;
  cursor: pointer;
}

.popup-details {
  display: flex;
  flex-direction: column;
  font-size: 10pt;
  color: #2d2d2d;
  background-color: #f9f8ff;
  padding: .3rem;
  border: 0.05rem solid #ccc;
  border-radius: 0.75rem;
  gap: .5rem;
  box-shadow: inset 0 0.1rem 0.25rem rgba(0, 0, 0, 0.05);
}

.popup-details p {
  display: flex;
  flex-direction: column;
  padding: 0.5rem 0.75rem;
  border: 0.05rem solid #e2e2e2;
  border-radius: 0.5rem;
  background-color: #fafafa;
}

.popup-details strong {
  font-weight: 600;
  color: #5c2e91;
  font-size: 11pt;
}
.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.1rem;
  padding: 0.3rem;
  border: 0.05rem solid #e2e2e2;
  border-radius: 0.5rem;
  background-color: #fafafa;
}

.form-group label {
  font-weight: 600;
  color: #5c2e91;
  font-size: 11pt;
}


.form-group input,
.form-group textarea,
.form-group select {
  padding: 0.6rem 1rem;
  border-radius: 0.3rem;
  border: 0.1rem solid #ccc;
  width: 100%;
  margin-bottom: 1rem;
}


footer{
  display: flex;
  flex-flow: row wrap;
  justify-content: center;
  text-align: center;
}

.task-toggle button{
  display: flex;
  flex-direction: column;
  justify-self: center;
  width: 10rem;
  margin-top: .5rem;
  padding: 0.5rem;
  color: white;
  background-color: #7b7b7b;
  border: 0.05rem solid rgb(184, 184, 184);
  box-shadow: 1pt 1pt 1pt rgba(0, 0, 0, 0.1);
  border-radius: 0.5rem;
  cursor: pointer;
}

.task-toggle button:hover {
  background-color: #636363;
  color: white;
}

#EditAndDeleteBtn {
  display: flex;
  justify-content: space-evenly;
  flex-flow: row wrap;
}

.edit-button,
.save-button,
.cancel-button,
.delete-button {
  margin-top: .5rem;
  width: 5rem;
  padding: 0.5rem;
  border-radius: 0.5rem;
  cursor: pointer;
  text-align: center;
} 
.edit-button{
  background-color: rgb(255, 170, 0);
  border: 0.05rem solid rgb(190, 127, 2);
  box-shadow: 1pt 1pt 1pt rgba(190, 127, 2, 0.379);
  color: white;
}
.edit-button:hover{
  background-color: rgb(190, 127, 2);
}

.save-button{
  background-color: rgb(5, 199, 5);
  border: 0.05rem solid rgb(0, 168, 0);
  box-shadow: 1pt 1pt 1pt rgba(0, 219, 0, 0.379);
  color: white;
}
.save-button:hover{
  background-color: rgb(1, 133, 1);
}

.delete-button{
  background-color: rgb(255, 0, 0);
  border: 0.05rem solid rgb(168, 1, 1);
  box-shadow: 1pt 1pt 1pt rgba(219,0, 0, 0.379);
  color: white;
}
.delete-button:hover{
  background-color: rgb(177, 0, 0);
}


.cancel-button {
  background-color: var(--primary-color);
  border-color: rgb(98, 0, 255);
  box-shadow: 1pt 1pt 1pt rgba(226, 221, 233, 0.107);
  color: #ffffff;
}

.cancel-button:hover {
  background-color: #6e41c9;
}

button:disabled {
  background-color: #ababab;
  cursor: not-allowed;
}

.task-toggle button.active {
  background-color: #272727;
  color: white;
} 

li{
  list-style: none;
}
</style>


