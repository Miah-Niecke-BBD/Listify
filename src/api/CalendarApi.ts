export async function GetDueTasks<dueTasks>(jwtToken: string|null,teamID:string): Promise<dueTasks | null> {
    try {
      const response = await fetch("http://localhost:8080/teams/"+teamID+"/dueTasks", {
        headers: {
          "Authorization": `Bearer ${jwtToken}`,
        },
      });
  
      const data = await response.json();
  
      if (!response.ok) {
       
        return null
      }
  
      return data;
    } catch (error) {
      return null;
    }
  }