GO
CREATE PROCEDURE uspUpdateTeamLeader
	@teamLeaderID
	@teamID
	@newTeamLeaderID
AS
BEGIN 
	BEGIN TRANSACTION
	BEGIN TRY

	IF NOT EXISTS (
		SELECT 1 FROM TeamMembers WHERE userID = @teamLeaderID AND TeamID = @teamID AND isTeamLeader = 1
	)
	 BEGIN
		   ROLLBACK;
           PRINT'Only team leaders can reassign a team leader';
     END;

	 UPDATE TeamMembers 
	 SET isTeamLeader = 1
	 WHERE UserID = @newTeamLeaderID

	 UPDATE TeamMembers 
	 SET isTeamLeader = 0
	 WHERE UserID = @teamLeaderID

	 COMMIT;
	 END TRY
	 BEGIN CATCH
		ROLLBACK;
		DECLARE @ErrorMessage NVARCHAR(MAX) = ERROR_MESSAGE();
        PRINT 'Error occurred: ' + @ErrorMessage;
        THROW;
	END CATCH
END
GO