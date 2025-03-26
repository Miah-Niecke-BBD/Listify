export interface TeamInterface{
    teamID:number,
    teamName:string,
    createdAt:Date,
    updatedAt:Date|null,
    projects: ProjectInterface[]
    }


export interface ProjectInterface{
    projectID:number,
    projectName:string,
    }