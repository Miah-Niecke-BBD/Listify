GO
CREATE PROCEDURE listify.uspCreateMyProjectsTeam
    @userID INT, 
    @gitHubID VARCHAR(2083) 
AS
BEGIN
    DECLARE @teamID INT, @projectID INT;

    BEGIN TRY
        

        INSERT INTO listify.Teams (teamName, createdAt)
        VALUES ('MyProjects', SYSDATETIME());

        SET @teamID = SCOPE_IDENTITY();
     
        INSERT INTO listify.TeamMembers (userID, teamID, isTeamLeader)
        VALUES (@userID, @teamID, 1);

        INSERT INTO listify.Projects (teamID, projectName, projectDescription, createdAt)
        VALUES (@teamID, 'MyList', 'Random Tasks', SYSDATETIME());

        INSERT INTO listify.Projects (teamID, projectName, projectDescription, createdAt)
        VALUES (@teamID, 'Home','Default personal project', SYSDATETIME());

        SET @projectID = SCOPE_IDENTITY();

        INSERT INTO listify.ProjectAssignees (userID, projectID)
        VALUES (@userID, @projectID);

    END TRY
    BEGIN CATCH
        PRINT 'Error occured:' + ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO