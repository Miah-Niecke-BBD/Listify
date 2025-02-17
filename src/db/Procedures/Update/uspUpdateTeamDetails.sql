GO
CREATE PROCEDURE listify.uspUpdateTeamDetails
    @teamID INT,
    @teamLeaderID INT, 
    @newTeamName VARCHAR(100) = NULL
AS
BEGIN
    BEGIN TRANSACTION;

    DECLARE @isTeamLeader BIT;

    SELECT @isTeamLeader = isTeamLeader
    FROM listify.TeamMembers
    WHERE userID = @teamLeaderID AND teamID = @teamID;

    IF @isTeamLeader <> 1
    BEGIN
        ROLLBACK;
        THROW 50086, 'Only team leaders can update teams.',1;
    END

    UPDATE listify.Teams
    SET 
        teamName = COALESCE(@newTeamName, teamName),  
        updatedAt = GETDATE()  
    WHERE teamID = @teamID;

    COMMIT TRANSACTION;
END;
GO