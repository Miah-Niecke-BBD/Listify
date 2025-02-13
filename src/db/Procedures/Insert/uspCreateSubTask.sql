GO
CREATE PROCEDURE uspCreateSubTask
    @userID INT,
    @parentTaskID INT,  
    @taskName VARCHAR(100),
    @taskDescription VARCHAR(500),
    @sectionID INT,
	@dueDate DATETIME = NULL
AS
BEGIN
    DECLARE @teamID INT, @isTeamLeader BIT, @taskPosition TINYINT;

    BEGIN TRY

        SELECT @isTeamLeader = isTeamLeader, 
               @teamID = tm.teamID
        FROM TeamMembers tm
        WHERE tm.userID = @userID AND tm.isTeamLeader = 1;

        IF @isTeamLeader IS NULL
        BEGIN
            THROW 50001, 'Only team leaders can create subtasks.', 1;
        END


        IF NOT EXISTS (SELECT 1 FROM Tasks WHERE taskID = @parentTaskID AND parentTaskID IS NULL)
        BEGIN
            THROW 50002, 'Parent task does not exist or is already a subtask.', 1;
        END

       
        SELECT @taskPosition = ISNULL(MAX(taskPosition), -1) + 1
        FROM Tasks
        WHERE parentTaskID = @parentTaskID;

      
        INSERT INTO Tasks (taskName, taskDescription, sectionID, parentTaskID, taskPosition,dueDate, createdAt)
        VALUES (@taskName, @taskDescription, @sectionID, @parentTaskID, @taskPosition,@dueDate,SYSDATETIME());

        -- DECLARE @subTaskID INT = SCOPE_IDENTITY();

        -- INSERT INTO TaskAssignees (userID, taskID)
        -- VALUES (@userID, @subTaskID);

    END TRY
    BEGIN CATCH
        DECLARE @ErrorMessage NVARCHAR(MAX) = ERROR_MESSAGE();
        PRINT 'Error occurred: ' + @ErrorMessage;
        THROW;
    END CATCH
END;
GO