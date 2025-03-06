ALTER TABLE listify.Tasks
    ALTER COLUMN createdAt DATETIMEOFFSET;

ALTER TABLE listify.Tasks
    ALTER COLUMN updatedAt DATETIMEOFFSET;

ALTER TABLE listify.Tasks
    ALTER COLUMN dueDate DATETIMEOFFSET;

ALTER TABLE listify.Tasks
    ALTER COLUMN dateCompleted DATETIMEOFFSET;

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
    DECLARE @teamID INT, @nextTaskPosition TINYINT;

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

            SELECT @nextTaskPosition = ISNULL(MAX(taskPosition), -1) + 1
            FROM listify.Tasks
            WHERE sectionID = @sectionID;

            INSERT INTO listify.Tasks (sectionID, taskName, taskDescription, taskPriority, taskPosition, dueDate, createdAt)
            VALUES (@sectionID, @taskName, @taskDescription, @taskPriority, @nextTaskPosition, @dueDate, SYSDATETIMEOFFSET());

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
