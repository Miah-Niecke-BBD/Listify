CREATE PROCEDURE listify.uspUpdateProjectDetails
    @projectID INT,
    @userID INT,
    @newProjectName VARCHAR(100) = NULL,
    @newProjectDescription VARCHAR(500) = NULL
AS
BEGIN
    BEGIN TRANSACTION;

    DECLARE @teamID INT;

    SELECT @teamID = teamID
    FROM listify.Projects
    WHERE projectID = @projectID;

    IF @teamID IS NULL
    BEGIN
        ROLLBACK;
        THROW 50062, 'Project does not exist.', 1;
    END

    IF NOT EXISTS (SELECT 1 FROM listify.TeamMembers WHERE userID = @userID AND teamID = @teamID)
    BEGIN
        ROLLBACK;
        THROW 50068, 'User is not part of the project team.', 1;
    END

    UPDATE listify.Projects
    SET 
        projectName = COALESCE(@newProjectName, projectName),
        projectDescription = COALESCE(@newProjectDescription, projectDescription),
        updatedAt = GETDATE() 
    WHERE projectID = @projectID;

    COMMIT TRANSACTION;
END;



