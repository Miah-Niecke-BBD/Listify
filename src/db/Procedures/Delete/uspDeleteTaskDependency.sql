GO
CREATE PROCEDURE uspDeleteTaskDependency
	@taskID INT,
	@taskDependencyID INT,
	@teamLeaderID INT
AS
BEGIN

	DECLARE @teamID INT;
	BEGIN TRANSACTION
	BEGIN TRY

	SELECT 
		@teamID = tm.teamID
	FROM Tasks t
	JOIN Sections s ON s.sectionID = t.sectionID
    JOIN Projects p ON p.projectID = s.projectID
    JOIN Teams tm ON tm.teamID = p.teamID
    WHERE t.taskID = @taskID;

	IF NOT EXISTS (
		SELECT 1 FROM TeamMembers WHERE userID = @teamLeaderID AND TeamID = @teamID AND isTeamLeader = 1
	)
	 BEGIN
			ROLLBACK;
           THROW 50011, 'Only team leaders can edit tasks',1;
     END;

	 DELETE TaskDependencies
	 WHERE taskID = @taskID AND dependentTaskID = @taskDependencyID

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
