import type { TaskDependency } from "@/models/TaskDependency";
import {jwtToken} from "@/models/JWTToken.ts";
const API_BASE_URL = "http://localhost:8080/tasks/dependencies";

export default class TaskDependencyHandler {
  static async createDependency(taskID: number, dependentTaskID: number): Promise<TaskDependency | null> {
    try {
      const response = await fetch(`${API_BASE_URL}?taskID=${encodeURIComponent(taskID)}&dependentTaskID=${encodeURIComponent(dependentTaskID)}`, {
        method: "POST",
        headers: {
          Accept: "*/*",
          Authorization: `Bearer ${jwtToken}`
        },
      })

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to create task dependency: ${errorMessage}`);
      }

      const taskDependency = await response.json()

      return {
        taskDependencyID: taskDependency.taskDependencyID,
        taskID: taskDependency.taskID,
        dependentTaskID: taskDependency.dependentTaskID
      }
    } catch (error) {
      console.error("Error creating task dependency:", error);
      return null
    }
  }

  static async deleteDependency(dependentTaskID: number, taskID: number): Promise<boolean> {
    try {
      const response = await fetch(`${API_BASE_URL}/${dependentTaskID}?taskID=${encodeURIComponent(taskID)}`, {
        method: "DELETE",
        headers: {
          Accept: "*/*",
          Authorization: `Bearer ${jwtToken}`
        },
      })

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to create task dependency: ${errorMessage}`);
      }
      return true
    } catch (error) {
      console.error("Error deleting task dependency:", error);
      return false
    }
  }
}
