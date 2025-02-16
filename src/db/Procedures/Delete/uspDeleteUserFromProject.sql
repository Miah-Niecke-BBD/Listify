CREATE PROCEDURE uspRemoveUserFromProjects
@userID INT,
@projectID INT,
@teamLeaderID INT
AS
BEGIN
	BEGIN TRANSACTION 
	BEGIN TRY
		DECLARE @teamID INT, @isTeamLeader BIT

		SELECT @teamID = teamID FROM Projects
		WHERE projectID = @projectID
		
		SELECT @isTeamLeader = isTeamLeader FROM TeamMembers
		WHERE teamID = @teamID AND userID = @teamLeaderID;

        IF @isTeamLeader IS NULL
        BEGIN
			ROLLBACK;
            PRINT 'Only team leaders can remove user from the project.';
			RETURN;
        END

		IF NOT EXISTS (
            SELECT 1 FROM ProjectAssignees WHERE userID = @userID AND projectID =@projectID
        )
        BEGIN
            PRINT 'User is not a part of the project';
            ROLLBACK;
            RETURN;
        END;

		DELETE ta
		FROM 
			TaskAssignees ta
		INNER JOIN 
			Tasks t ON ta.taskID = t.taskID
		INNER JOIN 
			Sections s ON t.sectionID = s.sectionID
		WHERE 
			ta.userID = @UserID AND s.projectID = @ProjectID;

		DELETE FROM ProjectAssignees 
		WHERE projectID =@projectID AND userID = @userID 
		COMMIT;

	END TRY
	BEGIN CATCH
	ROLLBACK;
	END CATCH
END;
GO