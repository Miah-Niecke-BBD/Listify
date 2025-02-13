-- Insert the new "Dev Team"
INSERT INTO Teams (teamName, createdAt, updatedAt) VALUES
('Dev Team', GETDATE(), GETDATE());

-- Get the teamID for "Dev Team" (Assuming it's the next ID after My Projects teams)
DECLARE @DevTeamID INT = SCOPE_IDENTITY();

-- Assign Users 1-3 to "Dev Team", with User 1 as the leader
INSERT INTO TeamMembers (userID, teamID, isTeamLeader) VALUES
(1, @DevTeamID, 1), -- Team Leader
(2, @DevTeamID, 0),
(3, @DevTeamID, 0);

-- Create "Task App" Project under "Dev Team"
INSERT INTO Projects (teamID, projectName, projectDescription, createdAt, updatedAt) VALUES
(@DevTeamID, 'Task App', 'A simple task management application.', GETDATE(), GETDATE());

-- Get projectID for "Task App"
DECLARE @TaskAppProjectID INT = SCOPE_IDENTITY();

-- Assign Users 1-3 to the "Task App" Project
INSERT INTO ProjectAssignees (userID, projectID) VALUES
(1, @TaskAppProjectID),
(2, @TaskAppProjectID),
(3, @TaskAppProjectID);

-- Create "Development" Section
INSERT INTO Sections (projectID, sectionName, sectionPosition, createdAt, updatedAt) VALUES
(@TaskAppProjectID, 'Development', 1, GETDATE(), GETDATE());

-- Get sectionID for "Development"
DECLARE @DevelopmentSectionID INT = SCOPE_IDENTITY();

-- Insert 10 tasks for the "Task App" Project
INSERT INTO Tasks (sectionID, parentTaskID, taskName, taskDescription, taskPriority, taskPosition, dueDate, createdAt, updatedAt) VALUES
(@DevelopmentSectionID, NULL, 'Setup Project Repo', 'Initialize GitHub repository and project structure.', 1, 1, '2025-02-15', GETDATE(), GETDATE()), -- Task 1
(@DevelopmentSectionID, NULL, 'Design Database Schema', 'Plan and design the database structure.', 1, 2, '2025-02-18', GETDATE(), GETDATE()), -- Task 2
(@DevelopmentSectionID, NULL, 'Implement User Authentication', 'Create login and registration functionality.', 2, 3, '2025-02-22', GETDATE(), GETDATE()), -- Task 3
(@DevelopmentSectionID, NULL, 'Develop Task CRUD API', 'Create REST API endpoints for task management.', 2, 4, NULL, GETDATE(), GETDATE()), -- Task 4
(@DevelopmentSectionID, NULL, 'Build Frontend UI', 'Develop the frontend using HTML/CSS/JS.', 2, 5, '2025-02-28', GETDATE(), GETDATE()), -- Task 5
(@DevelopmentSectionID, NULL, 'Integrate API with Frontend', 'Connect frontend UI with the backend API.', 3, 6, '2025-03-05', GETDATE(), GETDATE()), -- Task 6
(@DevelopmentSectionID, NULL, 'Implement Notifications', 'Add real-time notifications for task updates.', 3, 7, NULL, GETDATE(), GETDATE()), -- Task 7
(@DevelopmentSectionID, NULL, 'Testing & Debugging', 'Conduct unit and integration tests.', 3, 8, '2025-03-12', GETDATE(), GETDATE()), -- Task 8
(@DevelopmentSectionID, NULL, 'Deployment', 'Deploy the application to a cloud server.', 3, 9, '2025-03-18', GETDATE(), GETDATE()), -- Task 9
(@DevelopmentSectionID, NULL, 'Write Documentation', 'Document project structure and API usage.', 2, 10, NULL, GETDATE(), GETDATE()); -- Task 10

-- Capture task IDs
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

-- Assign tasks to users (Each user gets 2 tasks)
INSERT INTO TaskAssignees (userID, taskID) VALUES
(1, @Task1), -- User 1: Setup Project Repo
(1, @Task6), -- User 1: Integrate API with Frontend (Depends on Setup Project Repo)
(2, @Task2), -- User 2: Design Database Schema
(2, @Task7), -- User 2: Implement Notifications (Depends on Database Schema)
(3, @Task3), -- User 3: Implement User Authentication
(3, @Task8); -- User 3: Testing & Debugging (Depends on Authentication)

-- Define task dependencies
INSERT INTO TaskDependencies (taskID, dependentTaskID) VALUES
(@Task6, @Task1), -- "Integrate API with Frontend" depends on "Setup Project Repo"
(@Task7, @Task2), -- "Implement Notifications" depends on "Design Database Schema"
(@Task8, @Task3); -- "Testing & Debugging" depends on "User Authentication"



-- Insert new project "Deployment" under "Dev Team"
INSERT INTO [Projects] (teamID, projectName, projectDescription, createdAt, updatedAt)
VALUES ((SELECT teamID FROM [Teams] WHERE teamName = 'Dev Team'), 'Deployment', 'Project for deploying applications to production', GETDATE(), GETDATE());

-- Get the projectID of the newly created "Deployment" project
DECLARE @DeploymentProjectID INT = (SELECT projectID FROM [Projects] WHERE projectName = 'Deployment');

-- Insert sections into "Deployment" project
INSERT INTO [Sections] (projectID, sectionName, sectionPosition, createdAt, updatedAt)
VALUES
(@DeploymentProjectID, 'Pre-Deployment Checks', 1, GETDATE(), GETDATE()),
(@DeploymentProjectID, 'Deployment Execution', 2, GETDATE(), GETDATE()),
(@DeploymentProjectID, 'Post-Deployment Validation', 3, GETDATE(), GETDATE());

-- Get section IDs
DECLARE @PreDeploymentSectionID INT = (SELECT sectionID FROM [Sections] WHERE sectionName = 'Pre-Deployment Checks' AND projectID = @DeploymentProjectID);
DECLARE @DeploymentExecutionSectionID INT = (SELECT sectionID FROM [Sections] WHERE sectionName = 'Deployment Execution' AND projectID = @DeploymentProjectID);
DECLARE @PostDeploymentValidationSectionID INT = (SELECT sectionID FROM [Sections] WHERE sectionName = 'Post-Deployment Validation' AND projectID = @DeploymentProjectID);

-- Insert tasks related to deployment
INSERT INTO [Tasks] (sectionID, taskName, taskDescription, taskPriority, taskPosition, dueDate, createdAt, updatedAt)
VALUES
(@PreDeploymentSectionID, 'Verify application dependencies', 'Ensure all required dependencies are installed.', 1, 1, DATEADD(DAY, 2, GETDATE()), GETDATE(), GETDATE()),
(@PreDeploymentSectionID, 'Check server resource availability', 'Ensure the server has enough CPU and memory.', 2, 2, DATEADD(DAY, 3, GETDATE()), GETDATE(), GETDATE()),
(@PreDeploymentSectionID, 'Prepare rollback strategy', 'Create rollback plans in case of failure.', 1, 3, NULL, GETDATE(), GETDATE()),
(@PreDeploymentSectionID, 'Validate database schema changes', 'Ensure database migrations work correctly.', 3, 4, DATEADD(DAY, 1, GETDATE()), GETDATE(), GETDATE()),

(@DeploymentExecutionSectionID, 'Deploy application to staging', 'Deploy to staging and verify.', 2, 1, DATEADD(DAY, 5, GETDATE()), GETDATE(), GETDATE()),
(@DeploymentExecutionSectionID, 'Run automated tests', 'Execute automated test suite.', 1, 2, NULL, GETDATE(), GETDATE()),
(@DeploymentExecutionSectionID, 'Deploy application to production', 'Deploy application to the production environment.', 3, 3, DATEADD(DAY, 7, GETDATE()), GETDATE(), GETDATE()),
(@DeploymentExecutionSectionID, 'Monitor deployment logs', 'Check logs for errors post-deployment.', 2, 4, DATEADD(DAY, 8, GETDATE()), GETDATE(), GETDATE()),

(@PostDeploymentValidationSectionID, 'Verify application functionality', 'Ensure core features work as expected.', 1, 1, NULL, GETDATE(), GETDATE()),
(@PostDeploymentValidationSectionID, 'Check server health metrics', 'Review CPU, memory, and disk usage.', 2, 2, DATEADD(DAY, 9, GETDATE()), GETDATE(), GETDATE()),
(@PostDeploymentValidationSectionID, 'Validate security compliance', 'Check for any security misconfigurations.', 3, 3, NULL, GETDATE(), GETDATE()),
(@PostDeploymentValidationSectionID, 'Update deployment documentation', 'Document changes for future reference.', 1, 4, DATEADD(DAY, 10, GETDATE()), GETDATE(), GETDATE());

-- Assign project members from "Task App" to "Deployment"
INSERT INTO [ProjectAssignees] (userID, projectID)
SELECT userID, @DeploymentProjectID FROM [ProjectAssignees] WHERE projectID = (SELECT projectID FROM [Projects] WHERE projectName = 'Task App');

-- Assign each member 4 random tasks
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
