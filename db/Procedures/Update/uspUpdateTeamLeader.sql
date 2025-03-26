
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

DECLARE @projectID INT;

	DECLARE project_cursor CURSOR FOR
		SELECT p.ProjectID
		FROM listify.Projects p
		WHERE p.TeamID = @teamID;

	OPEN project_cursor;

	FETCH NEXT FROM project_cursor INTO @projectID;

	WHILE @@FETCH_STATUS = 0
	BEGIN
		IF NOT EXISTS (
			SELECT 1
			FROM listify.ProjectAssignees pa
			WHERE pa.ProjectID = @projectID
			AND pa.UserID = @newTeamLeaderID
		)
		BEGIN
			INSERT INTO listify.ProjectAssignees (UserID, ProjectID)
			VALUES (@newTeamLeaderID, @projectID);
		END

		FETCH NEXT FROM project_cursor INTO @projectID;
	END

	CLOSE project_cursor;
	DEALLOCATE project_cursor;

	 COMMIT;
	 END TRY
	 BEGIN CATCH
		ROLLBACK;
		DECLARE @ErrorMessage NVARCHAR(MAX) = ERROR_MESSAGE();
        PRINT 'Error occurred: ' + @ErrorMessage;
        THROW;
	END CATCH
END
