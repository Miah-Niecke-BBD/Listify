GO
CREATE PROCEDURE uspCreateTask
    @userID INT,              
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
            THROW 50010, 'Task position cannot be negative', 1;
        END;

        SELECT @teamID = teamID FROM Projects WHERE projectID = @projectID;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @userID AND teamID = @teamID
        )
        BEGIN
            THROW 50008, 'User is not a member of this team', 1;
        END;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @userID AND teamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            THROW 50006, 'Only team leaders can create tasks', 1;
        END;

        IF NOT EXISTS (
            SELECT 1 FROM Sections 
            WHERE projectID = @projectID AND sectionID = @sectionID
        )
        BEGIN
            THROW 50009, 'The section does not exist in this project', 1;
        END;

        SELECT @maxTaskPosition = MAX(taskPosition) FROM Tasks WHERE sectionID = @sectionID;

        IF @taskPosition <= @maxTaskPosition
        BEGIN
            SET @taskPosition = @maxTaskPosition + 1;
        END

        INSERT INTO Tasks (sectionID, taskName, taskDescription, taskPriority, taskPosition, dueDate,  createdAt)
        VALUES (@sectionID, @taskName, @taskDescription, @taskPriority, @taskPosition, SYSDATETIME(),  SYSDATETIME());

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