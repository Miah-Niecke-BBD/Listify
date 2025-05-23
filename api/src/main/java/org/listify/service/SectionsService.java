package org.listify.service;

import org.listify.dto.SectionTaskDTO;
import org.listify.dto.UpdateSectionDTO;
import org.listify.dto.UpdateSectionPositionDTO;
import org.listify.exception.BadRequestException;
import org.listify.exception.ForbiddenException;
import org.listify.exception.NotFoundException;
import org.listify.model.Sections;
import org.listify.model.Tasks;
import org.listify.repo.SectionsRepository;
import org.listify.repo.TasksRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SectionsService {

    private final SectionsRepository repository;

    private final TasksRepository tasksRepository;

    public SectionsService(SectionsRepository repository, TasksRepository tasksRepository) {
        this.repository = repository;
        this.tasksRepository = tasksRepository;
    }

    public Sections getSectionById(Long id) {
        return repository.findById(id)
                .orElseThrow();
    }

    public List<SectionTaskDTO> getTasksBySectionId(Long sectionID, Long userID) {
        Integer userAccessToTask = repository.userHasAccessToSection(userID, sectionID);
        if (userAccessToTask == null || userAccessToTask == 0) {
            throw new ForbiddenException("User does not have access to this section");
        }

        List<Tasks> tasks = repository.findTasksBySectionID(sectionID);
        if (tasks.isEmpty()) {
            throw new NotFoundException("There are no tasks in this section");
        }

        List<SectionTaskDTO> taskDTOs = new ArrayList<>();
        for (Tasks task : tasks) {
            SectionTaskDTO taskDTO = mapTaskToSectionTaskDTO(task);
            taskDTOs.add(taskDTO);
        }
        return taskDTOs;
    }

    public Long createSection(Long teamLeaderID, Long projectID,
                              String sectionName, Byte sectionPosition) {
        if (sectionName.length() > 100) {
            throw new BadRequestException("Section name has a maximum of 100 characters");
        }
        repository.createSection(teamLeaderID, projectID, sectionName, sectionPosition);

        Sections newlyCreatedSection = repository.findTopOrderBySectionIDDesc();
        return newlyCreatedSection != null ? newlyCreatedSection.getSectionID() : null;
    }

    public void updateSection(Long sectionID, Long userID, UpdateSectionDTO updatedSection) {
        if (updatedSection.getSectionName().length() > 100) {
            throw new BadRequestException("Section name has a maximum of 100 characters");
        }

        Integer userAccessToTask = repository.userHasAccessToSection(userID, sectionID);
        if (userAccessToTask == null || userAccessToTask == 0) {
            throw new ForbiddenException("Only sections within your project can be updated.");
        }
        repository.updateSection(sectionID, userID, updatedSection.getSectionName());
    }

    public void deleteSectionById(Long teamLeaderID, Long sectionID) {
        Integer userAccessToTask = repository.userHasAccessToSection(teamLeaderID, sectionID);
        if (userAccessToTask == null || userAccessToTask == 0) {
            throw new ForbiddenException("User does not have access to this section");
        }
        repository.deleteSectionById(teamLeaderID, sectionID);
    }

    public void updateSectionPosition(Long sectionID, Long loggedInUserID, UpdateSectionPositionDTO updatedPosition) {
        Integer userAccessToTask = repository.userHasAccessToSection(loggedInUserID, sectionID);
        if (userAccessToTask == null || userAccessToTask == 0) {
            throw new ForbiddenException("Only sections within your project can be updated.");
        }
        repository.updateSectionPosition(loggedInUserID, sectionID, updatedPosition.getSectionPosition());
    }

    private SectionTaskDTO mapTaskToSectionTaskDTO(Tasks task) {
        String priorityLabelName = tasksRepository.getPriorityLabelNameByTaskID(task.getTaskID());
        return new SectionTaskDTO(
              task.getTaskID(),
              task.getTaskName(),
                priorityLabelName,
              task.getParentTaskID(),
              task.getTaskPosition(),
              task.getDateCompleted(),
              task.getCreatedAt(),
              task.getDueDate()
        );
    }
}
