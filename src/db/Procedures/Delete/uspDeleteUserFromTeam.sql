CREATE PROCEDURE uspDeleteUserFromTeam
@userID INT,
@teamID INT,
@teamLeaderID INT
AS
BEGIN
	BEGIN TRANSACTION 
	BEGIN TRY
		DECLARE @varteamID INT, @isTeamLeader BIT

		SELECT @isTeamLeader = isTeamLeader 
        FROM TeamMembers tm
        WHERE tm.userID = @teamLeaderID AND tm.isTeamLeader = 1 AND teamID =@teamID;

        IF @isTeamLeader IS NULL
        BEGIN
            PRINT'Only team leader can remove user.';
			ROLLBACK;
			RETURN;
        END

		IF NOT EXISTS (
            SELECT 1 FROM TeamMembers WHERE userID = @userID AND teamID =@teamID
        )
		BEGIN
            PRINT 'User is not a part of the team';
            ROLLBACK;
            RETURN;
        END;


		DELETE FROM TaskAssignees 
		Where taskID IN(SELECT taskID FROM Tasks
		WHERE sectionID IN(SELECT projectID FROM Projects
		WHERE teamID IN (SELECT teamID FROM TeamMembers
		WHERE userID = @userID AND teamID = @teamID)));

		DELETE FROM ProjectAssignees
		WHERE ProjectID IN(SELECT projectID FROM Projects
		WHERE teamID IN(SELECT teamID FROM TeamMembers
		WHERE teamID = @teamID AND userID = @userID) );

		DELETE FROM TeamMembers
		WHERE userID = @userID AND teamID = @teamID;

		COMMIT;
	END TRY
	BEGIN CATCH
	ROLLBACK;
	END CATCH
END;
GO