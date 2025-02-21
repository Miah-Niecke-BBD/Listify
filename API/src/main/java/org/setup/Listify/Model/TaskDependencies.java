package org.setup.Listify.Model;

import jakarta.persistence.*;

@Entity
public class TaskDependencies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskDependencyID;
    @Column(name = "taskID", nullable = false)
    private Long taskID;
    @Column(name = "dependentTaskID", nullable = false)
    private Long dependentTaskID;

    protected TaskDependencies() {
    }

    @Override
    public String toString() {
        return "TaskDependency{" +
                "taskDependencyID=" + taskDependencyID +
                ", taskID=" + taskID +
                ", dependentTaskID=" + dependentTaskID +
                '}';
    }

    public TaskDependencies(Long taskDependencyID, Long taskID, Long dependentTaskID) {
        this.taskDependencyID = taskDependencyID;
        this.taskID = taskID;
        this.dependentTaskID = dependentTaskID;
    }

    public Long getTaskDependencyID() {
        return taskDependencyID;
    }

    public void setTaskDependencyID(Long taskDependencyID) {
        this.taskDependencyID = taskDependencyID;
    }

    public Long getTaskID() {
        return taskID;
    }

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }

    public Long getDependentTaskID() {
        return dependentTaskID;
    }

    public void setDependentTaskID(Long dependentTaskID) {
        this.dependentTaskID = dependentTaskID;
    }
}
