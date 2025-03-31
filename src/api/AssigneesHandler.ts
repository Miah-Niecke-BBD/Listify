import type {Assignee} from "@/models/Assignee.ts";
import { jwtToken } from "@/models/JWTToken";
const API_BASE_URL = "http://localhost:8080";


export default class AssigneesHandler {
  static async assignTask(taskID : number, githubID: string): Promise<Assignee | null> {
    try {
      const response = await fetch(`${API_BASE_URL}/tasks/assignee/${taskID}?githubID=${encodeURIComponent(githubID)}`,
        {
          method: 'POST',
          headers: {
            Accept: "*/*",
            Authorization: `Bearer ${jwtToken}`
          },
        }
      )

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to assign task: ${errorMessage}`);
      }

      const taskAssignee = await response.json()
      return {
        taskAssigneeID: taskAssignee.taskAssigneeID,
        userID: taskAssignee.userID,
        taskID: taskAssignee.taskID
      }
    } catch (error) {
      console.error("Error assigning task:", error);
      return null;
    }
  }


  static async deleteAssignment(taskID: number, githubID: string): Promise<boolean> {
    try {
      const response = await fetch(`${API_BASE_URL}/tasks/assignee/${taskID}?githubID=${encodeURIComponent(githubID)}`, {
          method: "DELETE",
          headers: {
            Accept: "*/*",
            Authorization: `Bearer ${jwtToken}`
          },
        }
      )

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to unassign task: ${errorMessage}`);
      }
      return true
    } catch (error) {
      console.error("Error unassigning task:", error);
      return false
    }
  }

  static async GetProjectMembers(jwtToken: string|null,projectID:string): Promise<any> {
    try {
      const response = await fetch("http://localhost:8080/project/assignees/"+projectID, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${jwtToken}`
        },
      });
      
      const data = await response.json();
  
   
      if (!response.ok) {
       
        return null
      }
  
      return data;
    } catch (error:any) {
  
      console.log((`Failed to get project members (status ${error.status}): ${error.message}`))
      throw error;
    }
  }

  static async GetTaskMembers<taskAssignee>(jwtToken: string|null,taskID:string): Promise<taskAssignee|null> {
    try {
      const response = await fetch("http://localhost:8080/tasks/assignee/"+taskID, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${jwtToken}`
        },
      });
      
      const data:taskAssignee = await response.json();
  
   
      if (!response.ok) {
       
        return null
      }
  
      return data;
    } catch (error:any) {
  
      console.log((`Failed to get project members (status ${error.status}): ${error.message}`))
      throw error;
    }
  }

}

