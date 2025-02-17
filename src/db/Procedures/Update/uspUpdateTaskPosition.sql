CREATE PROCEDURE listify.uspUpdateTaskPosition
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
	FROM listify.Tasks t
	JOIN listify.Sections s ON s.sectionID = t.sectionID
    JOIN listify.Projects p ON p.projectID = s.projectID
    JOIN listify.Teams tm ON tm.teamID = p.teamID
    WHERE t.taskID = @taskID;
 
	IF NOT EXISTS (
		SELECT 1 FROM listify.TeamMembers WHERE userID = @teamLeaderID AND TeamID = @teamID AND isTeamLeader = 1
	)
	 BEGIN
           PRINT 'Only team leaders can edit users to tasks';
		   ROLLBACK;
		   RETURN
     END;
 
	 IF EXISTS (
		SELECT 1 FROM listify.Sections WHERE sectionID = @sectionID AND projectID != @currentProjectID
	 )
	 BEGIN
	 		ROLLBACK;
            THROW 50084, 'Cannot move task to a section in a different project.',1;
     END
 
	  SELECT 
            @maxTaskPosition = MAX(taskPosition),
            @minTaskPosition = MIN(taskPosition)
        FROM listify.Tasks
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
	    UPDATE listify.Tasks
	    SET taskPosition = taskPosition - 1
	    WHERE taskPosition > @currentTaskPosition AND sectionID = @sectionID
	END
	ELSE
	 BEGIN 
	    UPDATE listify.Tasks
	    SET taskPosition = taskPosition - 1
	    WHERE taskPosition > @currentTaskPosition AND sectionID = @currentSectionID
	END
 
	    UPDATE listify.Tasks
	    SET taskPosition = taskPosition + 1
	    WHERE taskPosition >= @newTaskPosition AND sectionID = @sectionID
 
	    UPDATE listify.Tasks
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