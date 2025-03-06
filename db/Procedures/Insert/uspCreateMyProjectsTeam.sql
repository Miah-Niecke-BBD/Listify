IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'uspCreateMyProjectsTeam')
BEGIN
    DROP PROCEDURE listify.uspCreateMyProjectsTeam;
END;
 
GO
 
CREATE PROCEDURE listify.uspCreateMyProjectsTeam
    @userID INT, 
    @gitHubID VARCHAR(2083) 
AS
BEGIN
    DECLARE @teamID INT, @projectID INT;
 
    BEGIN TRY
        INSERT INTO listify.Teams (teamName, createdAt)
        VALUES ('MyProjects', SYSDATETIMEOFFSET());
 
        SET @teamID = SCOPE_IDENTITY();
        INSERT INTO listify.TeamMembers (userID, teamID, isTeamLeader)
        VALUES (@userID, @teamID, 1);
 
        INSERT INTO listify.Projects (teamID, projectName, projectDescription, createdAt)
        VALUES (@teamID, 'MyList', 'Random Tasks', SYSDATETIMEOFFSET());
 
        SET @projectID = SCOPE_IDENTITY();
 
        INSERT INTO listify.ProjectAssignees (userID, projectID)
        VALUES (@userID, @projectID);
 
        INSERT INTO listify.Projects (teamID, projectName, projectDescription, createdAt)
        VALUES (@teamID, 'Home','Default personal project', SYSDATETIMEOFFSET());
 
        SET @projectID = SCOPE_IDENTITY();
 
        INSERT INTO listify.ProjectAssignees (userID, projectID)
        VALUES (@userID, @projectID);
 
    END TRY
    BEGIN CATCH    
        THROW 50050, 'Could not create the default projects', 1;
    END CATCH
END;