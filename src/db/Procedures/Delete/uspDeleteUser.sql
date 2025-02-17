GO
CREATE PROCEDURE [listify].uspRemoveUser
@userID INT 
AS
BEGIN
	BEGIN TRANSACTION
	BEGIN TRY
		DECLARE @teamCount INT, @teamID INT

		SELECT @teamCount = COUNT(*) 
		FROM [listify].TeamMembers WHERE userID = @userID AND isTeamLeader =1;

		IF @teamCount > 0 
		BEGIN
			DELETE FROM [listify].Teams
			WHERE teamID IN(SELECT teamID FROM [listify].TeamMembers WHERE userID = @userID AND isTeamLeader = 1)
		END

		DELETE FROM [listify].Users
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


