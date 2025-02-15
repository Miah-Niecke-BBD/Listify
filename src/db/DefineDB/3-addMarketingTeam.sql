INSERT INTO [Teams] ([teamName], [createdAt], [updatedAt])
VALUES ('Marketing', '2023-07-01', '2023-07-01');

DECLARE @MarketingTeamID INT = SCOPE_IDENTITY();

INSERT INTO [TeamMembers] ([userID], [teamID], [isTeamLeader])
VALUES 
  (11, @MarketingTeamID, 1),  -- Team Leader
  (12, @MarketingTeamID, 0),
  (13, @MarketingTeamID, 0),
  (14, @MarketingTeamID, 0),
  (15, @MarketingTeamID, 0);

INSERT INTO [Projects] ([teamID], [projectName], [projectDescription], [createdAt], [updatedAt])
VALUES (@MarketingTeamID, 'Task App Launch', 'Launching the new Task App', '2023-07-01', '2023-07-01');

DECLARE @ProjectID INT = SCOPE_IDENTITY();

INSERT INTO [Sections] ([projectID], [sectionName], [sectionPosition], [createdAt], [updatedAt])
VALUES 
  (@ProjectID, 'Planning & Research', 0, '2023-07-01', '2023-07-01'),
  (@ProjectID, 'Development', 1, '2023-07-01', '2023-07-01'),
  (@ProjectID, 'Testing & QA', 2, '2023-07-01', '2023-07-01'),
  (@ProjectID, 'Marketing & Launch', 3, '2023-07-01', '2023-07-01');

DECLARE @Section1 INT, @Section2 INT, @Section3 INT, @Section4 INT;
SELECT @Section1 = MIN(sectionID), @Section2 = MIN(sectionID) + 1, 
       @Section3 = MIN(sectionID) + 2, @Section4 = MIN(sectionID) + 3 
FROM [Sections] WHERE projectID = @ProjectID;

INSERT INTO [Tasks] ([sectionID], [taskName], [taskDescription], [taskPriority], [taskPosition], [dueDate], [createdAt], [updatedAt])
VALUES 
  (@Section1, 'Conduct Market Research', 'Analyze competitor products and user needs', 2, 0, '2023-07-06', '2023-07-01', '2023-07-01'),
  (@Section1, 'Define MVP Features', 'List essential features for first launch', 2, 1, '2023-07-11', '2023-07-01', '2023-07-01'),
  (@Section2, 'Design UI/UX', 'Create wireframes and mockups', 2, 0, '2023-07-16', '2023-07-01', '2023-07-01'),
  (@Section2, 'Develop Frontend', 'Implement UI components', 3, 1, '2023-07-21', '2023-07-01', '2023-07-01'),
  (@Section2, 'Develop Backend', 'Set up database and API', 3, 2, '2023-07-26', '2023-07-01', '2023-07-01'),
  (@Section3, 'Write Unit Tests', 'Ensure code quality with unit tests', 2, 0, '2023-07-31', '2023-07-01', '2023-07-01'),
  (@Section3, 'Perform User Testing', 'Get feedback from users', 2, 1, '2023-08-05', '2023-07-01', '2023-07-01'),
  (@Section3, 'Fix Bugs', 'Address major issues found in testing', 3, 2, '2023-08-10', '2023-07-01', '2023-07-01'),
  (@Section4, 'Create Marketing Plan', 'Define promotion strategy', 2, 0, '2023-08-15', '2023-07-01', '2023-07-01'),
  (@Section4, 'Prepare Launch Event', 'Organize live demo and Q&A', 3, 1, '2023-08-20', '2023-07-01', '2023-07-01'),
  (@Section4, 'Social Media Campaign', 'Promote via social platforms', 2, 2, '2023-08-25', '2023-07-01', '2023-07-01'),
  (@Section4, 'Launch Task App', 'Release product to public', 1, 3, '2023-08-30', '2023-07-01', '2023-07-01');

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

    INSERT INTO [TaskDependencies] ([taskID], [dependentTaskID]) VALUES (@Task1, @Task2);

    SET @UserID = @UserID + 1;
    FETCH NEXT FROM TaskCursor INTO @Task1;
    FETCH NEXT FROM TaskCursor INTO @Task2;
    FETCH NEXT FROM TaskCursor INTO @Task3;
    FETCH NEXT FROM TaskCursor INTO @Task4;
END;

CLOSE TaskCursor;
DEALLOCATE TaskCursor;

INSERT INTO [ProjectAssignees] ([userID], [projectID])
VALUES (11, @ProjectID), (12, @ProjectID), (13, @ProjectID), (14, @ProjectID), (15, @ProjectID);
