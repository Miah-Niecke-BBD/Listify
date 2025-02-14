CREATE PROCEDURE uspUpdateTaskPosition
	@teamLeaderID INT,
	@taskID INT,
	@newTaskPosition INT,
    @section INT
AS
BEGIN 
	DECLARE @teamID INT, @sectionID INT, @currentTaskPosition INT, @currentSection INT;
	BEGIN TRANSACTION
	BEGIN TRY

	SELECT 
		@teamID = tm.teamID,
		@sectionID = t.sectionID,
		@currentTaskPosition = t.taskPosition,
        @currentSection = s.sectionID
	FROM Tasks t
	JOIN Sections s ON s.sectionID = t.sectionID
    JOIN Projects p ON p.projectID = s.projectID
    JOIN Teams tm ON tm.teamID = p.teamID
    WHERE t.taskID = @taskID;

	IF NOT EXISTS (
		SELECT 1 FROM TeamMembers WHERE userID = @teamLeaderID AND TeamID = @teamID AND isTeamLeader = 1
	)
	 BEGIN
           PRINT 'Only team leaders can edit users to tasks';
     END;

     IF @currentSection = @section
     BEGIN
	    UPDATE Tasks
	    SET taskPosition = taskPosition - 1
	    WHERE taskPosition > @currentTaskPosition AND sectionID = @sectionID
	END
	ELSE
	 BEGIN 
	    UPDATE Tasks
	    SET taskPosition = taskPosition - 1
	    WHERE taskPosition > @currentTaskPosition AND sectionID = @currentSection
	END

	    UPDATE Tasks
	    SET taskPosition = taskPosition + 1
	    WHERE taskPosition >= @newTaskPosition AND sectionID = @sectionID

	    UPDATE Tasks
	    SET taskPosition = @newTaskPosition
	    WHERE taskID = @taskID AND sectionID = @sectionID
     
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