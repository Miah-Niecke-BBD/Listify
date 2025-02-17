CREATE PROCEDURE listify.uspUpdateTaskDetails
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
    FROM listify.Tasks
    WHERE taskID = @taskID;

    SELECT @projectID = projectID
    FROM listify.Sections
    WHERE sectionID = @sectionID;

    SELECT @teamID = teamID
    FROM listify.Projects
    WHERE projectID = @projectID;

    SELECT @isTeamLeader = isTeamLeader
    FROM listify.TeamMembers
    WHERE userID = @teamLeaderID AND teamID = @teamID;

    IF @isTeamLeader <> 1
    BEGIN
        THROW 50080, 'Only team leaders can update tasks.',1;
    END

	IF @newTaskPriority < 0 OR @newTaskPriority > 3
	BEGIN
		ROLLBACK;
		THROW 50081, 'Task priority can only have values between 0 and 3',1;
	END

    UPDATE listify.Tasks
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