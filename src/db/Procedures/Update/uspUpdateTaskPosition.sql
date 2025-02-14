CREATE PROCEDURE uspUpdateTaskPosition
	@teamLeaderID INT,
	@taskID INT,
	@newTaskPosition INT,
    @sectionID INT
AS
BEGIN 
	DECLARE @teamID INT, @currentTaskPosition INT, @currentSectionID INT, @currentProjectID INT,  @maxTaskPosition INT,  @minTaskPosition INT
	BEGIN TRANSACTION
	BEGIN TRY
 
	SELECT 
		@teamID = tm.teamID,
		@currentTaskPosition = t.taskPosition,
        @currentSectionID = t.sectionID
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
		   ROLLBACK;
		   RETURN
     END;
 
	 IF EXISTS (
		SELECT 1 FROM Sections WHERE sectionID = @sectionID AND projectID != @currentProjectID
	 )
	 BEGIN
            PRINT 'Cannot move task to a section in a different project.';
			ROLLBACK;
			RETURN
     END
 
	  SELECT 
            @maxTaskPosition = MAX(taskPosition),
            @minTaskPosition = MIN(taskPosition)
        FROM Tasks
        WHERE sectionID = @sectionID;
 
        
        IF @newTaskPosition < 0
        BEGIN
            SET @newTaskPosition = 0;
        END
        ELSE IF @newTaskPosition > @maxTaskPosition
        BEGIN
            SET @newTaskPosition = @maxTaskPosition; 
        END
 
     IF @currentSectionID = @sectionID
     BEGIN
	    UPDATE Tasks
	    SET taskPosition = taskPosition - 1
	    WHERE taskPosition > @currentTaskPosition AND sectionID = @sectionID
	END
	ELSE
	 BEGIN 
	    UPDATE Tasks
	    SET taskPosition = taskPosition - 1
	    WHERE taskPosition > @currentTaskPosition AND sectionID = @currentSectionID
	END
 
	    UPDATE Tasks
	    SET taskPosition = taskPosition + 1
	    WHERE taskPosition >= @newTaskPosition AND sectionID = @sectionID
 
	    UPDATE Tasks
	    SET 
            taskPosition = @newTaskPosition,
            sectionID = @sectionID
	    WHERE taskID = @taskID 
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