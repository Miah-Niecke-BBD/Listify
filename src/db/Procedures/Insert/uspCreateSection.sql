GO
CREATE PROCEDURE uspCreateSection
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
            PRINT 'Section position cannot be negative';
            ROLLBACK;
        END;

        SELECT @teamID = teamID FROM Projects WHERE projectID = @projectID;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @teamLeaderID AND teamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            PRINT 'Only team leaders can create sections';
           ROLLBACK;
        END;

        SELECT @maxSectionPosition = MAX(sectionPosition) FROM Sections WHERE projectID = @projectID;

        IF EXISTS (SELECT 1 FROM Sections WHERE projectID = @projectID AND sectionPosition = @sectionPosition)
        BEGIN
            PRINT 'The input position is already taken. Setting to next available position.';
            SET @sectionPosition = @maxSectionPosition + 1;
        END
        ELSE
        BEGIN
            PRINT 'Setting to input position.';
        END

        INSERT INTO Sections (projectID, sectionName, sectionPosition, createdAt)
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
GO