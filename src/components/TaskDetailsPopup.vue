<script setup lang="ts">
import { ref } from "vue";
import type {Task} from "@/models/Task.ts";
import { updateTask, deleteTask } from "@/api/TasksHandler.ts"
import { jwtToken } from "@/models/JWTToken.ts";

const emit = defineEmits(["close", "task-updated", "task-deleted"])

const closePopup = () => {
  emit("close")
}

const props = defineProps<{
  task: {
    taskID: number;
    taskName: string;
    taskDescription: string;
    taskPriority: string | null;
    createdAt: Date;
    updatedAt: Date;
    dueDate: Date;
    dateCompleted: Date | null;
    taskAssignees: { userID: number; githubID: string }[] | null;
    dependantTask: { taskID: number; taskName: string } | null;
  } | null;
}>()

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

const startEdit = () => {
  isEditing.value = true;
  editableTask.value = { ...props.task };
};

const cancelEdit = () => {
  isEditing.value = false;
};

const handleSave = async (): Promise<void> => {
  if (!editableTask.value) return;

  try {
    const updatedTask: Task = await updateTask(editableTask.value, jwtToken);
    emit("task-updated", updatedTask);
    isEditing.value = false;
  } catch (error) {
    console.error("Error updating task:", error);
  }
};

const handleDelete = async (): Promise<void> => {
  if (!props.task) return;

  try {
    await deleteTask(props.task.taskID, jwtToken);
    emit("task-deleted", props.task.taskID);
  } catch (error) {
    console.error("Error deleting task:", error);
  }
};
</script>


<template>
  <dialog class="task-popup" v-if="task" open>
    <article class="popup-content">
      <header class="popup-header">
        <h2 v-if="!isEditing">{{ task.taskName }}</h2>
        <input v-else v-model="editableTask.taskName" type="text" />
        <button @click="closePopup" aria-label="Close popup">&times;</button>
      </header>

      <section class="popup-details" v-if="!isEditing">
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
          <h3>Assignees:</h3>
          <ul>
            <li v-for="user in task.taskAssignees" :key="user.userID">
              GitHub: {{ user.githubID }}
            </li>
          </ul>
        </section>
      </section>

      <section v-else>
        <form>
          <section class="form-group">
            <label for="task-name">Task Name</label>
            <input id="task-name" v-model="editableTask.taskName" type="text" />
          </section>

          <section class="form-group">
            <label for="task-description">Description</label>
            <textarea id="task-description" v-model="editableTask.taskDescription"></textarea>
          </section>

          <section class="form-group">
            <label for="task-priority">Priority</label>
            <select id="task-priority" v-model="editableTask.taskPriority">
              <option :value="null">None</option>
              <option :value="1">Low</option>
              <option :value="2">Medium</option>
              <option :value="3">High</option>
            </select>
          </section>

          <section class="form-group">
            <label for="due-date">Due Date</label>
            <input id="due-date" v-model="editableTask.dueDate" type="date" />
          </section>
        </form>
      </section>

      <footer class="popup-footer">
        <button v-if="!isEditing" @click="startEdit" class="edit-button">Edit</button>
        <button v-if="isEditing" @click="handleSave" class="save-button">Save</button>
        <button v-if="isEditing" @click="cancelEdit" class="cancel-button">Cancel</button>
        <button @click="handleDelete" class="delete-button">Delete</button>
      </footer>
    </article>
  </dialog>
</template>

<style>
::v-deep(.task-popup) {
  position: fixed;
}

:root {
  --primary: #4a6cfa;
  --primary-light: #eef1ff;
  --success: #34d399;
  --warning: #fbbf24;
  --danger: #f87171;
  --gray-100: #f3f4f6;
  --gray-200: #e5e7eb;
  --gray-300: #d1d5db;
  --gray-700: #374151;
  --gray-800: #1f2937;
  --gray-900: #111827;
  --radius: 12px;
  --shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
}

@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.95); }
  to { opacity: 1; transform: scale(1); }
}


.task-popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 1rem;
  border: none;
  overflow: auto;
}

.popup-content {
  background-color: white;
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  width: 100%;
  max-width: 550px;
  max-height: 90vh;
  overflow-y: auto;
  animation: fadeIn 0.3s ease-out;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--gray-200);
  position: sticky;
  top: 0;
  background-color: white;
  border-radius: var(--radius) var(--radius) 0 0;
}

.popup-header h2 {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--gray-900);
}

.popup-header button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: var(--gray-700);
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.2s;
}

.popup-header button:hover {
  background-color: var(--gray-100);
  color: var(--gray-900);
}


.popup-details {
  padding: 1.5rem;
  color: var(--gray-800);
}

.popup-details p {
  margin-bottom: 1rem;
  line-height: 1.5;
}

.popup-details strong {
  color: var(--gray-900);
  font-weight: 600;
}


.popup-details p:nth-child(2) strong {
  display: inline-block;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-weight: 500;
  margin-left: 0.5rem;
}

.popup-details p:nth-child(2)[data-priority="High"] strong {
  background-color: var(--danger);
  color: white;
}

.popup-details p:nth-child(2)[data-priority="Medium"] strong {
  background-color: var(--warning);
  color: var(--gray-900);
}

.popup-details p:nth-child(2)[data-priority="Low"] strong {
  background-color: var(--success);
  color: white;
}


.popup-details p:nth-child(3),
.popup-details p:nth-child(4),
.popup-details p:nth-child(5) {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.popup-details p:nth-child(3) strong + time,
.popup-details p:nth-child(4) strong + time,
.popup-details p:nth-child(5) strong + time {
  background-color: var(--primary-light);
  color: var(--primary);
  padding: 0.25rem 0.5rem;
  border-radius: 6px;
  font-size: 0.875rem;
}


.popup-details p:nth-child(1) {
  background-color: var(--gray-100);
  padding: 1rem;
  border-radius: var(--radius);
  border-left: 4px solid var(--primary);
  margin-bottom: 1.5rem;
}


.popup-details section {
  margin-top: 1.5rem;
  padding: 1rem;
  background-color: var(--gray-100);
  border-radius: var(--radius);
}

.popup-details section h3 {
  margin-top: 0;
  margin-bottom: 0.75rem;
  font-size: 1rem;
  color: var(--gray-900);
  font-weight: 600;
}


.popup-details ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.popup-details li {
  padding: 0.5rem 0;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.popup-details li::before {
  content: "";
  display: inline-block;
  width: 8px;
  height: 8px;
  background-color: var(--primary);
  border-radius: 50%;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--primary-light);
  padding: 16px;
  border-radius: var(--radius) var(--radius) 0 0;
}
.popup-header h2 {
  margin: 0;
  color: var(--gray-900);
}
.popup-header button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: var(--gray-700);
}

.popup-details, .form-group {
  margin-bottom: 16px;
}
.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 4px;
  color: var(--gray-700);
}
.form-group input, .form-group textarea {
  width: 100%;
  padding: 8px;
  margin-top: 4px;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius);
}
.form-group textarea {
  height: 80px;
  resize: vertical;
}

.popup-footer {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  margin-top: 16px;
}
.save-button, .edit-button, .delete-button {
  background-color: var(--primary);
  color: #fff;
  padding: 8px 16px;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
}
.save-button {
  background-color: var(--success);
}
.delete-button {
  background-color: var(--danger);
}
.save-button:hover, .edit-button:hover, .delete-button:hover {
  opacity: 0.9;
}
.cancel-button {
  background-color: var(--warning);
  color: #fff;
  padding: 8px 16px;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
}
</style>
