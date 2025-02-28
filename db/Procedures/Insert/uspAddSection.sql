CREATE PROCEDURE listify.uspAddSection
    @teamLeaderID INT,
    @projectID INT,
    @sectionName VARCHAR(100),
    @sectionPosition TINYINT
AS
BEGIN
    DECLARE @teamID INT, @maxSectionPosition TINYINT;

    BEGIN TRANSACTION
    BEGIN TRY

        IF @sectionPosition < 0
		BEGIN
			RAISERROR('Section position cannot be negative', 16, 1);
		END;

        SELECT @teamID = teamID FROM listify.Projects WHERE projectID = @projectID;

        IF NOT EXISTS (
            SELECT 1 FROM listify.TeamMembers
            WHERE userID = @teamLeaderID AND teamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            RAISERROR('Only team leaders can create sections',16, 1);
        END;

        SELECT @maxSectionPosition = MAX(sectionPosition) FROM listify.Sections WHERE projectID = @projectID;
		PRINT 'Max Section Position: ' + CAST(@maxSectionPosition AS VARCHAR(10));

        IF EXISTS (SELECT 1 FROM listify.Sections WHERE projectID = @projectID AND sectionPosition = @sectionPosition)
        BEGIN
            PRINT 'The input position is already taken. Setting to next available position.';
            SET @sectionPosition = @maxSectionPosition + 1;
        END

		IF @maxSectionPosition IS NULL
		BEGIN
            SET @sectionPosition = 0;
		END


        INSERT INTO listify.Sections (projectID, sectionName, sectionPosition, createdAt)
        VALUES (@projectID, @sectionName, @sectionPosition, SYSDATETIME());

        COMMIT;

    END TRY
    BEGIN CATCH
        ROLLBACK;
        DECLARE @ErrorMessage NVARCHAR(MAX) = ERROR_MESSAGE();
		RAISERROR('Error occurred: %s', 16, 1, @ErrorMessage) WITH NOWAIT;
        THROW;
    END CATCH
END;
