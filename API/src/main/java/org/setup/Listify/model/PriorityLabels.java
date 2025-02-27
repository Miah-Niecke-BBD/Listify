package org.setup.Listify.model;

import jakarta.persistence.*;

@Entity
public class PriorityLabels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long priorityLabelID;
    @Column(name="priorityLabelName", nullable = false, length = 15)
    private String priorityLabelName;

    protected PriorityLabels () {}

    public PriorityLabels(Long priorityLabelID, String priorityLabelName) {
        this.priorityLabelID = priorityLabelID;
        this.priorityLabelName = priorityLabelName;
    }

    public Long getPriorityLabelID() {
        return priorityLabelID;
    }

    public void setPriorityLabelID(Long priorityLabelID) {
        this.priorityLabelID = priorityLabelID;
    }

    public String getPriorityLabelName() {
        return priorityLabelName;
    }

    public void setPriorityLabelName(String priorityLabelName) {
        this.priorityLabelName = priorityLabelName;
    }
}


