GO
CREATE PROCEDURE uspRemoveSection
    @teamLeader INT,
    @sectionID INT
AS
BEGIN
    DECLARE @teamID INT, @deletedSectionPosition INT, @projectID INT;
    BEGIN TRANSACTION
    BEGIN TRY

        SELECT 
            @teamID = tm.teamID,
            @deletedSectionPosition = s.sectionPosition,
            @projectID = p.projectID
        FROM Sections s
        JOIN Projects p ON p.projectID = s.projectID
        JOIN Teams tm ON tm.teamID = p.teamID
        WHERE s.sectionID = @sectionID;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers WHERE userID = @teamLeader AND TeamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            PRINT 'Only team leaders can delete sections';
            RETURN;
        END;

        DECLARE task_cursor CURSOR FOR
        SELECT taskID
        FROM Tasks
        WHERE sectionID = @sectionID;

        OPEN task_cursor;
        FETCH NEXT FROM task_cursor INTO @taskID;

        WHILE @@FETCH_STATUS = 0
        BEGIN
            EXECUTE uspDeleteTask @taskID, @teamLeader;
            FETCH NEXT FROM task_cursor INTO @taskID;
        END;

        CLOSE task_cursor;
        DEALLOCATE task_cursor;

        UPDATE Sections
        SET sectionPosition = sectionPosition - 1
        WHERE sectionPosition > @deletedSectionPosition AND projectID = @projectID;

        DELETE FROM Sections
        WHERE sectionID = @sectionID;

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