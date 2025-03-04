package org.setup.listify.service;

import org.setup.listify.dto.ProjectsAndTasks;
import org.setup.listify.model.Projects;
import org.setup.listify.model.Tasks;
import org.setup.listify.model.Teams;
import org.setup.listify.repo.TeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamCalendarService {
    @Autowired
    private TeamsRepository teamsRepository;

    @Autowired
    private TeamsService teamsService;

    @Autowired
    private UserService userService;

    public ProjectsAndTasks findTeamsDueTasks(Long userID, Long teamID, Long projectID) {
        Teams team = teamsService.findATeamByUserID(userID, teamID);

        List<Projects> projects = teamsRepository.findTeamProjects(userID, teamID);
        List<Tasks> tasks;

        if (team.getCreatedAt().equals(userService.getUserCreatedAt(userID))) {
            tasks = teamsRepository.findTeamsDueTasks(userID, null, projectID);
        } else {
            tasks = teamsRepository.findTeamsDueTasks(userID, teamID, projectID);
        }

        return new ProjectsAndTasks(projects, tasks);
    }
}
