package org.setup.listify.dto;

public class SimpleTaskDTO {

    private Long taskID;
    private String taskName;

    public SimpleTaskDTO(Long taskID, String taskName) {
        this.taskID = taskID;
        this.taskName = taskName;
    }

    public Long getTaskID() {
        return taskID;
    }

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
