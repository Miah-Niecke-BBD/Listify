GO
CREATE PROCEDURE uspUpdateTaskDetails
    @taskID INT,
    @teamLeaderID INT,
    @newTaskName VARCHAR(100) = NULL,
    @newTaskDescription VARCHAR(500) = NULL,
    @newTaskPriority TINYINT = NULL,
    @newDueDate DATETIME = NULL
AS
BEGIN
    BEGIN TRANSACTION;

    DECLARE @sectionID INT;
    DECLARE @projectID INT;
    DECLARE @teamID INT;
    DECLARE @isTeamLeader BIT;


    SELECT @sectionID = sectionID
    FROM Tasks
    WHERE taskID = @taskID;

    SELECT @projectID = projectID
    FROM Sections
    WHERE sectionID = @sectionID;

    SELECT @teamID = teamID
    FROM Projects
    WHERE projectID = @projectID;

    SELECT @isTeamLeader = isTeamLeader
    FROM TeamMembers
    WHERE userID = @teamLeaderID AND teamID = @teamID;

    IF @isTeamLeader <> 1
    BEGIN
        PRINT 'Only team leaders can update tasks.';
        ROLLBACK;
    END

    UPDATE Tasks
    SET 
        taskName = COALESCE(@newTaskName, taskName),
        taskDescription = COALESCE(@newTaskDescription, taskDescription),
        taskPriority = COALESCE(@newTaskPriority, taskPriority),
        dueDate = COALESCE(@newDueDate, dueDate),
        updatedAt = GETDATE() 
    WHERE taskID = @taskID;

    COMMIT TRANSACTION;
END;
GO