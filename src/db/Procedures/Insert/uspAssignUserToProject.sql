GO
CREATE PROCEDURE uspAssignUserToProject
    @assignerID INT, 
    @userID INT,
    @projectID INT
AS
BEGIN
    DECLARE @teamID INT;

    BEGIN TRANSACTION
    BEGIN TRY
     
        SELECT @teamID = teamID FROM Projects WHERE projectID = @projectID;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @assignerID AND teamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            THROW 50004, 'Only team leaders can assign users to a project', 1;
        END;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @userID AND teamID = @teamID
        )
        BEGIN
            THROW 50005, 'User is not a member of this team', 1;
        END;

        INSERT INTO ProjectAssignees (userID, projectID)
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
