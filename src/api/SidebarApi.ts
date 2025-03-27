import type { TeamInterface } from "@/models/TeamInterface";

export async function GetTeams<TeamInterface>(jwtToken: string | null): Promise<TeamInterface | null> {
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


export async function GetProjects<ProjectInterface>(jwtToken: string | null, teamID: string): Promise<ProjectInterface | null> {
  try {
    const response = await fetch("http://localhost:8080/teams/" + teamID + "/projects", {
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

export async function addNewTeam(jwtToken: string | null, teamName: string): Promise<number> {
  try {
    const response = await fetch(`http://localhost:8080/teams?teamName=${teamName}`, {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${jwtToken}`,
      },
    });

    const data: TeamInterface = await response.json();

    if (!response.ok) {
      const errorMessage = await response.text();
      throw new Error(`Request failed with status ${response.status}: ${errorMessage}`);
    }

    return data.teamID;
  } catch (error) {
    throw error;
  }
}
