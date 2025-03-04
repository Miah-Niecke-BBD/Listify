//package org.setup.listify.controller;
//
//import org.setup.listify.assembler.TasksModelAssembler;
//import org.setup.listify.model.Tasks;
//import org.setup.listify.service.TeamCalendarService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.hateoas.CollectionModel;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.setup.listify.service.UserService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/teams/{teamID}/calendar")
//public class CalendarController {
//    @Autowired
//    private TeamCalendarService teamCalendarService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private TasksModelAssembler tasksAssembler;
//
//    @GetMapping
//    public CollectionModel<EntityModel<Tasks>>findAllTasksDue(@PathVariable("teamID") Long teamID) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long userID = userService.getUserIDFromAuthentication(authentication);
//        List<Tasks> tasks = teamCalendarService.findTeamsDueTasks(teamID, userID, null);
//        return tasksAssembler.toCollectionModel(tasks);
//    }
//
//    @GetMapping("/project/{projectID}")
//    public CollectionModel<EntityModel<Tasks>>findAllProjectTasksDue(@PathVariable("teamID") Long teamID, @PathVariable("projectID") Long projectID) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long userID = userService.getUserIDFromAuthentication(authentication);
//        List<Tasks> tasks = teamCalendarService.findTeamsDueTasks(teamID, userID, projectID);
//        return tasksAssembler.toCollectionModel(tasks);
//    }
//}
