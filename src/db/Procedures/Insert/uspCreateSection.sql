GO
CREATE PROCEDURE uspCreateSection
    @userID INT,              
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
            THROW 50010, 'Section position cannot be negative', 1;
        END;

        SELECT @teamID = teamID FROM Projects WHERE projectID = @projectID;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @userID AND teamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            THROW 50006, 'Only team leaders can create sections', 1;
        END;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @userID AND teamID = @teamID
        )
        BEGIN
            THROW 50007, 'User is not a member of this team', 1;
        END;

        SELECT @maxSectionPosition = MAX(sectionPosition) FROM Sections WHERE projectID = @projectID;

        IF @sectionPosition <= @maxSectionPosition
        BEGIN
            SET @sectionPosition = @maxSectionPosition + 1;
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