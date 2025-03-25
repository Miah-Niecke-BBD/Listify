import type { TeamMember } from "./TeamMember";

export interface Project {
    id: number;
    name: string;
    description: string;
    projectAssignees: TeamMember[];
}