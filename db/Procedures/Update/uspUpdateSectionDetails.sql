CREATE PROCEDURE listify.uspUpdateSectionDetails
    @sectionID INT,
    @userID INT, 
    @newSectionName VARCHAR(100)
AS
BEGIN
    BEGIN TRANSACTION;

    DECLARE @projectID INT;
    DECLARE @teamID INT;

    SELECT @projectID = projectID
    FROM listify.Sections
    WHERE sectionID = @sectionID;

    IF @projectID IS NULL
    BEGIN
        ROLLBACK;
        THROW 50070, 'Section does not exist.', 1;
    END

    SELECT @teamID = teamID
    FROM listify.Projects
    WHERE projectID = @projectID;

    IF NOT EXISTS (SELECT 1 FROM listify.TeamMembers WHERE userID = @userID AND teamID = @teamID)
    BEGIN
        ROLLBACK;
        THROW 50073, 'User is not part of the project team.', 1;
    END

    UPDATE listify.Sections
    SET sectionName = COALESCE(@newSectionName, sectionName),
        updatedAt = GETDATE()
    WHERE sectionID = @sectionID;

    COMMIT TRANSACTION;
END;
