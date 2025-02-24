
CREATE PROCEDURE listify.uspCreateSection
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
            ROLLBACK;
            THROW 50047, 'Section position cannot be negative',1;
        END;

        SELECT @teamID = teamID FROM listify.Projects WHERE projectID = @projectID;

        IF NOT EXISTS (
            SELECT 1 FROM listify.TeamMembers 
            WHERE userID = @teamLeaderID AND teamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            ROLLBACK;
            THROW 50049, 'Only team leaders can create sections',1;
        END;

        SELECT @maxSectionPosition = MAX(sectionPosition) FROM listify.Sections WHERE projectID = @projectID;

        IF EXISTS (SELECT 1 FROM listify.Sections WHERE projectID = @projectID AND sectionPosition = @sectionPosition)
        BEGIN
            PRINT 'The input position is already taken. Setting to next available position.';
            SET @sectionPosition = @maxSectionPosition + 1;
        END
        ELSE
        BEGIN
            ROLLBACK;
            THROW 50050, 'Setting to input position.',1;
        END

        INSERT INTO listify.Sections (projectID, sectionName, sectionPosition, createdAt)
        VALUES (@projectID, @sectionName, @sectionPosition, SYSDATETIME());

        COMMIT;

    END TRY
    BEGIN CATCH
        ROLLBACK;
        DECLARE @ErrorMessage NVARCHAR(MAX) = ERROR_MESSAGE();
        PRINT 'Error occurred: ' + @ErrorMessage;
        THROW;
    END CATCH
END;
