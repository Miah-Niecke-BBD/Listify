package org.setup.Listify.Service;

import org.setup.Listify.Model.Tasks;
import org.setup.Listify.Repo.TasksRepository;
import org.setup.Listify.exceptions.TaskNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TasksService {

    private final TasksRepository repository;

    public TasksService(TasksRepository repository) {
        this.repository = repository;
    }


    public List<Tasks> getAllTasks() {
        return repository.findAll();
    }


    public Tasks getTaskById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }


    public List<Tasks> getTaskBySectionId(Long sectionId) {
        return repository.findTasksBySectionID(sectionId);
    }


    public Tasks saveTask(Tasks newTask) {
        return repository.save(newTask);
    }


    public Tasks updateTask(Long id, Tasks updatedTask) {
        Tasks existingTask = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if (updatedTask.getTaskName() != null && !updatedTask.getTaskName().isEmpty()) {
            existingTask.setTaskName(updatedTask.getTaskName());
        }

        if (updatedTask.getTaskDescription() != null && !updatedTask.getTaskDescription().isEmpty()) {
            existingTask.setTaskDescription(updatedTask.getTaskDescription());
        }

        if (updatedTask.getTaskPriority() != null) {
            existingTask.setTaskPriority(updatedTask.getTaskPriority());
        }

        if (updatedTask.getTaskPosition() != null) {
            existingTask.setTaskPosition(updatedTask.getTaskPosition());
        }

        if (updatedTask.getDateCompleted() != null) {
            existingTask.setDateCompleted(updatedTask.getDateCompleted());
        }

        if (updatedTask.getDueDate() != null) {
            existingTask.setDueDate(updatedTask.getDueDate());
        }

        existingTask.setUpdatedAt(LocalDateTime.now());

        return repository.save(existingTask);
    }
}
