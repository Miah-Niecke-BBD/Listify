CREATE PROCEDURE [listify].uspDeleteSection
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
        FROM [listify].Sections s
        JOIN [listify].Projects p ON p.projectID = s.projectID
        JOIN Teams tm ON tm.teamID = p.teamID
        WHERE s.sectionID = @sectionID;

        IF NOT EXISTS (
            SELECT 1 FROM [listify].TeamMembers WHERE userID = @teamLeader AND TeamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            ROLLBACK;
            THROW 50010, 'Only team leaders can delete sections',1;
        END;

        DELETE FROM [listify].TaskAssignees
        WHERE taskID IN (
            SELECT taskID
            FROM tasks
            WHERE sectionID = @sectionID
        );

        UPDATE [listify].Sections
        SET sectionPosition = sectionPosition - 1
        WHERE sectionPosition > @deletedSectionPosition AND projectID = @projectID;

        DELETE FROM [listify].Sections
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
