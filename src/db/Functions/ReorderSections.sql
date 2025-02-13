CREATE PROCEDURE ReorderSections 
    @projectID INT, 
    @sectionID INT, 
    @newPosition TINYINT
AS
BEGIN
    BEGIN TRANSACTION
    BEGIN TRY
    
        IF NOT EXISTS (SELECT 1 FROM Sections WHERE sectionID = @sectionID AND projectID = @projectID)
        BEGIN
            THROW 50022, 'Section does not exist in given project', 1;
        END;

        UPDATE Sections
        SET sectionPosition = sectionPosition + 1
        WHERE projectID = @projectID 
        AND sectionPosition >= @newPosition 
        AND sectionID <> @sectionID;

        UPDATE Sections
        SET sectionPosition = @newPosition, updatedAt = GETDATE()
        WHERE sectionID = @sectionID;

        COMMIT;
    END TRY 
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;
GO  
