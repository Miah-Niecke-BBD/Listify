GO
CREATE PROCEDURE uspInviteUserToTeam
    @userID INT,
    @teamID INT,
    @isTeamLeader BIT = 0
AS
BEGIN
    BEGIN TRANSACTION
    BEGIN TRY

        IF EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @userID AND teamID = @teamID
        )
        BEGIN
            THROW 50002, 'User is already a member of this team', 1;
        END;

        -- Insert user into TeamMembers table
        INSERT INTO TeamMembers (userID, teamID, isTeamLeader)
        VALUES (@userID, @teamID, @isTeamLeader);

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