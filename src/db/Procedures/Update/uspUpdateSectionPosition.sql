CREATE PROCEDURE uspUpdateSectionPosition
	@teamLeaderID INT,
	@sectionID INT,
	@newSectionPosition INT
AS
BEGIN 
	DECLARE @teamID INT, @projectID INT, @currentPosition INT;
	BEGIN TRANSACTION
	BEGIN TRY

	SELECT 
		@teamID = tm.teamID,
		@currentPosition = s.sectionPosition,
		@projectID = p.projectID
	FROM Sections s
    JOIN Projects p ON p.projectID = s.projectID
    JOIN Teams tm ON tm.teamID = p.teamID
    WHERE s.sectionID = @sectionID

	IF NOT EXISTS (
		SELECT 1 FROM TeamMembers WHERE userID = @teamLeaderID AND TeamID = @teamID AND isTeamLeader = 1
	)
	 BEGIN
           PRINT 'Only team leaders can delete sections';
		   ROLLBACK;
     END;


	 UPDATE Sections
	 SET sectionPosition = sectionPosition - 1
	 WHERE sectionPosition > @currentPosition AND projectID = @projectID

	 UPDATE Sections
	 SET sectionPosition = sectionPosition + 1
	 WHERE sectionPosition >= @newSectionPosition AND projectID = @projectID

	 UPDATE Sections
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