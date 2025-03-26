export async function GetTeams<TeamInterface>(jwtToken: string|null): Promise<TeamInterface | null> {
    try {
      const response = await fetch("http://localhost:8080/teams", {
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
  

  export async function GetProjects<ProjectInterface>(jwtToken: string|null , teamID:string): Promise<ProjectInterface | null> {
    try {
      const response = await fetch("http://localhost:8080/teams/"+teamID+"/projects", {
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

    
  
  