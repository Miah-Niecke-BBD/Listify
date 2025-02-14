GO
CREATE PROCEDURE uspInviteUserToTeam
    @teamLeaderID INT,
    @userID INT,
    @teamID INT
AS
BEGIN
    BEGIN TRANSACTION
    BEGIN TRY

         IF NOT EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @teamLeaderID AND teamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            PRINT 'Only team leaders can create projects';
            ROLLBACK;
            RETURN;
        END;

        IF EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @userID AND teamID = @teamID
        )
        BEGIN
            PRINT 'User is already a member of this team';
            ROLLBACK;
            RETURN;
        END;
        
        INSERT INTO TeamMembers (userID, teamID, isTeamLeader)
        VALUES (@userID, @teamID, 0);

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