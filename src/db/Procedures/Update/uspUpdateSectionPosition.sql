CREATE PROCEDURE listify.uspUpdateSectionPosition
	@teamLeaderID INT,
	@sectionID INT,
	@newSectionPosition INT
AS
BEGIN 
	DECLARE @teamID INT, @projectID INT, @currentPosition INT, @maxTaskPosition INT, @minTaskPosition INT;
	BEGIN TRANSACTION
	BEGIN TRY

	SELECT 
		@teamID = tm.teamID,
		@currentPosition = s.sectionPosition,
		@projectID = p.projectID
	FROM listify.Sections s
    JOIN listify.Projects p ON p.projectID = s.projectID
    JOIN listify.Teams tm ON tm.teamID = p.teamID
    WHERE s.sectionID = @sectionID

	IF NOT EXISTS (
		SELECT 1 FROM listify.TeamMembers WHERE userID = @teamLeaderID AND TeamID = @teamID AND isTeamLeader = 1
	)
	 BEGIN
	 		ROLLBACK;
           THROW 50078, 'Only team leaders can delete sections',1;
     END;

	 SELECT 
            @maxTaskPosition = MAX(sectionPosition),
            @minTaskPosition = MIN(sectionPosition)
        FROM Sections
        WHERE projectID = @projectID
 
        
        IF @newSectionPosition < 0
        BEGIN
            SET @newSectionPosition = 0;
        END
        ELSE IF @newSectionPosition > @maxTaskPosition
        BEGIN
            SET @newSectionPosition = @maxTaskPosition; 
        END


	 UPDATE listify.Sections
	 SET sectionPosition = sectionPosition - 1
	 WHERE sectionPosition > @currentPosition AND projectID = @projectID

	 UPDATE listify.Sections
	 SET sectionPosition = sectionPosition + 1
	 WHERE sectionPosition >= @newSectionPosition AND projectID = @projectID

	 UPDATE listify.Sections
	 SET sectionPosition = @newSectionPosition
	 WHERE sectionID = @sectionID AND projectID = @projectID

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