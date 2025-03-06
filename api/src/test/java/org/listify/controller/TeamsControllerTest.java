package org.listify.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.listify.dto.DueTasksDTO;
import org.listify.dto.TeamInfoDTO;
import org.listify.dto.TeamMemberInfoDTO;
import org.listify.dto.TeamProjectsDTO;
import org.listify.model.Teams;
import org.listify.repo.TeamsRepository;
import org.listify.service.TeamsService;
import org.listify.service.UserService;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TeamsControllerTest {
    @Autowired
    private TeamsRepository teamsRepository;

    @Mock
    private TeamsService teamService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TeamsController teamsController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teamsController).build();

        Authentication mockAuthentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(userService.getUserIDFromAuthentication(mockAuthentication)).thenReturn(1L);
    }

    @Test
    public void testGetAllTeams() throws Exception {
        Teams teamA = new Teams(1L, "Team A", LocalDateTime.now(), LocalDateTime.now());
        Teams teamB = new Teams(2L, "Team B", LocalDateTime.now(), LocalDateTime.now());

        when(teamService.getAllUserTeams(1L)).thenReturn(List.of(teamA, teamB));

        mockMvc.perform(MockMvcRequestBuilders.get("/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teamID").value(1))
                .andExpect(jsonPath("$[0].teamName").value("Team A"))
                .andExpect(jsonPath("$[1].teamID").value(2))
                .andExpect(jsonPath("$[1].teamName").value("Team B"));

        verify(teamService, times(1)).getAllUserTeams(1L);
    }

    @Test
    public void testAddTeam() throws Exception {
        Long userID = 1L;
        String teamName = "New Team";
        Long newTeamID = 3L;

        Teams createdTeam = new Teams(newTeamID, teamName, null, null);
        when(teamService.addTeam(userID, teamName)).thenReturn(newTeamID);
        when(teamService.findATeamByUserID(userID, newTeamID)).thenReturn(createdTeam);

        mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .param("teamName", teamName))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.teamID").value(newTeamID))
                .andExpect(jsonPath("$.teamName").value(teamName));

        verify(userService, times(1)).getUserIDFromAuthentication(any());
        verify(teamService, times(1)).addTeam(userID, teamName);
        verify(teamService, times(1)).findATeamByUserID(userID, newTeamID);
    }

    @Test
    public void testGetTeamById() throws Exception {
        LocalDateTime createdAt = LocalDateTime.parse("2025-03-05T11:23:09.622836600");

        when(teamService.findATeamByUserID(1L, 1L)).thenReturn(
                new Teams(1L, "Team A", createdAt, null));

        mockMvc.perform(MockMvcRequestBuilders.get("/teams/{teamID}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamID").doesNotExist())
                .andExpect(jsonPath("$.teamName").value("Team A"))
                .andExpect(jsonPath("$.createdAt").value("2025-03-05 11:23:09.6228366"))
                .andExpect(jsonPath("$.updatedAt").doesNotExist());


        verify(userService, times(1)).getUserIDFromAuthentication(any());
        verify(teamService, times(1)).findATeamByUserID(1L, 1L);
    }

    @Test
    public void testDeleteTeam() throws Exception {
        doNothing().when(teamService).deleteTeam(1L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/teams/{teamID}", 1L))
                .andExpect(status().isNoContent());

        verify(teamService, times(1)).deleteTeam(1L, 1L);
    }

    @Test
    public void testUpdateTeam() throws Exception {
        LocalDateTime createdAt = LocalDateTime.parse("2025-03-05T11:23:09.622836600");
        LocalDateTime updatedAt = LocalDateTime.now();

        when(teamService.updateTeamDetails(1L, 3L, "New team name")).thenReturn(new TeamInfoDTO("New team name", createdAt, updatedAt));

        mockMvc.perform(MockMvcRequestBuilders.put("/teams/{teamID}", 3L)
                        .param("newTeamName", "New team name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName").value("New team name"))
                .andExpect(jsonPath("$.createdAt").value("2025-03-05 11:23:09.6228366"));

        verify(teamService, times(1)).updateTeamDetails(1L, 3L, "New team name");
    }

    @Test
    public void testGetTeamMembersByTeamId() throws Exception {
        TeamMemberInfoDTO teamMemberA = new TeamMemberInfoDTO(1L, "01234", true);
        TeamMemberInfoDTO teamMemberB = new TeamMemberInfoDTO(4L, "56789", false);

        when(teamService.getTeamMembersByTeamID(3L, 1L)).thenReturn(List.of(teamMemberA, teamMemberB));

        mockMvc.perform(MockMvcRequestBuilders.get("/teams/{teamID}/members", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userID").value(1))
                .andExpect(jsonPath("$[1].userID").value(4))
                .andExpect(jsonPath("$[0].githubID").value("01234"))
                .andExpect(jsonPath("$[1].githubID").value("56789"))
                .andExpect(jsonPath("$[0].teamLeader").value(true))
                .andExpect(jsonPath("$[1].teamLeader").value(false));

        verify(teamService, times(1)).getTeamMembersByTeamID(3L, 1L);
    }

    @Test
    public void testAssignMemberToTeam() throws Exception {
        TeamMemberInfoDTO teamMemberA = new TeamMemberInfoDTO(1L, "01234", true);
        TeamMemberInfoDTO teamMemberB = new TeamMemberInfoDTO(4L, "56789", false);

        doNothing().when(teamService).assignMemberToTeam(1L, "56789", 3L);
        when(teamService.getTeamMembersByTeamID(3L, 1L)).thenReturn(List.of(teamMemberA, teamMemberB));

        mockMvc.perform(MockMvcRequestBuilders.post("/teams/{teamID}/members", 3L)
                        .param("githubID", "56789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userID").value(1))
                .andExpect(jsonPath("$[1].userID").value(4))
                .andExpect(jsonPath("$[0].githubID").value("01234"))
                .andExpect(jsonPath("$[1].githubID").value("56789"))
                .andExpect(jsonPath("$[0].teamLeader").value(true))
                .andExpect(jsonPath("$[1].teamLeader").value(false));

        verify(teamService, times(1)).assignMemberToTeam(1L, "56789", 3L);
        verify(teamService, times(1)).getTeamMembersByTeamID(3L, 1L);
    }

    @Test
    public void testDeleteMemberFromTeam() throws Exception {
        doNothing().when(teamService).deleteMemberFromTeam("56789", 3L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/teams/{teamID}/members", 3L)
                        .param("githubID", "56789"))
                .andExpect(status().isNoContent());

        verify(teamService, times(1)).deleteMemberFromTeam("56789", 3L, 1L);
    }

    @Test
    public void testUpdateTeamLeader() throws Exception {
        doNothing().when(teamService).updateTeamLeader(1L, 3L, "56789");

        mockMvc.perform(MockMvcRequestBuilders.put("/teams/{teamID}/members", 3L)
                        .param("newTeamLeaderGithubID", "56789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Team leader for team 3 successfully updated to user 56789"));

        verify(teamService, times(1)).updateTeamLeader(1L, 3L, "56789");
    }

    @Test
    public void testFindProjectsByTeamID() throws Exception {
        TeamProjectsDTO projectA = new TeamProjectsDTO(4L, "First-project");
        TeamProjectsDTO projectB = new TeamProjectsDTO(5L, "Second-project");

        when(teamService.findProjectsByTeamID(1L, 3L)).thenReturn(List.of(projectA, projectB));

        mockMvc.perform(MockMvcRequestBuilders.get("/teams/{teamID}/projects", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projectID").value(4))
                .andExpect(jsonPath("$[1].projectID").value(5))
                .andExpect(jsonPath("$[0].projectName").value("First-project"))
                .andExpect(jsonPath("$[1].projectName").value("Second-project"));

        verify(teamService, times(1)).findProjectsByTeamID(1L, 3L);
    }

    @Test
    public void testFindTeamsDueTasks() throws Exception {
        LocalDateTime dueDate = LocalDateTime.parse("2025-04-05T11:23:09.622836600");

        DueTasksDTO taskA = new DueTasksDTO(10L, "Complete controller", dueDate);
        DueTasksDTO taskB = new DueTasksDTO(20L, "Complete controller tests", dueDate);

        when(teamService.findTeamsDueTasks(1L, 3L)).thenReturn(List.of(taskA, taskB));

        mockMvc.perform(MockMvcRequestBuilders.get("/teams/{teamID}/dueTasks", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskID").value(10L))
                .andExpect(jsonPath("$[1].taskID").value(20L))
                .andExpect(jsonPath("$[0].taskName").value("Complete controller"))
                .andExpect(jsonPath("$[1].taskName").value("Complete controller tests"))
                .andExpect(jsonPath("$[0].dueDate").value("2025-04-05 11:23:09.6228366"))
                .andExpect(jsonPath("$[1].dueDate").value("2025-04-05 11:23:09.6228366"));


    }
}
