GO
CREATE PROCEDURE listify.uspCreateTask
    @teamLeaderID INT,              
    @projectID INT,           
    @sectionID INT,           
    @taskName VARCHAR(100),
    @taskDescription VARCHAR(500) = NULL , 
    @taskPriority TINYINT = 0, 
    @taskPosition TINYINT 
AS
BEGIN
    DECLARE @teamID INT, @maxTaskPosition TINYINT;

    BEGIN TRANSACTION
    BEGIN TRY

        IF @taskPosition < 0
        BEGIN
            ROLLBACK;
            THROW 50055, 'Task position cannot be negative',1;
        END;

        SELECT @teamID = teamID FROM listify.Projects WHERE projectID = @projectID;

        IF NOT EXISTS (
            SELECT 1 FROM listify.TeamMembers 
            WHERE userID = @teamLeaderID AND teamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            ROLLBACK;
            THROW 50057, 'Only team leaders can create tasks',1;
        END;

        IF NOT EXISTS (
            SELECT 1 FROM listify.Sections 
            WHERE projectID = @projectID AND sectionID = @sectionID
        )
        BEGIN
            ROLLBACK;
            THROW 50058, 'The section does not exist in this project',1;
        END;

        SELECT @maxTaskPosition = MAX(taskPosition) FROM listify.Tasks WHERE sectionID = @sectionID;

        IF @taskPosition > @maxTaskPosition
        BEGIN
            PRINT 'The input position exceeds the current maximum task position. Setting to next available position.';
            SET @taskPosition = @maxTaskPosition + 1;
        END
        ELSE IF EXISTS (SELECT 1 FROM listify.Tasks WHERE sectionID = @sectionID AND taskPosition = @taskPosition)
        BEGIN
            PRINT 'The input position is already taken. Setting to next available position.';
            SET @taskPosition = @maxTaskPosition + 1;
        END

        INSERT INTO listify.Tasks (sectionID, taskName, taskDescription, taskPriority, taskPosition, dueDate,  createdAt)
        VALUES (@sectionID, @taskName, @taskDescription, @taskPriority, @taskPosition, SYSDATETIME(), SYSDATETIME());

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        DECLARE @ErrorMessage NVARCHAR(MAX) = ERROR_MESSAGE();
        PRINT 'Error occurred: ' + @ErrorMessage;
        THROW;
    END CATCH
END;
GO