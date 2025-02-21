
CREATE PROCEDURE listify.uspCreateTeam
    @userID INT,
    @teamName VARCHAR(100)
AS
BEGIN
    DECLARE @teamID INT;

    BEGIN TRANSACTION
    BEGIN TRY

        INSERT INTO listify.Teams (teamName, createdAt)
        VALUES (@teamName, SYSDATETIME());

		SET @teamID = SCOPE_IDENTITY();

        INSERT INTO listify.TeamMembers (userID, teamID, isTeamLeader)
        VALUES (@userID, @teamID, 1);

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        DECLARE @ErrorMessage NVARCHAR(MAX) = ERROR_MESSAGE();
        PRINT 'Error occurred: ' + @ErrorMessage;
        THROW;
    END CATCH
END;
