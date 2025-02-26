package org.setup.Listify.Model;

import jakarta.persistence.*;
<<<<<<< HEAD
import java.time.LocalDateTime;

@Entity
@Table(name = "Sections", schema = "listify")
=======

import java.time.LocalDateTime;

@Entity
>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c
public class Sections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionID;
<<<<<<< HEAD

    @Column(name = "projectID", nullable = false)
    private Long projectID;

    @Column(name = "sectionName", nullable = false, length = 100)
    private String sectionName;

    @Column(name = "sectionPosition", nullable = false)
    private int sectionPosition;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = true)
    private LocalDateTime updatedAt;

    protected Sections() {}

    public Sections(Long sectionID, Long projectID, String sectionName, int sectionPosition, LocalDateTime createdAt, LocalDateTime updatedAt) {
=======
    @Column(name = "projectID", nullable = false)
    private Long projectID;
    @Column(name = "sectionName", nullable = false, length = 100)
    private String sectionName;
    @Column(name = "sectionPosition", nullable = false)
    private int sectionPosition;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updatedAt", nullable = true)
    private LocalDateTime updatesAt;

    protected Sections() {};

    public Sections(Long sectionID, Long projectID, String sectionName, int sectionPosition, LocalDateTime createdAt, LocalDateTime updatesAt) {
>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c
        this.sectionID = sectionID;
        this.projectID = projectID;
        this.sectionName = sectionName;
        this.sectionPosition = sectionPosition;
        this.createdAt = createdAt;
<<<<<<< HEAD
        this.updatedAt = updatedAt;
=======
        this.updatesAt = updatesAt;
>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c
    }

    @Override
    public String toString() {
        return "Section{" +
                "sectionID=" + sectionID +
                ", projectID=" + projectID +
                ", sectionName='" + sectionName + '\'' +
                ", sectionPosition=" + sectionPosition +
                ", createdAt=" + createdAt +
<<<<<<< HEAD
                ", updatedAt=" + updatedAt +
                '}';
    }

}
=======
                ", updatedAt=" + updatesAt +
                '}';
    }

    public Long getSectionID() {
        return sectionID;
    }

    public void setSectionID(Long sectionID) {
        this.sectionID = sectionID;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getSectionPosition() {
        return sectionPosition;
    }

    public void setSectionPosition(int sectionPosition) {
        this.sectionPosition = sectionPosition;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatesAt() {
        return updatesAt;
    }

    public void setUpdatesAt(LocalDateTime updatesAt) {
        this.updatesAt = updatesAt;
    }
}

>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c
