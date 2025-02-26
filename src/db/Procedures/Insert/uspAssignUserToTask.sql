GO
CREATE PROCEDURE uspAssignUserToTask
    @userID INT,          
    @taskID INT,              
    @assigningUserID INT      
AS
BEGIN
    DECLARE @teamID INT, @projectID INT, @taskSectionID INT, @teamLeader INT;

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
            THROW 50011, 'Task does not exist in the given section and project.', 1;
        END;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers WHERE userID = @assigningUserID AND teamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            THROW 50006, 'Only team leaders can assign users to tasks', 1;
        END;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers WHERE userID = @userID AND teamID = @teamID
        )
        BEGIN
            THROW 50008, 'User is not a member of this team', 1;
        END;

        IF NOT EXISTS (
            SELECT 1 FROM ProjectAssignees WHERE userID = @userID AND projectID = @projectID
        )
        BEGIN
            THROW 50009, 'User is not assigned to this project', 1;
        END;

        IF EXISTS (
            SELECT 1 FROM TaskAssignees WHERE userID = @userID AND taskID = @taskID
        )
        BEGIN
            THROW 50010, 'User is already assigned to this task', 1;
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