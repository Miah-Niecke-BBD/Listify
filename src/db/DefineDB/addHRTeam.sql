-- Insert the "Human Resources" team
INSERT INTO [Teams] ([teamName], [createdAt], [updatedAt])
VALUES ('Human Resources', GETDATE(), GETDATE());

-- Get the team ID
DECLARE @teamID INT = SCOPE_IDENTITY();

-- Assign users 4-10 to the team with User 4 as the team leader
INSERT INTO [TeamMembers] ([userID], [teamID], [isTeamLeader])
VALUES 
  (4, @teamID, 1),
  (5, @teamID, 0),
  (6, @teamID, 0),
  (7, @teamID, 0),
  (8, @teamID, 0),
  (9, @teamID, 0),
  (10, @teamID, 0);

-- Insert the "Manage Staff" project
INSERT INTO [Projects] ([teamID], [projectName], [projectDescription], [createdAt], [updatedAt])
VALUES (@teamID, 'Manage Staff', 'A project to oversee staff management processes.', GETDATE(), GETDATE());

-- Get the project ID
DECLARE @projectID INT = SCOPE_IDENTITY();

-- Insert project sections
INSERT INTO [Sections] ([projectID], [sectionName], [sectionPosition], [createdAt], [updatedAt])
VALUES 
  (@projectID, 'Recruitment', 1, GETDATE(), GETDATE()),
  (@projectID, 'Training', 2, GETDATE(), GETDATE()),
  (@projectID, 'Payroll & Benefits', 3, GETDATE(), GETDATE()),
  (@projectID, 'Performance Management', 4, GETDATE(), GETDATE());

-- Store section IDs
DECLARE @recruitment INT, @training INT, @payroll INT, @performance INT;
SELECT @recruitment = sectionID FROM [Sections] WHERE sectionName = 'Recruitment';
SELECT @training = sectionID FROM [Sections] WHERE sectionName = 'Training';
SELECT @payroll = sectionID FROM [Sections] WHERE sectionName = 'Payroll & Benefits';
SELECT @performance = sectionID FROM [Sections] WHERE sectionName = 'Performance Management';

-- Insert tasks for the project
INSERT INTO [Tasks] ([sectionID], [taskName], [taskDescription], [taskPriority], [taskPosition], [dueDate], [createdAt], [updatedAt])
VALUES 
  (@recruitment, 'Post Job Listings', 'Create and publish job descriptions.', 1, 1, DATEADD(DAY, 7, GETDATE()), GETDATE(), GETDATE()),
  (@recruitment, 'Screen Resumes', 'Review submitted applications.', 2, 2, DATEADD(DAY, 14, GETDATE()), GETDATE(), GETDATE()),
  (@recruitment, 'Conduct Interviews', 'Schedule and perform interviews.', 3, 3, NULL, GETDATE(), GETDATE()),
  (@recruitment, 'Finalize Hiring', 'Make final hiring decisions.', 1, 4, DATEADD(DAY, 21, GETDATE()), GETDATE(), GETDATE()),
  (@training, 'Onboarding Sessions', 'Conduct training for new hires.', 2, 1, NULL, GETDATE(), GETDATE()),
  (@training, 'Compliance Training', 'Ensure employees complete mandatory training.', 3, 2, DATEADD(DAY, 30, GETDATE()), GETDATE(), GETDATE()),
  (@training, 'Skill Development', 'Enroll employees in skill workshops.', 2, 3, NULL, GETDATE(), GETDATE()),
  (@payroll, 'Process Payroll', 'Calculate and process monthly salaries.', 1, 1, DATEADD(DAY, 10, GETDATE()), GETDATE(), GETDATE()),
  (@payroll, 'Review Benefits', 'Ensure benefits packages are up to date.', 3, 2, NULL, GETDATE(), GETDATE()),
  (@payroll, 'Manage Taxes', 'Ensure payroll tax compliance.', 2, 3, DATEADD(DAY, 20, GETDATE()), GETDATE(), GETDATE()),
  (@performance, 'Conduct Performance Reviews', 'Evaluate employee performance.', 1, 1, DATEADD(DAY, 40, GETDATE()), GETDATE(), GETDATE()),
  (@performance, 'Update Promotion Criteria', 'Review and adjust promotion policies.', 3, 2, NULL, GETDATE(), GETDATE()),
  (@performance, 'Handle Employee Complaints', 'Address workplace concerns.', 2, 3, DATEADD(DAY, 25, GETDATE()), GETDATE(), GETDATE()),
  (@performance, 'Implement Feedback Mechanism', 'Improve feedback collection methods.', 2, 4, NULL, GETDATE(), GETDATE());

-- Assign team members to tasks
INSERT INTO [TaskAssignees] ([userID], [taskID])
VALUES 
  (4, 1), (4, 2),
  (5, 3), (5, 4),
  (6, 5), (6, 6),
  (7, 7), (7, 8),
  (8, 9), (8, 10),
  (9, 11), (9, 12),
  (10, 13), (10, 14);

-- Set dependencies (each second task depends on the first one)
INSERT INTO [TaskDependencies] ([taskID], [dependentTaskID])
VALUES 
  (1, 2),
  (3, 4),
  (5, 6),
  (7, 8),
  (9, 10),
  (11, 12),
  (13, 14);

-- Assign team members to the project
INSERT INTO [ProjectAssignees] ([userID], [projectID])
VALUES 
  (4, @projectID),
  (5, @projectID),
  (6, @projectID),
  (7, @projectID),
  (8, @projectID),
  (9, @projectID),
  (10, @projectID);
