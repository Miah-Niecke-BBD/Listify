GO
CREATE PROCEDURE listify.uspAssignUserToProject
    @teamLeaderID INT, 
    @userID INT,
    @projectID INT
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
            THROW 50031, 'Only team leaders can assign users to a project',1;
        END;

        IF NOT EXISTS (
            SELECT 1 FROM listify.TeamMembers 
            WHERE userID = @userID AND teamID = @teamID
        )
        BEGIN
            THROW 50029, 'User is not a member of this team',1;
        END;

        IF EXISTS (
            SELECT 1 FROM listify.ProjectAssignees 
            WHERE userID = @userID AND projectID = @projectID
        )
        BEGIN
			ROLLBACK;
            THROW 50026, 'User is already assigned to this project',1;
        END;

        INSERT INTO listify.ProjectAssignees (userID, projectID)
        VALUES (@userID, @projectID);

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