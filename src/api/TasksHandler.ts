import { ref } from "vue";
import type { SectionTask } from "@/models/SectionTask.ts";
import type { Task } from "@/models/Task.ts";
import { useRoute } from "vue-router";
import { jwtToken } from "@/models/JWTToken.ts";

export function useTasks(sectionID: number) {
  const route = useRoute();
  const projectID = route.params.id as string;

  const sectionTask = ref<SectionTask[]>([]);
  const selectedTask = ref(null);
  const showForm = ref(false);

  const newTask = ref({
    taskName: "",
    taskDescription: null as string | null,
    taskPriority: null as number | null,
    dueDate: null as string | null,
  });

  function mapTaskData(task: any): SectionTask {
    return {
      taskID: task.taskID,
      taskName: task.taskName,
      parentTaskID: task.parentTaskID ?? null,
      taskPosition: task.taskPosition ?? 0,
      createdAt: new Date(task.createdAt),
      dueDate: task.dueDate ? new Date(task.dueDate) : null,
    };
  }

  const getTasksForSection = async () => {
    try {
      const response = await fetch(`http://localhost:8080/sections/${sectionID}/tasks`, {
        method: "GET",
        headers: {
          Accept: "*/*",
          Authorization: `Bearer ${jwtToken}`,
        },
      });

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to fetch tasks: ${errorMessage}`);
      }
      const data = await response.json();
      sectionTask.value = data.map(mapTaskData);
    } catch (error) {
      console.error(`Error loading tasks for section ${sectionID}:`, error);
    }
  };

  const addTask = async () => {
    try {
      const params = new URLSearchParams();

      params.append("projectID", String(projectID));
      params.append("sectionID", String(sectionID));
      if (newTask.value.taskName) params.append("taskName", newTask.value.taskName);
      if (newTask.value.taskDescription) params.append("taskDescription", newTask.value.taskDescription);
      if (newTask.value.taskPriority !== null && newTask.value.taskPriority !== undefined) {
        params.append("taskPriority", String(newTask.value.taskPriority));
      }
      if (newTask.value.dueDate) params.append("dueDate", new Date(newTask.value.dueDate).toISOString());


      const response = await fetch(`http://localhost:8080/tasks?${params.toString()}`, {
        method: "POST",
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
        },
      });

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to create task: ${errorMessage}`);
      }

      const createdTask = await response.json();
      sectionTask.value.push(createdTask);
      newTask.value.taskName = "";
      newTask.value.taskDescription = null;
      newTask.value.taskPriority = null;
      newTask.value.dueDate = null;
      showForm.value = false;
    } catch (error) {
      console.error("Error adding task:", error);
    }
  };



  const cancelTask = () => {
    showForm.value = false;
    newTask.value = { taskName: "", taskDescription: "", taskPriority: 1, dueDate: "" };
  };

  const openTaskPopup = async (taskID: number) => {
    try {
      const response = await fetch(`http://localhost:8080/tasks/${taskID}`, {
        method: "GET",
        headers: {
          Accept: "*/*",
          Authorization: `Bearer ${jwtToken}`,
        },
      });

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to fetch task: ${errorMessage}`);
      }
      selectedTask.value = await response.json();
    } catch (error) {
      console.error("Error loading task details:", error);
    }
  };

  return {
    sectionTask,
    selectedTask,
    showForm,
    newTask,
    addTask,
    projectID,
    getTasksForSection,
    cancelTask,
    openTaskPopup,
  };
}

export async function updateTask(task: Partial<Task>, jwtToken: string | null): Promise<Task> {
  if (!jwtToken) throw new Error("JWT token is missing");

  const response = await fetch(`http://localhost:8080/tasks/${task.taskID}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${jwtToken}`,
    },
    body: JSON.stringify({
      taskName: task.taskName || null,
      taskDescription: task.taskDescription || null,
      taskPriority: task.taskPriority || null,
      dueDate: task.dueDate ? new Date(task.dueDate).toISOString() : null,
      dateCompleted: task.dateCompleted ? new Date(task.dateCompleted).toISOString() : null,
    }),
  });

  if (!response.ok) {
    const errorMessage = await response.text();
    throw new Error(`Failed to update task: ${errorMessage}`);
  }
  return response.json();
}

export async function deleteTask(taskID: number, jwtToken: string | null): Promise<void> {
  if (!jwtToken) throw new Error("JWT token is missing");

  const response = await fetch(`http://localhost:8080/tasks/${taskID}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${jwtToken}` },
  });

  if (!response.ok) {
    const errorMessage = await response.text();
    throw new Error(`Failed to delete task: ${errorMessage}`);
  }
}

// export async function addTask(
//   projectID: number,
//   sectionID: number,
//   taskData: {
//     taskName: string;
//     taskDescription?: string | null;
//     taskPriority?: number | null;
//     dueDate?: string | null;
//   },
//   jwtToken: string | null,
// ): Promise<SectionTask | null> {
//   if (!jwtToken) throw new Error("JWT token is missing");
//
//   try {
//     const params = new URLSearchParams({
//       projectID: projectID.toString(),
//       sectionID: sectionID.toString(),
//       taskName: taskData.taskName,
//     });
//
//     // Only add optional params if they have a value
//     if (taskData.taskDescription) params.append("taskDescription", taskData.taskDescription);
//     if (taskData.taskPriority !== undefined && taskData.taskPriority !== null)
//       params.append("taskPriority", taskData.taskPriority.toString());
//     if (taskData.dueDate) params.append("dueDate", taskData.dueDate);
//
//     const response = await fetch(
//       `http://localhost:8080/tasks?${params.toString()}`,
//       {
//         method: "POST",
//         headers: {
//           Accept: "*/*",
//           Authorization: `Bearer ${jwtToken}`,
//         },
//       },
//     );
//
//     if (!response.ok) {
//       const errorMessage = await response.text();
//       throw new Error(`Failed to create task: ${errorMessage}`);
//     }
//
//     return await response.json();
//   } catch (error) {
//     console.error("Error adding task:", error);
//     return null;
//   }
// }
