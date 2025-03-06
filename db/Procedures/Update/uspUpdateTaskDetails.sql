IF OBJECT_ID('listify.uspUpdateTaskDetails', 'P') IS NOT NULL
    DROP PROCEDURE listify.uspUpdateTaskDetails;
GO

CREATE PROCEDURE listify.uspUpdateTaskDetails
    @taskID INT,
    @userID INT,
    @newTaskName VARCHAR(100) = NULL,
    @newTaskDescription VARCHAR(500) = NULL,
    @newTaskPriority TINYINT = NULL,
    @newDueDate DATETIMEOFFSET = NULL,
    @newDateCompleted DATETIMEOFFSET = NULL
AS
BEGIN
    BEGIN TRANSACTION;

    DECLARE @sectionID INT;
    DECLARE @projectID INT;
    DECLARE @teamID INT;

    SELECT @sectionID = sectionID
    FROM listify.Tasks
    WHERE taskID = @taskID;

    SELECT @projectID = projectID
    FROM listify.Sections
    WHERE sectionID = @sectionID;

    SELECT @teamID = teamID
    FROM listify.Projects
    WHERE projectID = @projectID;

    IF NOT EXISTS (
        SELECT 1 FROM listify.TeamMembers WHERE userID = @userID AND teamID = @teamID
    )
        BEGIN
            THROW 50082, 'User is not part of the project team.', 1;
        END;

    IF @newTaskPriority IS NOT NULL AND (@newTaskPriority < 1 OR @newTaskPriority > 3)
        BEGIN
            ROLLBACK;
            THROW 50081, 'Task priority can only have values between 1 and 3', 1;
        END;

    UPDATE listify.Tasks
    SET
        taskName = COALESCE(@newTaskName, taskName),
        taskDescription = COALESCE(@newTaskDescription, taskDescription),
        taskPriority = COALESCE(@newTaskPriority, taskPriority),
        dueDate = COALESCE(@newDueDate, dueDate),
        dateCompleted = COALESCE(@newDateCompleted, dateCompleted),
        updatedAt = SYSDATETIMEOFFSET()
    WHERE taskID = @taskID;

    COMMIT TRANSACTION;
END;
GO
