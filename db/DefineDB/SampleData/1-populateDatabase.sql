USE ListifyDB

INSERT INTO [listify].[Users] ([gitHubID], [createdAt], [updatedAt])
VALUES 
  ('user1_github', '2023-01-15', '2023-01-15'),
  ('user2_github', '2023-02-20', '2023-02-20'),
  ('user3_github', '2023-03-25', '2023-03-25'),
  ('user4_github', '2023-04-30', '2023-04-30'),
  ('user5_github', '2023-05-10', '2023-05-10'),
  ('user6_github', '2023-06-05', '2023-06-05'),
  ('user7_github', '2023-07-18', '2023-07-18'),
  ('user8_github', '2023-08-22', '2023-08-22'),
  ('user9_github', '2023-09-17', '2023-09-17'),
  ('user10_github', '2023-10-02', '2023-10-02'),
  ('user11_github', '2023-11-09', '2023-11-09'),
  ('user12_github', '2023-12-01', '2023-12-01'),
  ('user13_github', '2024-01-14', '2024-01-14'),
  ('user14_github', '2024-02-18', '2024-02-18'),
  ('user15_github', '2024-03-25', '2024-03-25');


INSERT INTO [listify].[Teams] ([teamName], [createdAt], [updatedAt])
VALUES 
  ('My Projects', '2023-01-15', '2023-01-15'), -- for user 1
  ('My Projects', '2023-02-20', '2023-02-20'), -- for user 2
  ('My Projects', '2023-03-25', '2023-03-25'), -- for user 3
  ('My Projects', '2023-04-30', '2023-04-30'), -- for user 4
  ('My Projects', '2023-05-10', '2023-05-10'), -- for user 5
  ('My Projects', '2023-06-05', '2023-06-05'), -- for user 6
  ('My Projects', '2023-07-18', '2023-07-18'), -- for user 7
  ('My Projects', '2023-08-22', '2023-08-22'), -- for user 8
  ('My Projects', '2023-09-17', '2023-09-17'), -- for user 9
  ('My Projects', '2023-10-02', '2023-10-02'), -- for user 10
  ('My Projects', '2023-11-09', '2023-11-09'), -- for user 11
  ('My Projects', '2023-12-01', '2023-12-01'), -- for user 12
  ('My Projects', '2024-01-14', '2024-01-14'), -- for user 13
  ('My Projects', '2024-02-18', '2024-02-18'), -- for user 14
  ('My Projects', '2024-03-25', '2024-03-25'); -- for user 15



INSERT INTO [listify].[Projects] ([teamID], [projectName], [projectDescription], [createdAt], [updatedAt])
VALUES
  (1, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (1, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (2, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (2, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (3, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (3, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (4, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (4, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (5, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (5, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (6, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (6, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (7, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (7, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (8, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (8, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (9, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (9, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (10, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (10, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (11, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (11, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (12, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (12, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (13, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (13, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (14, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (14, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (15, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (15, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE());

INSERT INTO [listify].[Sections] ([projectID], [sectionName], [sectionPosition], [createdAt], [updatedAt])
VALUES 
  (1, 'To Do', 0, GETDATE(), GETDATE()),
  (2, 'To Do', 0, GETDATE(), GETDATE()),
  (3, 'To Do', 0, GETDATE(), GETDATE()),
  (4, 'To Do', 0, GETDATE(), GETDATE()),
  (5, 'To Do', 0, GETDATE(), GETDATE()),
  (6, 'To Do', 0, GETDATE(), GETDATE()),
  (7, 'To Do', 0, GETDATE(), GETDATE()),
  (8, 'To Do', 0, GETDATE(), GETDATE()),
  (9, 'To Do', 0, GETDATE(), GETDATE()),
  (10, 'To Do', 0, GETDATE(), GETDATE()),
  (11, 'To Do', 0, GETDATE(), GETDATE()),
  (12, 'To Do', 0, GETDATE(), GETDATE()),
  (13, 'To Do', 0, GETDATE(), GETDATE()),
  (14, 'To Do', 0, GETDATE(), GETDATE()),
  (15, 'To Do', 0, GETDATE(), GETDATE());


INSERT INTO [listify].[Tasks] ([sectionID], [taskName], [taskDescription], [taskPriority], [taskPosition], [createdAt], [updatedAt])
VALUES
  (1, 'Buy groceries', 'Buy milk, eggs, and bread', NULL, 0, GETDATE(), GETDATE()),
  (2, 'Clean the house', 'Vacuum and mop the floors', NULL, 0, GETDATE(), GETDATE()),
  (3, 'Fix the car', 'Take the car to the mechanic', NULL, 0, GETDATE(), GETDATE()),
  (4, 'Work on website', 'Complete homepage layout', NULL, 0, GETDATE(), GETDATE()),
  (5, 'Plan vacation', 'Book tickets for the trip', NULL, 0, GETDATE(), GETDATE()),
  (6, 'Organize closet', 'Sort through clothes', NULL, 0, GETDATE(), GETDATE()),
  (7, 'Prepare meeting notes', 'Prepare agenda for the meeting', NULL, 0, GETDATE(), GETDATE()),
  (8, 'Update resume', 'Make edits to resume and LinkedIn profile', NULL, 0, GETDATE(), GETDATE()),
  (9, 'Read book', 'Finish reading the book', NULL, 0, GETDATE(), GETDATE()),
  (10, 'Complete project', 'Work on the final presentation', NULL, 0, GETDATE(), GETDATE()),
  (11, 'Write blog post', 'Publish a new article on the blog', NULL, 0, GETDATE(), GETDATE()),
  (12, 'Buy new laptop', 'Check reviews and prices', NULL, 0, GETDATE(), GETDATE()),
  (13, 'Study for exam', 'Prepare notes for the exam', NULL, 0, GETDATE(), GETDATE()),
  (14, 'Declutter workspace', 'Clear the desk and organize files', NULL, 0, GETDATE(), GETDATE()),
  (15, 'Bake cookies', 'Make chocolate chip cookies', NULL, 0, GETDATE(), GETDATE());


INSERT INTO [listify].[TeamMembers] ([userID], [teamID], [isTeamLeader])
VALUES
  (1, 1, 1), -- user 1
  (2, 2, 1), -- user 2
  (3, 3, 1), -- user 3
  (4, 4, 1), -- user 4
  (5, 5, 1), -- user 5
  (6, 6, 1), -- user 6
  (7, 7, 1), -- user 7
  (8, 8, 1), -- user 8
  (9, 9, 1), -- user 9
  (10, 10, 1), -- user 10
  (11, 11, 1), -- user 11
  (12, 12, 1), -- user 12
  (13, 13, 1), -- user 13
  (14, 14, 1), -- user 14
  (15, 15, 1); -- user 15



INSERT INTO [listify].[ProjectAssignees] ([userID], [projectID])
VALUES
  (1, 1), -- user 1 to "My List"
  (1, 2), -- user 1 to "Home"
  (2, 3), -- user 2 to "My List"
  (2, 4), -- user 2 to "Home"
  (3, 5), -- user 3 to "My List"
  (3, 6), -- user 3 to "Home"
  (4, 7), -- user 4 to "My List"
  (4, 8), -- user 4 to "Home"
  (5, 9), -- user 5 to "My List"
  (5, 10), -- user 5 to "Home"
  (6, 11), -- user 6 to "My List"
  (6, 12), -- user 6 to "Home"
  (7, 13), -- user 7 to "My List"
  (7, 14), -- user 7 to "Home"
  (8, 15), -- user 8 to "My List"
  (8, 16), -- user 8 to "Home"
  (9, 17), -- user 9 to "My List"
  (9, 18), -- user 9 to "Home"
  (10, 19), -- user 10 to "My List"
  (10, 20), -- user 10 to "Home"
  (11, 21), -- user 11 to "My List"
  (11, 22), -- user 11 to "Home"
  (12, 23), -- user 12 to "My List"
  (12, 24), -- user 12 to "Home"
  (13, 25), -- user 13 to "My List"
  (13, 26), -- user 13 to "Home"
  (14, 27), -- user 14 to "My List"
  (14, 28), -- user 14 to "Home"
  (15, 29), -- user 15 to "My List"
  (15, 30); -- user 15 to "Home"



INSERT INTO [listify].[Teams] (teamName, createdAt, updatedAt) VALUES
('Dev Team', '2023-12-01', '2023-12-01');

DECLARE @DevTeamID INT = SCOPE_IDENTITY();

INSERT INTO [listify].[TeamMembers] (userID, teamID, isTeamLeader) VALUES
(1, @DevTeamID, 1), -- Team Leader
(2, @DevTeamID, 0),
(3, @DevTeamID, 0);

INSERT INTO [listify].[Projects] (teamID, projectName, projectDescription, createdAt, updatedAt) VALUES
(@DevTeamID, 'Task App', 'A simple task management application.', '2024-01-10', '2024-01-10');

DECLARE @TaskAppProjectID INT = SCOPE_IDENTITY();

INSERT INTO [listify].[ProjectAssignees] (userID, projectID) VALUES
(1, @TaskAppProjectID),
(2, @TaskAppProjectID),
(3, @TaskAppProjectID);

INSERT INTO [listify].[Sections] (projectID, sectionName, sectionPosition, createdAt, updatedAt) VALUES
(@TaskAppProjectID, 'Development', 0, '2024-01-10', '2024-01-10');

DECLARE @DevelopmentSectionID INT = SCOPE_IDENTITY();

INSERT INTO [listify].[Tasks] (sectionID, parentTaskID, taskName, taskDescription, taskPriority, taskPosition, dueDate, createdAt, updatedAt) VALUES
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

INSERT INTO [listify].[TaskAssignees] (userID, taskID) VALUES
(1, @Task1),
(1, @Task6),
(2, @Task2),
(2, @Task7),
(3, @Task3),
(3, @Task8);


INSERT INTO [listify].[TaskDependencies] (taskID, dependentTaskID) VALUES
(@Task6, @Task1), 
(@Task7, @Task2), 
(@Task8, @Task3); 


INSERT INTO [listify].[Projects] (teamID, projectName, projectDescription, createdAt, updatedAt)
VALUES ((SELECT teamID FROM [listify].[Teams] WHERE teamName = 'Dev Team'), 'Deployment', 'Project for deploying applications to production', '2024-01-30', '2024-01-30');

DECLARE @DeploymentProjectID INT = (SELECT projectID FROM [Projects] WHERE projectName = 'Deployment');

INSERT INTO [listify].[Sections] (projectID, sectionName, sectionPosition, createdAt, updatedAt)
VALUES
(@DeploymentProjectID, 'Pre-Deployment Checks', 1, '2024-01-30', '2024-01-30'),
(@DeploymentProjectID, 'Deployment Execution', 2, '2024-01-30', '2024-01-30'),
(@DeploymentProjectID, 'Post-Deployment Validation', 3, '2024-01-30', '2024-01-30');

DECLARE @PreDeploymentSectionID INT = (SELECT sectionID FROM [listify].[Sections] WHERE sectionName = 'Pre-Deployment Checks' AND projectID = @DeploymentProjectID);
DECLARE @DeploymentExecutionSectionID INT = (SELECT sectionID FROM [listify].[Sections] WHERE sectionName = 'Deployment Execution' AND projectID = @DeploymentProjectID);
DECLARE @PostDeploymentValidationSectionID INT = (SELECT sectionID FROM [listify].[Sections] WHERE sectionName = 'Post-Deployment Validation' AND projectID = @DeploymentProjectID);

INSERT INTO [listify].[Tasks] (sectionID, taskName, taskDescription, taskPriority, taskPosition, dueDate, createdAt, updatedAt)
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

INSERT INTO [listify].[ProjectAssignees] (userID, projectID)
SELECT userID, @DeploymentProjectID FROM [listify].[ProjectAssignees] WHERE projectID = (SELECT projectID FROM [listify].[Projects] WHERE projectName = 'Task App');

DECLARE @UserID INT;
DECLARE user_cursor CURSOR FOR
SELECT userID FROM [listify].[ProjectAssignees] WHERE projectID = @DeploymentProjectID;

OPEN user_cursor;
FETCH NEXT FROM user_cursor INTO @UserID;

WHILE @@FETCH_STATUS = 0
BEGIN
    INSERT INTO [listify].[TaskAssignees] (userID, taskID)
    SELECT TOP 4 @UserID, taskID FROM [listify].[Tasks] WHERE sectionID IN (@PreDeploymentSectionID, @DeploymentExecutionSectionID, @PostDeploymentValidationSectionID) ORDER BY NEWID();
    FETCH NEXT FROM user_cursor INTO @UserID;
END

CLOSE user_cursor;
DEALLOCATE user_cursor;


INSERT INTO [listify].[Teams] ([teamName], [createdAt], [updatedAt])
VALUES ('Human Resources', '2023-06-01', '2023-06-01');

DECLARE @teamID INT = SCOPE_IDENTITY();

INSERT INTO [listify].[TeamMembers] ([userID], [teamID], [isTeamLeader])
VALUES 
  (4, @teamID, 1),
  (5, @teamID, 0),
  (6, @teamID, 0),
  (7, @teamID, 0),
  (8, @teamID, 0),
  (9, @teamID, 0),
  (10, @teamID, 0);

INSERT INTO [listify].[Projects] ([teamID], [projectName], [projectDescription], [createdAt], [updatedAt])
VALUES (@teamID, 'Manage Staff', 'A project to oversee staff management processes.', '2023-06-01', '2023-06-01');

-- Get the project ID
DECLARE @projectID INT = SCOPE_IDENTITY();

INSERT INTO [listify].[Sections] ([projectID], [sectionName], [sectionPosition], [createdAt], [updatedAt])
VALUES 
  (@projectID, 'Recruitment', 0, '2023-06-01', '2023-06-01'),
  (@projectID, 'Training', 1, '2023-06-01', '2023-06-01'),
  (@projectID, 'Payroll & Benefits', 2, '2023-06-01', '2023-06-01'),
  (@projectID, 'Performance Management', 3, '2023-06-01', '2023-06-01');

-- Store section IDs
DECLARE @recruitment INT, @training INT, @payroll INT, @performance INT;
SELECT @recruitment = sectionID FROM [Sections] WHERE sectionName = 'Recruitment';
SELECT @training = sectionID FROM [Sections] WHERE sectionName = 'Training';
SELECT @payroll = sectionID FROM [Sections] WHERE sectionName = 'Payroll & Benefits';
SELECT @performance = sectionID FROM [Sections] WHERE sectionName = 'Performance Management';

-- Insert tasks for the project
INSERT INTO [listify].[Tasks] ([sectionID], [taskName], [taskDescription], [taskPriority], [taskPosition], [dueDate], [createdAt], [updatedAt])
VALUES 
  (@recruitment, 'Post Job Listings', 'Create and publish job descriptions.', 1, 0, '2023-06-08', '2023-06-01', '2023-06-01'),
  (@recruitment, 'Screen Resumes', 'Review submitted applications.', 2, 1, '2023-06-15', '2023-06-01', '2023-06-01'),
  (@recruitment, 'Conduct Interviews', 'Schedule and perform interviews.', 3, 2, NULL, '2023-06-01', '2023-06-01'),
  (@recruitment, 'Finalize Hiring', 'Make final hiring decisions.', 1, 3, '2023-06-22', '2023-06-01', '2023-06-01'),
  (@training, 'Onboarding Sessions', 'Conduct training for new hires.', 2, 0, NULL, '2023-06-01', '2023-06-01'),
  (@training, 'Compliance Training', 'Ensure employees complete mandatory training.', 3, 1, '2023-06-30', '2023-06-01', '2023-06-01'),
  (@training, 'Skill Development', 'Enroll employees in skill workshops.', 2, 2, NULL, '2023-06-01', '2023-06-01'),
  (@payroll, 'Process Payroll', 'Calculate and process monthly salaries.', 1, 0, '2023-06-11', '2023-06-01', '2023-06-01'),
  (@payroll, 'Review Benefits', 'Ensure benefits packages are up to date.', 3, 1, NULL, '2023-06-01', '2023-06-01'),
  (@payroll, 'Manage Taxes', 'Ensure payroll tax compliance.', 2, 2, '2023-06-20', '2023-06-01', '2023-06-01'),
  (@performance, 'Conduct Performance Reviews', 'Evaluate employee performance.', 1, 0, '2023-07-10', '2023-06-01', '2023-06-01'),
  (@performance, 'Update Promotion Criteria', 'Review and adjust promotion policies.', 3, 1, NULL, '2023-06-01', '2023-06-01'),
  (@performance, 'Handle Employee Complaints', 'Address workplace concerns.', 2, 2, '2023-06-26', '2023-06-01', '2023-06-01'),
  (@performance, 'Implement Feedback Mechanism', 'Improve feedback collection methods.', 2, 3, NULL, '2023-06-01', '2023-06-01');

INSERT INTO [listify].[TaskAssignees] ([userID], [taskID])
VALUES 
  (4, 1), (4, 2),
  (5, 3), (5, 4),
  (6, 5), (6, 6),
  (7, 7), (7, 8),
  (8, 9), (8, 10),
  (9, 11), (9, 12),
  (10, 13), (10, 14);

INSERT INTO [listify].[TaskDependencies] ([taskID], [dependentTaskID])
VALUES 
  (1, 2),
  (3, 4),
  (5, 6),
  (7, 8),
  (9, 10),
  (11, 12),
  (13, 14);

INSERT INTO [listify].[ProjectAssignees] ([userID], [projectID])
VALUES 
  (4, @projectID),
  (5, @projectID),
  (6, @projectID),
  (7, @projectID),
  (8, @projectID),
  (9, @projectID),
  (10, @projectID);