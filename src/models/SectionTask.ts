export interface SectionTask {
  taskID: number;
  taskName: string;
  parentTaskID: number | null;
  taskPosition: number;
  dueDate: Date | null;
  createdAt: Date;
}
