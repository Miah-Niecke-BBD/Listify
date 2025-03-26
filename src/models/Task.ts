export interface Task {
  taskID: number;
  taskName: string;
  taskDescription: string | null;
  taskPriority: string | null;
  createdAt: string | Date;
  updatedAt: string | Date | null;
  dueDate: string | Date | null;
  dateCompleted: string | Date | null;
  taskAssignees: { userID: number; githubID: string }[] | null;
  dependantTask: { taskID: number; taskName: string } | null;
}
