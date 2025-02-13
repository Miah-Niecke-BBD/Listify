GO
CREATE PROCEDURE uspDeleteTask
    @taskID INT,    
    @deletingUserID INT  
AS
BEGIN
    DECLARE @teamID INT, @projectID INT, @taskSectionID INT;

    BEGIN TRANSACTION
    BEGIN TRY

        SELECT @taskSectionID = t.sectionID, 
               @projectID = p.projectID, 
               @teamID = tm.teamID
        FROM Tasks t
        JOIN Sections s ON s.sectionID = t.sectionID
        JOIN Projects p ON p.projectID = s.projectID
        JOIN Teams tm ON tm.teamID = p.teamID
        WHERE t.taskID = @taskID;

        IF @taskSectionID IS NULL
        BEGIN
            THROW 50012, 'Task does not exist.', 1;
        END;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @deletingUserID 
            AND teamID = @teamID 
            AND isTeamLeader = 1
        )
        BEGIN
            THROW 50013, 'Only team leaders can delete tasks.', 1;
        END;

  DELETE FROM TaskAssignees WHERE taskID = @taskID;

        DELETE FROM TaskDependencies WHERE taskID = @taskID OR dependentTaskID = @taskID;

        DELETE FROM Tasks WHERE taskID = @taskID;

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