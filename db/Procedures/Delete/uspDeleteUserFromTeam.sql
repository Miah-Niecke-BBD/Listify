CREATE PROCEDURE listify.uspDeleteUserFromTeam
@userID INT,
@teamID INT,
@teamLeaderID INT
AS
BEGIN
	BEGIN TRANSACTION 
	BEGIN TRY
		DECLARE @varteamID INT, @isTeamLeader BIT

		SELECT @isTeamLeader = isTeamLeader 
        FROM [listify].TeamMembers tm
        WHERE tm.userID = @teamLeaderID AND tm.isTeamLeader = 1 AND teamID =@teamID;

        IF @isTeamLeader IS NULL
        BEGIN
            THROW 50016,'Only team leader can remove user.',1;
        END

		IF NOT EXISTS (
            SELECT 1 FROM [listify].TeamMembers WHERE userID = @userID AND teamID =@teamID
        )
		BEGIN
            THROW 50018, 'User is not a part of the team',1;
        END;


		DELETE FROM [listify].TaskAssignees 
		Where taskID IN(SELECT taskID FROM [listify].Tasks
		WHERE sectionID IN(SELECT projectID FROM [listify].Projects
		WHERE teamID IN (SELECT teamID FROM [listify].TeamMembers
		WHERE userID = @userID AND teamID = @teamID)));

		DELETE PA
        FROM [listify].ProjectAssignees AS PA
        INNER JOIN [listify].Projects AS P ON PA.ProjectID = P.projectID
        INNER JOIN [listify].TeamMembers AS TM ON P.teamID = TM.teamID
        WHERE TM.teamID = @teamID AND TM.userID = @userID AND PA.userID = @userID;

		DELETE FROM [listify].TeamMembers
		WHERE userID = @userID AND teamID = @teamID;

		COMMIT;
	END TRY
	BEGIN CATCH
	ROLLBACK;
	END CATCH
END;
