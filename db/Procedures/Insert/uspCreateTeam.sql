ALTER TABLE listify.Teams
    ALTER COLUMN createdAt DATETIMEOFFSET;
ALTER TABLE listify.Teams
	ALTER COLUMN updatedAt DATETIMEOFFSET;

IF OBJECT_ID('listify.uspCreateTeam', 'P') IS NOT NULL
    DROP PROCEDURE listify.uspCreateTeam;
GO

CREATE PROCEDURE listify.uspCreateTeam
    @userID INT,
    @teamName VARCHAR(100)
AS
BEGIN
    DECLARE @teamID INT;

    BEGIN TRANSACTION
    BEGIN TRY

        INSERT INTO listify.Teams (teamName, createdAt)
        VALUES (@teamName, SYSDATETIMEOFFSET());

		SET @teamID = SCOPE_IDENTITY();

        INSERT INTO listify.TeamMembers (userID, teamID, isTeamLeader)
        VALUES (@userID, @teamID, 1);

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        DECLARE @ErrorMessage NVARCHAR(MAX) = ERROR_MESSAGE();
        THROW 5001, 'Error occurred: ', @ErrorMessage;
    END CATCH
END;
