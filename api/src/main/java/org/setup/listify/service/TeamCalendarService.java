package org.setup.listify.service;

import org.setup.listify.exception.ListNotFoundException;
import org.setup.listify.model.Tasks;
import org.setup.listify.repo.TeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamCalendarService {
    @Autowired
    private TeamsRepository teamsRepository;

    @Autowired
    private TeamsService  teamsService;

    @Autowired
    private UserService userService;

    public List<Tasks> findTeamsDueTasks(Long teamId, Long userID, Long projectID) {
        teamsService.getTeamById(teamId);
        List<Tasks> tasks = teamsRepository.findTeamsDueTasks(teamId, userID, projectID);

        if (tasks.isEmpty()) {
            throw new ListNotFoundException("tasks");
        }
        return tasks;
    }

}
