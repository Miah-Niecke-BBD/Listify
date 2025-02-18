
CREATE PROCEDURE listify.uspUpdateProjectDetails
    @projectID INT,
    @teamLeaderID INT,
    @newProjectName VARCHAR(100) = NULL,
    @newProjectDescription VARCHAR(500) = NULL
AS
BEGIN
    BEGIN TRANSACTION;

    DECLARE @teamID INT;
    DECLARE @isTeamLeader BIT;

    SELECT @teamID = teamID
    FROM listify.Projects
    WHERE projectID = @projectID;

    IF @teamID IS NULL
    BEGIN
        ROLLBACK;
        THROW 50062, 'Project does not exist.',1;
    END

    SELECT @isTeamLeader = isTeamLeader
    FROM listify.TeamMembers
    WHERE userID = @teamLeaderID AND teamID = @teamID;

    IF @isTeamLeader <> 1
    BEGIN
        ROLLBACK;
        THROW 50067, 'Only team leaders can update projects.',1;
    END

    UPDATE listify.Projects
    SET 
        projectName = COALESCE(@newProjectName, projectName),
        projectDescription = COALESCE(@newProjectDescription, projectDescription),
        updatedAt = GETDATE() 
    WHERE projectID = @projectID;

    COMMIT TRANSACTION;
END;

