	IF OBJECT_ID('listify.uspCreateSubTask', 'P') IS NOT NULL
        DROP PROCEDURE listify.uspCreateSubTask;
    GO

	CREATE PROCEDURE listify.uspCreateSubTask
		@teamLeaderID INT,
		@parentTaskID INT,  
		@taskName VARCHAR(100),
		@taskDescription VARCHAR(500),
		@sectionID INT,
		@dueDate DATETIMEOFFSET = NULL
	AS
	BEGIN
		DECLARE @teamID INT, @isTeamLeader BIT, @taskPosition TINYINT;

		BEGIN TRY

			SELECT @isTeamLeader = isTeamLeader, 
				   @teamID = tm.teamID
			FROM listify.TeamMembers tm
			WHERE tm.userID = @teamLeaderID AND tm.isTeamLeader = 1;

			IF @isTeamLeader IS NULL
			BEGIN
				ROLLBACK;
				THROW 50053, 'Only team leaders can create subtasks.',1;
            RETURN;
        END

        IF NOT EXISTS (SELECT 1 FROM listify.Tasks WHERE taskID = @parentTaskID AND parentTaskID IS NULL)
        BEGIN
			ROLLBACK;
            THROW 50053,  'Parent task does not exist or is already a subtask.',1;
        END

        SELECT @taskPosition = ISNULL(MAX(taskPosition), 0) + 1
        FROM listify.Tasks
        WHERE parentTaskID = @parentTaskID;

        INSERT INTO listify.Tasks (taskName, taskDescription, sectionID, parentTaskID, taskPosition, dueDate, createdAt)
        VALUES (@taskName, @taskDescription, @sectionID, @parentTaskID, @taskPosition, @dueDate, SYSDATETIMEOFFSET());

    END TRY
    BEGIN CATCH
        DECLARE @ErrorMessage NVARCHAR(MAX) = ERROR_MESSAGE();
        PRINT 'Error occurred: ' + @ErrorMessage;
        THROW;
    END CATCH
END;
