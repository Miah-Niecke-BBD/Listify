INSERT INTO [Teams] ([teamName], [createdAt], [updatedAt])
VALUES ('Human Resources', '2023-06-01', '2023-06-01');

DECLARE @teamID INT = SCOPE_IDENTITY();

INSERT INTO [TeamMembers] ([userID], [teamID], [isTeamLeader])
VALUES 
  (4, @teamID, 1),
  (5, @teamID, 0),
  (6, @teamID, 0),
  (7, @teamID, 0),
  (8, @teamID, 0),
  (9, @teamID, 0),
  (10, @teamID, 0);

INSERT INTO [Projects] ([teamID], [projectName], [projectDescription], [createdAt], [updatedAt])
VALUES (@teamID, 'Manage Staff', 'A project to oversee staff management processes.', '2023-06-01', '2023-06-01');

-- Get the project ID
DECLARE @projectID INT = SCOPE_IDENTITY();

INSERT INTO [Sections] ([projectID], [sectionName], [sectionPosition], [createdAt], [updatedAt])
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
INSERT INTO [Tasks] ([sectionID], [taskName], [taskDescription], [taskPriority], [taskPosition], [dueDate], [createdAt], [updatedAt])
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

INSERT INTO [TaskAssignees] ([userID], [taskID])
VALUES 
  (4, 1), (4, 2),
  (5, 3), (5, 4),
  (6, 5), (6, 6),
  (7, 7), (7, 8),
  (8, 9), (8, 10),
  (9, 11), (9, 12),
  (10, 13), (10, 14);

INSERT INTO [TaskDependencies] ([taskID], [dependentTaskID])
VALUES 
  (1, 2),
  (3, 4),
  (5, 6),
  (7, 8),
  (9, 10),
  (11, 12),
  (13, 14);

INSERT INTO [ProjectAssignees] ([userID], [projectID])
VALUES 
  (4, @projectID),
  (5, @projectID),
  (6, @projectID),
  (7, @projectID),
  (8, @projectID),
  (9, @projectID),
  (10, @projectID);

