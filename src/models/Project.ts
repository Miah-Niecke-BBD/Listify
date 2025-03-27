import type { TeamMember } from "./TeamMember";

export interface Project {
    projectID: number;
    projectName: string;
    projectDescription: string;
    projectAssignees: TeamMember[];
}