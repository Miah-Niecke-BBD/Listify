GO
CREATE PROCEDURE uspRemoveUser
@userID INT 
AS
BEGIN
	BEGIN TRANSACTION
	BEGIN TRY
		DECLARE @teamCount INT, @teamID INT

		SELECT @teamCount = COUNT(*) 
		FROM TeamMembers WHERE userID = @userID AND isTeamLeader =1;

		IF @teamCount > 0 
		BEGIN
			DELETE FROM Teams
			WHERE teamID IN(SELECT teamID FROM TeamMembers WHERE userID = @userID AND isTeamLeader = 1)
		END

		DELETE FROM Users
		WHERE userID = @userID
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


