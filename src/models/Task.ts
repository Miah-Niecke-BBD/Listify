export interface Task {
  taskID: number;
  taskName: string;
  taskDescription: string;
  taskPriority: string | null;
  createdAt: string | Date;
  updatedAt: string | Date;
  dueDate: string | Date;
  dateCompleted: string | Date | null;
  taskAssignees: { userID: number; githubID: string }[] | null;
  dependantTask: { taskID: number; taskName: string } | null;
}
