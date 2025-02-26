package org.setup.Listify.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Sections", schema = "listify")
public class Sections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionID;

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
        this.sectionID = sectionID;
        this.projectID = projectID;
        this.sectionName = sectionName;
        this.sectionPosition = sectionPosition;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Section{" +
                "sectionID=" + sectionID +
                ", projectID=" + projectID +
                ", sectionName='" + sectionName + '\'' +
                ", sectionPosition=" + sectionPosition +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
