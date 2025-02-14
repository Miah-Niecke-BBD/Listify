GO
CREATE PROCEDURE uspUpdateTeamDetails
    @teamID INT,
    @teamLeaderID INT, 
    @newTeamName VARCHAR(100) = NULL
AS
BEGIN
    BEGIN TRANSACTION;

    DECLARE @isTeamLeader BIT;

    SELECT @isTeamLeader = isTeamLeader
    FROM TeamMembers
    WHERE userID = @teamLeaderID AND teamID = @teamID;

    IF @isTeamLeader <> 1
    BEGIN
        PRINT 'Only team leaders can update teams.';
        ROLLBACK;
    END

    UPDATE Teams
    SET 
        teamName = COALESCE(@newTeamName, teamName),  
        updatedAt = GETDATE()  
    WHERE teamID = @teamID;

    COMMIT TRANSACTION;
END;
GO