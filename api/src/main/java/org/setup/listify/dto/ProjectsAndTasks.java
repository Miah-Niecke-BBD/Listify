package org.setup.listify.dto;

import org.setup.listify.model.Projects;
import org.setup.listify.model.Tasks;

import java.util.List;

public class ProjectsAndTasks {

    private List<Projects> projects;
    private List<Tasks> tasks;

    public ProjectsAndTasks(List<Projects> projects, List<Tasks> tasks) {
        this.projects = projects;
        this.tasks = tasks;
    }

    public List<Projects> getProjects() {
        return projects;
    }

    public void setProjects(List<Projects> projects) {
        this.projects = projects;
    }

    public List<Tasks> getTasks() {
        return tasks;
    }

    public void setTasks(List<Tasks> tasks) {
        this.tasks = tasks;
    }
}
