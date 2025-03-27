import type { SectionTask } from "@/models/SectionTask.ts";
import type { Task } from "@/models/Task.ts";
import { jwtToken } from "@/models/JWTToken.ts";
import { removeJwt } from "./Main";

export default class TasksHandler {
  static async getTasksForSection(sectionID: number): Promise<SectionTask[]> {
    try {
      const response = await fetch(`http://localhost:8080/sections/${sectionID}/tasks`, {
        method: "GET",
        headers: {
          Accept: "*/*",
          Authorization: `Bearer ${jwtToken}`,
        },
      });
      if(response.status === 401) {
        removeJwt();
      }

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to fetch tasks: ${errorMessage}`);
      }
      const task = await response.json();
      return task.map((task: any) => ({
        taskID: task.taskID,
        taskName: task.taskName,
        parentTaskID: task.parentTaskID ?? null,
        taskPosition: task.taskPosition ?? 0,
        createdAt: new Date(task.createdAt),
        dueDate: task.dueDate ? new Date(task.dueDate) : null,
      }))
    } catch (error) {
      console.error(`Error loading tasks for section ${sectionID}:`, error);
      return []
    }
  };

  static async addTask(projectID: string, sectionID: number, taskData: {taskName: string, taskDescription: string | null, taskPriority: number | null, dueDate: string | null}): Promise<SectionTask | null> {
    try {
      const params = new URLSearchParams();

      params.append("projectID", projectID);
      params.append("sectionID", String(sectionID));
      if (taskData.taskName) params.append("taskName", taskData.taskName);
      if (taskData.taskDescription) params.append("taskDescription", taskData.taskDescription);
      if (taskData.taskPriority !== null && taskData.taskPriority !== undefined) {
        params.append("taskPriority", String(taskData.taskPriority));
      }
      if (taskData.dueDate) params.append("dueDate", new Date(taskData.dueDate).toISOString());


      const response = await fetch(`http://localhost:8080/tasks?${params.toString()}`, {
        method: "POST",
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
        },
      });

      if(response.status === 401) {
        removeJwt();
      }

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to create task: ${errorMessage}`);
      }

      const createdTask = await response.json();
      return {
        taskID: createdTask.taskID,
        taskName: createdTask.taskName,
        parentTaskID: createdTask.parentTaskID ? createdTask.parentTaskID: null,
        taskPosition: createdTask.taskPosition,
        dueDate: createdTask.dueDate? new Date(createdTask.dueDate) : null,
        createdAt: new Date(createdTask.createdAt),
      }
    } catch (error) {
      console.error("Error adding task:", error);
      return null
    }
  };

  static async openTaskPopup(taskID: number | undefined): Promise<Task> {
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

    if (response.status === 401) {
      removeJwt();
    }
    const task = await response.json();
    return {
      taskID: task.taskID,
      taskName: task.taskName,
      taskDescription: task.taskDescription,
      taskPriority: task.taskPriority,
      createdAt: new Date(task.createdAt),
      updatedAt: task.updatedAt ? new Date(task.updatedAt) : null,
      dueDate: task.dueDate ? new Date(task.dueDate) : null,
      dateCompleted: task.dateCompleted ? new Date(task.dateCompleted) : null,
      taskAssignees: task.taskAssignees,
      dependantTask: task.dependantTask,
      }
  };

  static async updateTask(task: Partial<Task>, jwtToken: string | null): Promise<Task> {
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

    if(response.status === 401) {
      removeJwt();
    }
    return response.json();
  }

  static async updateTaskPosition(taskID: number, newTaskPosition: number, sectionID: number, jwtToken: string | null): Promise<boolean> {
    if (!jwtToken) throw new Error("JWT token is missing");
    try {
      const response = await fetch(`http://localhost:8080/tasks/${taskID}/position`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwtToken}`
        },
        body: JSON.stringify({
          taskPosition: newTaskPosition,
          sectionID: sectionID
        })
      })

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to update task position: ${errorMessage}`);
      }
      return true
    } catch (error) {
      console.error("Error updating task position:", error);
      return false
    }

  }


  static async deleteTask(taskID: number, jwtToken: string | null): Promise<void> {
    if (!jwtToken) throw new Error("JWT token is missing");

    const response = await fetch(`http://localhost:8080/tasks/${taskID}`, {
      method: "DELETE",
      headers: {Authorization: `Bearer ${jwtToken}`},
    });

    if (!response.ok) {
      const errorMessage = await response.text();
      throw new Error(`Failed to delete task: ${errorMessage}`);
    }
  }
}
