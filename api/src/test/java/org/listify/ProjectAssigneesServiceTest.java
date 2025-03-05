//package org.listify;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.listify.dto.ProjectAssigneeDTO;
//import org.listify.model.ProjectAssignees;
//import org.listify.repo.ProjectAssigneesRepository;
//import org.listify.repo.ProjectsRepository;
//import org.listify.service.ProjectAssigneesService;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class ProjectAssigneesServiceTest {
//
//    @Mock
//    private ProjectAssigneesRepository projectAssigneesRepository;
//
//    @Mock
//    private ProjectsRepository projectsRepository;
//
//    private ProjectAssigneesService projectAssigneesService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        projectAssigneesService = new ProjectAssigneesService(projectAssigneesRepository, projectsRepository);
//    }
//
//    @Test
//    public void testGetAllProjectAssignees_Success() {
//        // Given
//        Long userID = 9L;
//        ProjectAssignees assignee = new ProjectAssignees();
//        assignee.setProjectAssigneeID(1L);
//        assignee.setUserID(userID);
//        assignee.setProjectID(10L);
//        assignee.setGithubID("user9_github");  // Set the githubID for the assignee
//        assignee.setProjectName("Project 10");
//
//        // When
//        when(projectAssigneesRepository.findAll()).thenReturn(List.of(assignee));
//
//        // Then
//        List<ProjectAssigneeDTO> assignees = projectAssigneesService.getAllProjectAssignees(userID);
//        assertNotNull(assignees);
//        assertEquals(1, assignees.size());
//        assertEquals(10L, assignees.get(0).getProjectID());
//        assertEquals("user9_github", assignees.get(0).getGithubID());  // Check that githubID is correctly mapped
//        assertEquals("Project 10", assignees.get(0).getProjectName());  // Check that projectName is correctly mapped
//    }
//
//
//    @Test
//    public void testGetAllProjectAssignees_EmptyList() {
//        // Given
//        when(projectAssigneesRepository.findAll()).thenReturn(List.of());
//
//        // Then
//        assertThrows(ResourceNotFoundException.class, () -> projectAssigneesService.getAllProjectAssignees(9L));
//    }
//
//    @Test
//    public void testGetProjectAssigneeById_Success() {
//        // Given
//        ProjectAssignees assignee = new ProjectAssignees();
//        assignee.setProjectAssigneeID(1L);
//        assignee.setUserID(9L);
//        assignee.setProjectID(10L);
//
//        // When
//        when(projectAssigneesRepository.findById(1L)).thenReturn(Optional.of(assignee));
//
//        // Then
//        ProjectAssigneeDTO result = projectAssigneesService.getProjectAssigneeById(1L);
//        assertNotNull(result);
//        assertEquals(10L, result.getProjectID());
//        assertEquals("user9_github", result.getGithubID());
//    }
//
//    @Test
//    public void testGetProjectAssigneeById_NotFound() {
//
//        when(projectAssigneesRepository.findById(1L)).thenReturn(Optional.empty());
//
//
//        assertThrows(ResourceNotFoundException.class, () -> projectAssigneesService.getProjectAssigneeById(1L));
//    }
//
//    @Test
//    public void testAssignUserToProject_Success() {
//
//        Long teamLeaderID = 9L;
//        Long userID = 10L;
//        Long projectID = 1L;
//
//        // If assignUserToProject is void, use doNothing() instead of thenReturn()
//        doNothing().when(projectAssigneesRepository).assignUserToProject(teamLeaderID, userID, projectID);
//
//        ProjectAssignees assignee = new ProjectAssignees();
//        assignee.setProjectAssigneeID(1L);
//        when(projectAssigneesRepository.findTopOrderByProjectAssigneeIDDesc()).thenReturn(assignee);
//
//        Long result = projectAssigneesService.assignUserToProject(teamLeaderID, userID, projectID);
//        assertNotNull(result);
//        assertEquals(1L, result);
//    }
//
//    @Test
//    public void testDeleteUserFromProject_Success() {
//        Long userID = 9L;
//        Long projectID = 10L;
//        Long teamLeaderID = 1L;
//
//        doNothing().when(projectAssigneesRepository).deleteUserFromProject(userID, projectID, teamLeaderID);
//
//        projectAssigneesService.deleteUserFromProject(userID, projectID, teamLeaderID);
//        verify(projectAssigneesRepository, times(1)).deleteUserFromProject(userID, projectID, teamLeaderID);
//    }
//}
