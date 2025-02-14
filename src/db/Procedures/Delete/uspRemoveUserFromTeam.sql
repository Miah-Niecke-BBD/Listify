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

		

		SELECT @isTeamLeader = isTeamLeader 
        FROM TeamMembers tm
        WHERE tm.userID = @teamLeaderID AND tm.isTeamLeader = 1 AND teamID =@teamID;

        IF @isTeamLeader IS NULL
        BEGIN
            PRINT'Only team leader can remove user.';
			ROLLBACK;
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