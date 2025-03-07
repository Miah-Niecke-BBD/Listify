package org.listify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

@Entity
@Table(name = "TaskDependencies", schema = "listify")
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public TaskDependencies(Long taskDependencyID, Long taskID, Long dependentTaskID) {
        this.taskDependencyID = taskDependencyID;
        this.taskID = taskID;
        this.dependentTaskID = dependentTaskID;
    }

    public Long getTaskDependencyID() {
        return taskDependencyID;
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
