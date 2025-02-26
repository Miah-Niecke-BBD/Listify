CREATE PROCEDURE listify.uspUpdateTeamDetails
    @userID INT,
    @teamID INT, 
    @newTeamName VARCHAR(100) = NULL
AS
BEGIN
    BEGIN TRANSACTION;

    DECLARE @isTeamMember BIT;


    SELECT @isTeamMember = 1
    FROM listify.TeamMembers
    WHERE userID = @userID AND teamID = @teamID;

    IF @isTeamMember IS NULL
    BEGIN
        ROLLBACK;
        THROW 50087, 'User is not a member of the team.', 1;
    END


    UPDATE listify.Teams
    SET 
        teamName = COALESCE(@newTeamName, teamName),
        updatedAt = GETDATE()
    WHERE teamID = @teamID;

    COMMIT TRANSACTION;
END;
