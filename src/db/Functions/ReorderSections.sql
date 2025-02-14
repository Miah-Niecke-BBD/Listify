CREATE PROCEDURE ReorderSections 
    @projectID INT, 
    @sectionID INT, 
    @newPosition TINYINT
AS
BEGIN
    BEGIN TRANSACTION;

    BEGIN TRY
        -- Validate section existence
        IF NOT EXISTS (SELECT 1 FROM dbo.Sections WHERE sectionID = @sectionID AND projectID = @projectID)
        BEGIN
            ROLLBACK;
            THROW 50022, 'Section does not exist in given project', 1;
        END;

        -- Shift sections forward, avoiding the section being moved
        UPDATE dbo.Sections
        SET sectionPosition = sectionPosition + 1
        WHERE projectID = @projectID 
        AND sectionPosition >= @newPosition 
        AND sectionID <> @sectionID;

        -- Assign the new position to the moved sectio
        UPDATE dbo.Sections
        SET sectionPosition = @newPosition, updatedAt = GETDATE()
        WHERE sectionID = @sectionID;

        COMMIT;
    END TRY 
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH;
END;
GO
