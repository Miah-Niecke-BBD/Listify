IF OBJECT_ID('listify.uspCreateTask', 'P') IS NOT NULL
    DROP PROCEDURE listify.uspCreateTask;
GO

CREATE PROCEDURE listify.uspCreateTask
    @teamLeaderID INT,
    @projectID INT,
    @sectionID INT,
    @taskName VARCHAR(100),
    @taskDescription VARCHAR(500) = NULL,
    @taskPriority TINYINT = 0,
    @dueDate DATETIMEOFFSET = NULL
AS
BEGIN
    DECLARE @teamID INT;

    BEGIN TRANSACTION
        BEGIN TRY
            
            SELECT @teamID = teamID FROM listify.Projects WHERE projectID = @projectID;

            
            IF NOT EXISTS (
                SELECT 1 FROM listify.TeamMembers
                WHERE userID = @teamLeaderID AND teamID = @teamID AND isTeamLeader = 1
            )
            BEGIN
                ROLLBACK;
                THROW 50057, 'Only team leaders can create tasks', 1;
            END;

            
            IF NOT EXISTS (
                SELECT 1 FROM listify.Sections
                WHERE projectID = @projectID AND sectionID = @sectionID
            )
            BEGIN
                ROLLBACK;
                THROW 50058, 'The section does not exist in this project', 1;
            END;

            
            INSERT INTO listify.Tasks (
                sectionID, taskName, taskDescription, taskPriority, taskPosition, dueDate, createdAt
            )
            SELECT 
                @sectionID,
                @taskName,
                @taskDescription,
                @taskPriority,
                COALESCE(MAX(taskPosition) + 1, 0),
                @dueDate,
                SYSDATETIMEOFFSET()
            FROM listify.Tasks
            WHERE sectionID = @sectionID;

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
