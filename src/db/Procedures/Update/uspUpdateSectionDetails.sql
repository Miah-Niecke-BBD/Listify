GO
CREATE PROCEDURE uspUpdateSectionDetails
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
    FROM Sections
    WHERE sectionID = @sectionID;

    -- Check if the project exists
    IF @projectID IS NULL
    BEGIN
        PRINT 'Section does not exist.';
    END

    SELECT @teamID = teamID
    FROM Projects
    WHERE projectID = @projectID;

    SELECT @isTeamLeader = isTeamLeader
    FROM TeamMembers
    WHERE userID = @teamLeaderID AND teamID = @teamID;

    IF @isTeamLeader <> 1
    BEGIN
        PRINT 'Only team leaders can update sections.';
    END

    UPDATE Sections
    SET sectionName = COALESCE(@newSectionName,sectionName),
        updatedAt = GETDATE()
    WHERE sectionID = @sectionID;

    COMMIT TRANSACTION;
END;
GO