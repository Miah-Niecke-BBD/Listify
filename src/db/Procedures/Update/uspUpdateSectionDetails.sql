GO
CREATE PROCEDURE listify.uspUpdateSectionDetails
    @sectionID INT,
    @teamLeaderID INT, 
    @newSectionName VARCHAR(100)
AS
BEGIN
    BEGIN TRANSACTION;

    DECLARE @projectID INT;
    DECLARE @teamID INT;
    DECLARE @isTeamLeader BIT;

    SELECT @projectID = projectID
    FROM listify.Sections
    WHERE sectionID = @sectionID;

    IF @projectID IS NULL
    BEGIN
        ROLLBACK;
        THROW 50070, 'Section does not exist.',1;
    END

    SELECT @teamID = teamID
    FROM listify.Projects
    WHERE projectID = @projectID;

    SELECT @isTeamLeader = isTeamLeader
    FROM listify.TeamMembers
    WHERE userID = @teamLeaderID AND teamID = @teamID;

    IF @isTeamLeader <> 1
    BEGIN
        ROLLBACK;
        THROW 50072, 'Only team leaders can update sections.',1;
    END

    UPDATE listify.Sections
    SET sectionName = COALESCE(@newSectionName,sectionName),
        updatedAt = GETDATE()
    WHERE sectionID = @sectionID;

    COMMIT TRANSACTION;
END;
GO