
INSERT INTO Teams (teamName, createdAt, updatedAt) VALUES
('Dev Team', '2023-12-01', '2023-12-01');

DECLARE @DevTeamID INT = SCOPE_IDENTITY();

INSERT INTO TeamMembers (userID, teamID, isTeamLeader) VALUES
(1, @DevTeamID, 1), -- Team Leader
(2, @DevTeamID, 0),
(3, @DevTeamID, 0);

INSERT INTO Projects (teamID, projectName, projectDescription, createdAt, updatedAt) VALUES
(@DevTeamID, 'Task App', 'A simple task management application.', '2024-01-10', '2024-01-10');

DECLARE @TaskAppProjectID INT = SCOPE_IDENTITY();

INSERT INTO ProjectAssignees (userID, projectID) VALUES
(1, @TaskAppProjectID),
(2, @TaskAppProjectID),
(3, @TaskAppProjectID);

INSERT INTO Sections (projectID, sectionName, sectionPosition, createdAt, updatedAt) VALUES
(@TaskAppProjectID, 'Development', 1, '2024-01-10', '2024-01-10');

DECLARE @DevelopmentSectionID INT = SCOPE_IDENTITY();

INSERT INTO Tasks (sectionID, parentTaskID, taskName, taskDescription, taskPriority, taskPosition, dueDate, createdAt, updatedAt) VALUES
(@DevelopmentSectionID, NULL, 'Setup Project Repo', 'Initialize GitHub repository and project structure.', 1, 1, '2024-02-15', '2024-01-10', '2024-01-10'), -- Task 1
(@DevelopmentSectionID, NULL, 'Design Database Schema', 'Plan and design the database structure.', 1, 2, '2024-02-18', '2024-01-10', '2024-01-10'), -- Task 2
(@DevelopmentSectionID, NULL, 'Implement User Authentication', 'Create login and registration functionality.', 2, 3, '2024-02-22', '2024-01-10', '2024-01-10'), -- Task 3
(@DevelopmentSectionID, NULL, 'Develop Task CRUD API', 'Create REST API endpoints for task management.', 2, 4, '2024-02-25', '2024-01-10', '2024-01-10'), -- Task 4
(@DevelopmentSectionID, NULL, 'Build Frontend UI', 'Develop the frontend using HTML/CSS/JS.', 2, 5, '2024-02-28', '2024-01-10', '2024-01-10'), -- Task 5
(@DevelopmentSectionID, NULL, 'Integrate API with Frontend', 'Connect frontend UI with the backend API.', 3, 6, '2024-03-05', '2024-01-10', '2024-01-10'), -- Task 6
(@DevelopmentSectionID, NULL, 'Implement Notifications', 'Add real-time notifications for task updates.', 3, 7, '2024-03-10', '2024-01-10', '2024-01-10'), -- Task 7
(@DevelopmentSectionID, NULL, 'Testing & Debugging', 'Conduct unit and integration tests.', 3, 8, '2024-03-12', '2024-01-10', '2024-01-10'), -- Task 8
(@DevelopmentSectionID, NULL, 'Deployment', 'Deploy the application to a cloud server.', 3, 9, '2024-03-18', '2024-01-10', '2024-01-10'), -- Task 9
(@DevelopmentSectionID, NULL, 'Write Documentation', 'Document project structure and API usage.', 2, 10, '2024-03-25', '2024-01-10', '2024-01-10'); -- Task 10

DECLARE @Task1 INT = SCOPE_IDENTITY() - 9;
DECLARE @Task2 INT = @Task1 + 1;
DECLARE @Task3 INT = @Task1 + 2;
DECLARE @Task4 INT = @Task1 + 3;
DECLARE @Task5 INT = @Task1 + 4;
DECLARE @Task6 INT = @Task1 + 5;
DECLARE @Task7 INT = @Task1 + 6;
DECLARE @Task8 INT = @Task1 + 7;
DECLARE @Task9 INT = @Task1 + 8;
DECLARE @Task10 INT = @Task1 + 9;

INSERT INTO TaskAssignees (userID, taskID) VALUES
(1, @Task1),
(1, @Task6),
(2, @Task2),
(2, @Task7),
(3, @Task3),
(3, @Task8);


INSERT INTO TaskDependencies (taskID, dependentTaskID) VALUES
(@Task6, @Task1), 
(@Task7, @Task2), 
(@Task8, @Task3); 


INSERT INTO [Projects] (teamID, projectName, projectDescription, createdAt, updatedAt)
VALUES ((SELECT teamID FROM [Teams] WHERE teamName = 'Dev Team'), 'Deployment', 'Project for deploying applications to production', '2024-01-30', '2024-01-30');

DECLARE @DeploymentProjectID INT = (SELECT projectID FROM [Projects] WHERE projectName = 'Deployment');

INSERT INTO [Sections] (projectID, sectionName, sectionPosition, createdAt, updatedAt)
VALUES
(@DeploymentProjectID, 'Pre-Deployment Checks', 1, '2024-01-30', '2024-01-30'),
(@DeploymentProjectID, 'Deployment Execution', 2, '2024-01-30', '2024-01-30'),
(@DeploymentProjectID, 'Post-Deployment Validation', 3, '2024-01-30', '2024-01-30');

DECLARE @PreDeploymentSectionID INT = (SELECT sectionID FROM [Sections] WHERE sectionName = 'Pre-Deployment Checks' AND projectID = @DeploymentProjectID);
DECLARE @DeploymentExecutionSectionID INT = (SELECT sectionID FROM [Sections] WHERE sectionName = 'Deployment Execution' AND projectID = @DeploymentProjectID);
DECLARE @PostDeploymentValidationSectionID INT = (SELECT sectionID FROM [Sections] WHERE sectionName = 'Post-Deployment Validation' AND projectID = @DeploymentProjectID);

INSERT INTO [Tasks] (sectionID, taskName, taskDescription, taskPriority, taskPosition, dueDate, createdAt, updatedAt)
VALUES
(@PreDeploymentSectionID, 'Verify application dependencies', 'Ensure all required dependencies are installed.', 1, 1, '2024-02-10', '2024-01-30', '2024-01-30'),
(@PreDeploymentSectionID, 'Check server resource availability', 'Ensure the server has enough CPU and memory.', 2, 2, '2024-02-12', '2024-01-30', '2024-01-30'),
(@PreDeploymentSectionID, 'Prepare rollback strategy', 'Create rollback plans in case of failure.', 1, 3, '2024-02-14', '2024-01-30', '2024-01-30'),
(@PreDeploymentSectionID, 'Validate database schema changes', 'Ensure database migrations work correctly.', 3, 4, '2024-02-16', '2024-01-30', '2024-01-30'),

(@DeploymentExecutionSectionID, 'Deploy application to staging', 'Deploy to staging and verify.', 2, 1, '2024-02-20', '2024-01-30', '2024-01-30'),
(@DeploymentExecutionSectionID, 'Run automated tests', 'Execute automated test suite.', 1, 2, '2024-02-22', '2024-01-30', '2024-01-30'),
(@DeploymentExecutionSectionID, 'Deploy application to production', 'Deploy application to the production environment.', 3, 3, '2024-02-25', '2024-01-30', '2024-01-30'),
(@DeploymentExecutionSectionID, 'Monitor deployment logs', 'Check logs for errors post-deployment.', 2, 4, '2024-02-27', '2024-01-30', '2024-01-30'),

(@PostDeploymentValidationSectionID, 'Verify application functionality', 'Ensure core features work as expected.', 1, 1, '2024-03-01', '2024-01-30', '2024-01-30'),
(@PostDeploymentValidationSectionID, 'Check server health metrics', 'Review CPU, memory, and disk usage.', 2, 2, '2024-03-03', '2024-01-30', '2024-01-30'),
(@PostDeploymentValidationSectionID, 'Validate security compliance', 'Check for any security misconfigurations.', 3, 3, '2024-03-05', '2024-01-30', '2024-01-30'),
(@PostDeploymentValidationSectionID, 'Update deployment documentation', 'Document changes for future reference.', 1, 4, '2024-03-07', '2024-01-30', '2024-01-30');

INSERT INTO [ProjectAssignees] (userID, projectID)
SELECT userID, @DeploymentProjectID FROM [ProjectAssignees] WHERE projectID = (SELECT projectID FROM [Projects] WHERE projectName = 'Task App');

DECLARE @UserID INT;
DECLARE user_cursor CURSOR FOR
SELECT userID FROM [ProjectAssignees] WHERE projectID = @DeploymentProjectID;

OPEN user_cursor;
FETCH NEXT FROM user_cursor INTO @UserID;

WHILE @@FETCH_STATUS = 0
BEGIN
    INSERT INTO [TaskAssignees] (userID, taskID)
    SELECT TOP 4 @UserID, taskID FROM [Tasks] WHERE sectionID IN (@PreDeploymentSectionID, @DeploymentExecutionSectionID, @PostDeploymentValidationSectionID) ORDER BY NEWID();
    FETCH NEXT FROM user_cursor INTO @UserID;
END

CLOSE user_cursor;
DEALLOCATE user_cursor;
