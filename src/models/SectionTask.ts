export interface SectionTask {
  taskID: number;
  taskName: string;
  taskPriority:string|null;
  parentTaskID: number | null;
  taskPosition: number;
  dateCompleted:Date|null;
  dueDate: Date | null;
  createdAt: Date;
}
