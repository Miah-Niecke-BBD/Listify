GO
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
            THROW 50001, 'Only team leaders can remove user from the project.', 1;
        END

		DELETE FROM ProjectAssignees 
		WHERE projectID =@projectID AND userID = @userID 
		COMMIT;

	END TRY
	BEGIN CATCH
	ROLLBACK;
	END CATCH
END;
GO