export interface TeamMember {
    userID: number
    githubID: string;
    name?: string | undefined;
    teamLeader: boolean;
}