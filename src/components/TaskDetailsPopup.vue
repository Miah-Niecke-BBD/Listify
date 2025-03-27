<script setup lang="ts">
import { ref, watch } from "vue";
import { jwtToken } from "@/models/JWTToken.ts";
import TaskDependencyManager from "@/components/TaskDependencyManager.vue";
import TaskAssignmentManager from "@/components/TaskAssignmentManager.vue";
import TasksHandler from "@/api/TasksHandler.ts";

const emit = defineEmits(["close", "task-updated", "task-deleted"]);

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
  editableTask.value = { ...props.task };
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

const toggleDependencies = () => (showDependencies.value = !showDependencies.value);
const toggleAssignments = () => (showAssignments.value = !showAssignments.value);

const taskDependencies = ref(props.task?.dependantTask ? [props.task.dependantTask] : []);

const onDependencyAdded = (newDependency: any) => {
  taskDependencies.value.push(newDependency);
};

const onDependencyDeleted = (dependencyID: number) => {
  taskDependencies.value = taskDependencies.value.filter(
    (dep) => dep.taskID !== dependencyID
  );
};

const taskState = ref({ ...props.task });

const onAssigneeAdded = (newAssignee: { userID: number; githubID: string }) => {
  if (taskState.value?.taskAssignees) {
    taskState.value.taskAssignees.push(newAssignee);
  } else if (taskState.value) {
    taskState.value.taskAssignees = [newAssignee];
  }
};

const onAssigneeRemoved = (userID: number) => {
  if (taskState.value?.taskAssignees) {
    taskState.value.taskAssignees = taskState.value.taskAssignees.filter(
      (assignee) => assignee.userID !== userID
    );
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

      <section class="popup-details" v-if="!isEditing && !showDependencies && !showAssignments">
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

      <section class="task-dependency-toggle">
        <button @click="toggleDependencies">
          {{ showDependencies ? 'Hide' : 'Manage' }} Dependencies</button>
        <TaskDependencyManager
          v-if="showDependencies"
          :taskID="task.taskID"
          :dependencies="taskDependencies"
          @dependency-added="onDependencyAdded"
          @dependency-deleted="onDependencyDeleted"
        />
      </section>

      <section class="task-assignment-toggle">
        <button @click="toggleAssignments">{{ showAssignments ? 'Hide' : 'Manage' }} Assignments</button>
        <TaskAssignmentManager
          v-if="showAssignments"
          :taskID="task.taskID"
          :assignees="task.taskAssignees"
          @assignee-added="onAssigneeAdded"
          @assignee-removed="onAssigneeRemoved"
        />
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

<style scoped>
.task-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
  border: 0.05rem solid #ccc;
  width: 90%;
  max-width: 30rem;
  padding: 1.25rem;
  border-radius: 0.75rem;
  box-shadow: 0 0.25rem 1rem rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.popup-header button {
  background: none;
  border: none;
  font-size: 1.25rem;
  color: #5c2e91;
  cursor: pointer;
}

.popup-details {
  font-size: 1rem;
  color: #2d2d2d;
  background-color: #f9f8ff;
  padding: 1rem;
  border: 0.05rem solid #ccc;
  border-radius: 0.75rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  box-shadow: inset 0 0.1rem 0.25rem rgba(0, 0, 0, 0.05);
}

.popup-details p {
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  padding: 0.5rem 0.75rem;
  border: 0.05rem solid #e2e2e2;
  border-radius: 0.5rem;
  background-color: #fafafa;
}

.popup-details strong {
  font-weight: 600;
  color: #5c2e91;
  font-size: 0.95rem;
}

.popup-details time {
  font-style: italic;
  color: #444;
}

.popup-details section {
  padding: 0.75rem;
  border: 0.05rem solid #dad6f8;
  background-color: #f6f4ff;
  border-radius: 0.5rem;
}

.popup-details h3 {
  font-size: 1rem;
  color: #5c2e91;
  margin-bottom: 0.5rem;
  border-bottom: 0.05rem solid #d2c7f7;
  padding-bottom: 0.25rem;
}

.popup-details ul {
  list-style-type: disc;
  margin-left: 1.25rem;
  padding-left: 0.25rem;
}

.popup-details li {
  margin-bottom: 0.25rem;
  font-size: 0.95rem;
  color: #333;
}


section[v-if="isEditing"] form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1rem;
  border: 0.05rem solid #ccc;
  border-radius: 0.75rem;
  background-color: #fff;
  box-shadow: inset 0 0.1rem 0.25rem rgba(0, 0, 0, 0.05);
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  padding: 0.75rem;
  border: 0.05rem solid #e2e2e2;
  border-radius: 0.5rem;
  background-color: #fafafa;
}

.form-group label {
  font-weight: 600;
  color: #5c2e91;
  font-size: 0.95rem;
}

.form-group input,
.form-group textarea,
.form-group select {
  font-size: 0.95rem;
  padding: 0.5rem;
  border: 0.05rem solid #ccc;
  border-radius: 0.5rem;
  background-color: #fff;
  color: #333;
  font-family: inherit;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  outline: none;
  border-color: #8e7cf1;
  box-shadow: 0 0 0.15rem #8e7cf1;
}

.task-dependency-toggle button,
.task-assignment-toggle button {
  margin-top: 1rem;
  width: 100%;
  padding: 0.5rem;
  background-color: #fbfbfb;
  border: 0.05rem solid black;
  border-radius: 0.5rem;
  cursor: pointer;
  text-align: center;
}

.task-dependency-toggle button:hover,
.task-assignment-toggle button:hover {
  background-color: #8e7cf1;
  color: white;
}

.popup-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 1.25rem;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.edit-button,
.save-button,
.cancel-button,
.delete-button {
  background-color: #8e7cf1;
  color: white;
  padding: 0.5rem 1.25rem;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
  font-size: 0.95rem;
  font-weight: 500;
}

.edit-button:hover,
.save-button:hover,
.delete-button:hover {
  background-color: #5c2e91;
}

.cancel-button {
  background-color: #f3f3f3;
  color: #5c2e91;
}

.cancel-button:hover {
  background-color: #e1d4f9;
}

button:disabled {
  background-color: #ababab;
  cursor: not-allowed;
}
</style>


