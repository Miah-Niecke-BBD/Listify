package org.setup.listify.controller;

import org.setup.listify.dto.ProjectsAndTasks;
import org.setup.listify.service.TeamCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.setup.listify.service.UserService;

@RestController
@RequestMapping("/teams/{teamID}/calendar")
public class CalendarController {
    @Autowired
    private TeamCalendarService teamCalendarService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Object> findAllTasksDue(@PathVariable("teamID") Long teamID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userID = userService.getUserIDFromAuthentication(authentication);
        ProjectsAndTasks projectsAndTasks = teamCalendarService.findTeamsDueTasks(userID, teamID, null);
        return ResponseEntity.ok().body(projectsAndTasks);
    }

    @GetMapping("/project/{projectID}")
    public ResponseEntity<Object> findAllProjectTasksDue(@PathVariable("teamID") Long teamID, @PathVariable("projectID") Long projectID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userID = userService.getUserIDFromAuthentication(authentication);
        ProjectsAndTasks projectsAndTasks = teamCalendarService.findTeamsDueTasks(userID, teamID, projectID);
        return ResponseEntity.ok().body(projectsAndTasks);
    }
}
