GO
CREATE PROCEDURE listify.uspUpdateTeamLeader
	@teamLeaderID INT,
	@teamID INT,
	@newTeamLeaderID INT
AS
BEGIN 
	BEGIN TRANSACTION
	BEGIN TRY

	IF NOT EXISTS (
		SELECT 1 FROM listify.TeamMembers WHERE userID = @teamLeaderID AND TeamID = @teamID AND isTeamLeader = 1
	)
	 BEGIN
	 		ROLLBACK;
           THROW 50091,'Only team leaders can reassign a team leader',1;
     END;

	 UPDATE listify.TeamMembers 
	 SET isTeamLeader = 1
	 WHERE UserID = @newTeamLeaderID

	 UPDATE listify.TeamMembers 
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