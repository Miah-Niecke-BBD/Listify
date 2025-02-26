CREATE PROCEDURE listify.uspAssignUserToTask
    @userID INT,          
    @taskID INT               
AS
BEGIN
    DECLARE @teamID INT, @projectID INT, @taskSectionID INT;

    BEGIN TRANSACTION
    BEGIN TRY

        SELECT @taskSectionID = t.sectionID, 
               @projectID = p.projectID, 
               @teamID = tm.teamID
        FROM listify.Tasks t
        JOIN listify.Sections s ON s.sectionID = t.sectionID
        JOIN listify.Projects p ON p.projectID = s.projectID
        JOIN listify.Teams tm ON tm.teamID = p.teamID
        WHERE t.taskID = @taskID;

        IF @taskSectionID IS NULL
        BEGIN
            ROLLBACK;
            THROW 50034, 'Task does not exist in the given section and project.', 1;
        END;

       
        IF NOT EXISTS (
            SELECT 1 FROM listify.TeamMembers WHERE userID = @userID AND teamID = @teamID
        )
        BEGIN
            ROLLBACK;
            THROW 50039, 'User is not a member of this team', 1;
        END;

       
        IF NOT EXISTS (
            SELECT 1 FROM listify.ProjectAssignees WHERE userID = @userID AND projectID = @projectID
        )
        BEGIN
            ROLLBACK;
            THROW 50040, 'User is not assigned to this project', 1;
        END;

       
        IF EXISTS (
            SELECT 1 FROM listify.TaskAssignees WHERE userID = @userID AND taskID = @taskID
        )
        BEGIN
            ROLLBACK;
            THROW 50041, 'User is already assigned to this task', 1;
        END;

        
        INSERT INTO listify.TaskAssignees (userID, taskID)
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
