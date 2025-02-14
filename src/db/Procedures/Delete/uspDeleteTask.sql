GO
CREATE PROCEDURE uspDeleteTask
@taskID INT,
@teamLeaderID INT  
AS
BEGIN 
	DECLARE @teamID INT, @isTeamLeader BIT , @sectionID INT;
	BEGIN TRANSACTION
	BEGIN TRY

		SELECT 
		@teamID = tm.teamID,
		@sectionID = t.sectionID,
		@isTeamLeader = tms.isTeamLeader
		FROM Tasks t
		JOIN Sections s ON s.sectionID = t.sectionID
		JOIN Projects p ON p.projectID = s.projectID
		JOIN Teams tm ON tm.teamID = p.teamID
		JOIN TeamMembers tms ON tms.teamID = tm.teamID
		WHERE t.taskID = @taskID;
	
 
		IF @isTeamLeader IS NULL
		BEGIN
			ROLLBACK;
			THROW 50001, 'Only team leaders can remove tasks from projects.', 1;
		END
	
		DELETE FROM Tasks
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