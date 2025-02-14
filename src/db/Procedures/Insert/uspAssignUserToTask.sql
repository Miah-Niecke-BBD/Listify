GO
CREATE PROCEDURE uspAssignUserToTask
    @userID INT,          
    @taskID INT,              
    @teamLeaderID INT      
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
            PRINT'Task does not exist in the given section and project.';
        END;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers WHERE userID = @teamLeaderID AND teamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            PRINT 'Only team leaders can assign users to tasks';
        END;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers WHERE userID = @userID AND teamID = @teamID
        )
        BEGIN
            PRINT 'User is not a member of this team';
        END;

        IF NOT EXISTS (
            SELECT 1 FROM ProjectAssignees WHERE userID = @userID AND projectID = @projectID
        )
        BEGIN
            PRINT 'User is not assigned to this project';
        END;

        IF EXISTS (
            SELECT 1 FROM TaskAssignees WHERE userID = @userID AND taskID = @taskID
        )
        BEGIN
            PRINT 'User is already assigned to this task';
        END;

        INSERT INTO TaskAssignees (userID, taskID)
        VALUES (@userID, @taskID);

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