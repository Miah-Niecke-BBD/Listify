GO
CREATE PROCEDURE uspCreateMyProjectsTeam
    @userID INT, 
    @gitHubID VARCHAR(2083) 
AS
BEGIN
    DECLARE @teamID INT, @projectID INT;

    BEGIN TRY
        

        INSERT INTO Teams (teamName, createdAt)
        VALUES ('MyProjects', SYSDATETIME());

        SET @teamID = SCOPE_IDENTITY();
     
        INSERT INTO TeamMembers (userID, teamID, isTeamLeader)
        VALUES (@userID, @teamID, 1);

        INSERT INTO Projects (teamID, projectName, projectDescription, createdAt, updatedAt)
        VALUES (@teamID, 'MyList', 'Default personal project', SYSDATETIME(), SYSDATETIME());

        SET @projectID = SCOPE_IDENTITY();

        INSERT INTO ProjectAssignees (userID, projectID)
        VALUES (@userID, @projectID);

    END TRY
    BEGIN CATCH
        THROW;
    END CATCH
END;
GO