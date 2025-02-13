-- Insert new team "Marketing"
INSERT INTO [Teams] ([teamName], [createdAt], [updatedAt])
VALUES ('Marketing', GETDATE(), GETDATE());

-- Get the teamID for "Marketing"
DECLARE @MarketingTeamID INT = SCOPE_IDENTITY();

-- Insert team members (User 11 is the leader)
INSERT INTO [TeamMembers] ([userID], [teamID], [isTeamLeader])
VALUES 
  (11, @MarketingTeamID, 1),  -- Team Leader
  (12, @MarketingTeamID, 0),
  (13, @MarketingTeamID, 0),
  (14, @MarketingTeamID, 0),
  (15, @MarketingTeamID, 0);

-- Insert new project "Task App Launch"
INSERT INTO [Projects] ([teamID], [projectName], [projectDescription], [createdAt], [updatedAt])
VALUES (@MarketingTeamID, 'Task App Launch', 'Launching the new Task App', GETDATE(), GETDATE());

-- Get the projectID for "Task App Launch"
DECLARE @ProjectID INT = SCOPE_IDENTITY();

-- Insert project sections
INSERT INTO [Sections] ([projectID], [sectionName], [sectionPosition], [createdAt], [updatedAt])
VALUES 
  (@ProjectID, 'Planning & Research', 1, GETDATE(), GETDATE()),
  (@ProjectID, 'Development', 2, GETDATE(), GETDATE()),
  (@ProjectID, 'Testing & QA', 3, GETDATE(), GETDATE()),
  (@ProjectID, 'Marketing & Launch', 4, GETDATE(), GETDATE());

-- Get section IDs
DECLARE @Section1 INT, @Section2 INT, @Section3 INT, @Section4 INT;
SELECT @Section1 = MIN(sectionID), @Section2 = MIN(sectionID) + 1, 
       @Section3 = MIN(sectionID) + 2, @Section4 = MIN(sectionID) + 3 
FROM [Sections] WHERE projectID = @ProjectID;

-- Insert 20 tasks
INSERT INTO [Tasks] ([sectionID], [taskName], [taskDescription], [taskPriority], [taskPosition], [dueDate], [createdAt], [updatedAt])
VALUES 
  (@Section1, 'Conduct Market Research', 'Analyze competitor products and user needs', 2, 1, DATEADD(DAY, RAND() * 10 + 5, GETDATE()), GETDATE(), GETDATE()),
  (@Section1, 'Define MVP Features', 'List essential features for first launch', 2, 2, DATEADD(DAY, RAND() * 10 + 10, GETDATE()), GETDATE(), GETDATE()),
  (@Section2, 'Design UI/UX', 'Create wireframes and mockups', 2, 1, DATEADD(DAY, RAND() * 10 + 15, GETDATE()), GETDATE(), GETDATE()),
  (@Section2, 'Develop Frontend', 'Implement UI components', 3, 2, DATEADD(DAY, RAND() * 10 + 20, GETDATE()), GETDATE(), GETDATE()),
  (@Section2, 'Develop Backend', 'Set up database and API', 3, 3, DATEADD(DAY, RAND() * 10 + 25, GETDATE()), GETDATE(), GETDATE()),
  (@Section3, 'Write Unit Tests', 'Ensure code quality with unit tests', 2, 1, DATEADD(DAY, RAND() * 10 + 30, GETDATE()), GETDATE(), GETDATE()),
  (@Section3, 'Perform User Testing', 'Get feedback from users', 2, 2, DATEADD(DAY, RAND() * 10 + 35, GETDATE()), GETDATE(), GETDATE()),
  (@Section3, 'Fix Bugs', 'Address major issues found in testing', 3, 3, DATEADD(DAY, RAND() * 10 + 40, GETDATE()), GETDATE(), GETDATE()),
  (@Section4, 'Create Marketing Plan', 'Define promotion strategy', 2, 1, DATEADD(DAY, RAND() * 10 + 45, GETDATE()), GETDATE(), GETDATE()),
  (@Section4, 'Prepare Launch Event', 'Organize live demo and Q&A', 3, 2, DATEADD(DAY, RAND() * 10 + 50, GETDATE()), GETDATE(), GETDATE()),
  (@Section4, 'Social Media Campaign', 'Promote via social platforms', 2, 3, DATEADD(DAY, RAND() * 10 + 55, GETDATE()), GETDATE(), GETDATE()),
  (@Section4, 'Launch Task App', 'Release product to public', 1, 4, DATEADD(DAY, RAND() * 10 + 60, GETDATE()), GETDATE(), GETDATE());

-- Assign users to 4 tasks each
DECLARE @Task1 INT, @Task2 INT, @Task3 INT, @Task4 INT;
DECLARE @UserID INT = 11;

DECLARE TaskCursor CURSOR FOR
SELECT taskID FROM [Tasks] WHERE sectionID IN (@Section1, @Section2, @Section3, @Section4);

OPEN TaskCursor;

FETCH NEXT FROM TaskCursor INTO @Task1;
FETCH NEXT FROM TaskCursor INTO @Task2;
FETCH NEXT FROM TaskCursor INTO @Task3;
FETCH NEXT FROM TaskCursor INTO @Task4;

WHILE @@FETCH_STATUS = 0
BEGIN
    INSERT INTO [TaskAssignees] ([userID], [taskID]) VALUES (@UserID, @Task1);
    INSERT INTO [TaskAssignees] ([userID], [taskID]) VALUES (@UserID, @Task2);
    INSERT INTO [TaskAssignees] ([userID], [taskID]) VALUES (@UserID, @Task3);
    INSERT INTO [TaskAssignees] ([userID], [taskID]) VALUES (@UserID, @Task4);

    -- Add task dependency (Second task depends on first)
    INSERT INTO [TaskDependencies] ([taskID], [dependentTaskID]) VALUES (@Task1, @Task2);

    -- Move to next user and next 4 tasks
    SET @UserID = @UserID + 1;
    FETCH NEXT FROM TaskCursor INTO @Task1;
    FETCH NEXT FROM TaskCursor INTO @Task2;
    FETCH NEXT FROM TaskCursor INTO @Task3;
    FETCH NEXT FROM TaskCursor INTO @Task4;
END;

CLOSE TaskCursor;
DEALLOCATE TaskCursor;

-- Assign users to the project
INSERT INTO [ProjectAssignees] ([userID], [projectID])
VALUES (11, @ProjectID), (12, @ProjectID), (13, @ProjectID), (14, @ProjectID), (15, @ProjectID);
