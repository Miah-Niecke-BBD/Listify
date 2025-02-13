GO
CREATE PROCEDURE uspRemoveUserFromTask
	@userID INT,
	@taskID INT,
	@assigningUserID INT  
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
		SELECT 1 FROM TeamMembers WHERE userID = @assigningUserID AND TeamID = @teamID AND isTeamLeader = 1
	)
	 BEGIN
           THROW 50006, 'Only team leaders can delete users to tasks', 1;
     END;

	 DELETE FROM TaskAssignees
	 WHERE userID = @userID AND taskID = @taskID

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
