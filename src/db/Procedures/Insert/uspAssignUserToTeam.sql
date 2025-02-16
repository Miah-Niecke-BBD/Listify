GO
CREATE PROCEDURE uspAssignUserToTeam
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
            ROLLBACK;
            THROW 50098, 'Only team leaders can create projects',1;
        END;

        IF EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @userID AND teamID = @teamID
        )
        BEGIN
            ROLLBACK;
            THROW 50060, 'User is already a member of this team',1;
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