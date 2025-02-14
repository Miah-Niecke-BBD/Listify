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

CREATE PROCEDURE uspRemoveUserFromTeam
@userID INT,
@teamID INT,
@teamLeaderID INT
AS
BEGIN
	BEGIN TRANSACTION 
	BEGIN TRY
		DECLARE @varteamID INT, @isTeamLeader BIT

		

		SELECT @isTeamLeader = isTeamLeader, 
               @varteamID = tm.teamID
        FROM TeamMembers tm
        WHERE tm.userID = @teamLeaderID AND tm.isTeamLeader = 1 AND teamID =@teamID;

        IF @isTeamLeader IS NULL
        BEGIN
            THROW 50001, 'Only team leader can remove user.', 1;
        END

		DELETE FROM TaskAssignees 
		Where taskID IN(SELECT taskID FROM Tasks
		WHERE sectionID IN(SELECT projectID FROM Projects
		WHERE teamID IN (SELECT teamID FROM TeamMember
		WHERE userID = @userID AND teamID = @teamID)));

		DELETE FROM ProjectAssignees
		WHERE ProjectID IN(SELECT projectID FROM Project
		WHERE teamID IN(SELECT teamID FROM TeamMemebers
		WHERE teamID =@teamID AND userID = @userID) );

		DELETE FROM TeamMembers
		WHERE userID = @userID AND teamID = @teamID;

		COMMIT;
	END TRY
	BEGIN CATCH
	ROLLBACK;
	END CATCH
END;
GO

