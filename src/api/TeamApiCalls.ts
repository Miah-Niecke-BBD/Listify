import type { Project } from "@/models/Project";
import type { Team } from "@/models/Team";
import type { TeamMember } from "@/models/TeamMember";

const jwtToken: string | null = localStorage.getItem("jwtToken");

export const fetchTeamByID = async (teamID: string): Promise<Team> => {
    if (!jwtToken) {
        throw new Error("JWT token is missing.");
    }

    try {
        const response = await fetch(`http://localhost:8080/teams/${teamID}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwtToken}`,
            }
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(`Request failed with status ${response.status}: ${errorMessage}`);
        }

        const data: Team = await response.json();
        return data;
    } catch (error) {
        console.error("Error fetching team data:", error);
        throw error;
    }
};

export const fetchTeamMembers = async (teamID: string): Promise<TeamMember[]> => {

    if (!jwtToken) {
        throw new Error("JWT token is missing.");
    }

    try {
        const response = await fetch(`http://localhost:8080/teams/${teamID}/members`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwtToken}`,
            },
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(`Request failed with status ${response.status}: ${errorMessage}`);
        }

        const data: TeamMember[] = await response.json();

        return data;
    } catch (error) {
        console.error("Error fetching team members:", error);
        throw error;
    }
};

export const fetchProjectAssignees = async (projectID: number): Promise<{ githubID: string }[]> => {
    if (!jwtToken) {
        throw new Error("JWT token is missing.");
    }

    try {
        const response = await fetch(`http://localhost:8080/project/assignees/${projectID}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwtToken}`,
            }
        })

        const data: { githubID: string }[] = await response.json();
        return data;

    } catch (error) {
        throw error
    }

}

export const fetchTeamProject = async (teamID: string, TeamMembers: TeamMember[]): Promise<Project[]> => {
    if (!jwtToken) {
        throw new Error("JWT token is missing.");
    }

    try {
        const response = await fetch(`http://localhost:8080/teams/${teamID}/projects`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwtToken}`,
            }
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(`Request failed with status ${response.status}: ${errorMessage}`);
        }

        const data: Project[] = await response.json();

        const addedProjectAssignees: Project[] = await Promise.all(
            data.map(async (project) => {

                const projectAssignees: { githubID: string }[] = await fetchProjectAssignees(project.projectID);

                const updatedProjectAssignees: TeamMember[] = projectAssignees.flatMap((projectMember) =>
                    TeamMembers.filter((teamMember) => teamMember.githubID === projectMember.githubID)
                );

                return {
                    ...project,
                    projectAssignees: updatedProjectAssignees,
                };
            })
        );

        return addedProjectAssignees;
    } catch (error) {
        throw error;
    }
};

export const updateTeam = async (teamID: string, newTeamName: string) => {
    if (!jwtToken) {
        throw new Error("JWT token is missing.");
    }

    try {
        const response = await fetch(`http://localhost:8080/teams/${teamID}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwtToken}`,
            },
            body: JSON.stringify({
                newTeamName: newTeamName,
            }),
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(`Request failed with status ${response.status}: ${errorMessage}`);
        }
    } catch (error) {
        console.error("Error updating team:", error);
        throw error;
    }
}

export const addTeamMember = async (teamID: string, googleID: string): Promise<TeamMember[]> => {
    if (!jwtToken) {
        throw new Error("JWT token is missing.");
    }

    try {
        const response = await fetch(`http://localhost:8080/teams/${teamID}/members?githubID=${googleID}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwtToken}`,
            },
        })

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(`Request failed with status ${response.status}: ${errorMessage}`);
        }

        const data: TeamMember[] = await response.json();

        return data;
    } catch (error) {
        throw error
    }
}

export const removeMemberFromTeam = async (teamID: string, googleID: string) => {
    if (!jwtToken) {
        throw new Error("JWT token is missing.");
    }

    try {
        const response = await fetch(`http://localhost:8080/teams/${teamID}/members?githubID=${googleID}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwtToken}`,
            },
        })

        if (response.status !== 204) {
            const errorMessage = await response.text();
            throw new Error(`Request failed with status ${response.status}: ${errorMessage}`);
        }
    } catch (error) {
        throw error
    }
}

export const updateTeamLeader = async (teamID: string, googleID: string) => {
    if (!jwtToken) {
        throw new Error("JWT token is missing.");
    }

    try {
        const response = await fetch(`http://localhost:8080/teams/${teamID}/members?newTeamLeaderGithubID=${googleID}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwtToken}`,
            },
        })

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(`Request failed with status ${response.status}: ${errorMessage}`);
        }
    } catch (error) {
        throw error
    }
}

export const createProject = async (teamID: string, projectName: string, projectDescription: string, user: TeamMember): Promise<Project> => {
    if (!jwtToken) {
        throw new Error("JWT token is missing.");
    }

    try {
        const response = await fetch(`http://localhost:8080/projects?teamID=${teamID}&projectName=${projectName}&projectDescription=${projectDescription}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwtToken}`,
            },
        })

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(`Request failed with status ${response.status}: ${errorMessage}`);
        }

        const data: Project = await response.json();
        data.projectAssignees = [user];

        return data
    } catch (error) {
        throw error;
    }
}

export const deleteProject = async (projectID: number) => {
    if (!jwtToken) {
        throw new Error("JWT token is missing.");
    }
    try {
        const response = await fetch(`http://localhost:8080/projects/${projectID}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwtToken}`,
            },
        })

        if (response.status !== 204) {
            const errorMessage = await response.text();
            throw new Error(`Request failed with status ${response.status}: ${errorMessage}`);
        }
    } catch (error) {
        throw error
    }
}

export const addProjectAssignee = async (projectID: number, googleID: string) => {
    if (!jwtToken) {
        throw new Error("JWT token is missing.");
    }

    try {
        const response = await fetch(`http://localhost:8080/project/assignees/${projectID}?githubID=${googleID}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwtToken}`,
            },
        })

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(`Request failed with status ${response.status}: ${errorMessage}`);
        }

    } catch (error) {
        throw error
    }
}

export const deleteProjectAssignee = async (projectID: number, googleID: string) => {
    if (!jwtToken) {
        throw new Error("JWT token is missing.");
    }

    try {
        const response = await fetch(`http://localhost:8080/project/assignees/${projectID}?githubID=${googleID}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwtToken}`,
            },
        })
        if (response.status !== 204) {
            const errorMessage = await response.text();
            throw new Error(`Request failed with status ${response.status}: ${errorMessage}`);
        }
    } catch (error) {
        throw error
    }
}

export const deleteTeam = async (teamID: string) => {
    if (!jwtToken) {
        throw new Error("JWT token is missing.");
    }

    try {
        const response = await fetch(`http://localhost:8080/teams/${teamID}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwtToken}`,
            },
        })

        if (response.status !== 204) {
            const errorMessage = await response.text();
            throw new Error(`Request failed with status ${response.status}: ${errorMessage}`);
        }
    } catch (error) {
        throw error
    }
}