GO
CREATE PROCEDURE uspUpdateProjectDetails
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
    FROM Projects
    WHERE projectID = @projectID;

    IF @teamID IS NULL
    BEGIN
        PRINT 'Project does not exist.';
        ROLLBACK;
    END

    SELECT @isTeamLeader = isTeamLeader
    FROM TeamMembers
    WHERE userID = @teamLeaderID AND teamID = @teamID;

    IF @isTeamLeader <> 1
    BEGIN
        PRINT 'Only team leaders can update projects.';
        ROLLBACK;
    END

    UPDATE Projects
    SET 
        projectName = COALESCE(@newProjectName, projectName),
        projectDescription = COALESCE(@newProjectDescription, projectDescription),
        updatedAt = GETDATE() 
    WHERE projectID = @projectID;

    COMMIT TRANSACTION;
END;
GO
