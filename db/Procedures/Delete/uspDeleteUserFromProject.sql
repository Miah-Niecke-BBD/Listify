CREATE PROCEDURE [listify].uspRemoveUserFromProjects
@userID INT,
@projectID INT,
@teamLeaderID INT
AS
BEGIN
	BEGIN TRANSACTION 
	BEGIN TRY
		DECLARE @teamID INT, @isTeamLeader BIT

		SELECT @teamID = teamID FROM [listify].Projects
		WHERE projectID = @projectID
		
		SELECT @isTeamLeader = isTeamLeader FROM [listify].TeamMembers
		WHERE teamID = @teamID AND userID = @teamLeaderID;

        IF @isTeamLeader IS NULL
        BEGIN
			ROLLBACK;
            THROW 50014, 'Only team leaders can remove user from the project.',1;
        END

		IF NOT EXISTS (
            SELECT 1 FROM [listify].ProjectAssignees WHERE userID = @userID AND projectID =@projectID
        )
        BEGIN
            ROLLBACK;
            THROW 50015, 'User is not a part of the project',1;
        END;

		DELETE ta
		FROM 
			[listify].TaskAssignees ta
		INNER JOIN 
			[listify].Tasks t ON ta.taskID = t.taskID
		INNER JOIN 
			[listify].Sections s ON t.sectionID = s.sectionID
		WHERE 
			ta.userID = @UserID AND s.projectID = @ProjectID;

		DELETE FROM [listify].ProjectAssignees 
		WHERE projectID =@projectID AND userID = @userID 
		COMMIT;

	END TRY
	BEGIN CATCH
	ROLLBACK;
	END CATCH
END;
