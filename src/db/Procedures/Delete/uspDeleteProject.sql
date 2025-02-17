GO
CREATE PROCEDURE [listify].uspDeleteProject
    @projectID INT,
    @teamLeaderID INT  
AS
BEGIN 
    DECLARE @teamID INT, @isTeamLeader BIT;

    BEGIN TRANSACTION;
    BEGIN TRY
        
        SELECT @teamID = p.teamID
        FROM [listify].Projects p WITH (UPDLOCK, HOLDLOCK)
        WHERE p.projectID = @projectID;

        IF @teamID IS NULL
        BEGIN
            ROLLBACK;
            THROW 50008, 'Project does not exist.', 1;
        END


        SELECT @isTeamLeader = isTeamLeader
        FROM [listify].TeamMembers WITH (UPDLOCK, HOLDLOCK)
        WHERE teamID = @teamID AND userID = @teamLeaderID AND isTeamLeader = 1;

        IF @isTeamLeader IS NULL OR @isTeamLeader = 0
        BEGIN
            ROLLBACK;
            THROW 50009, 'Only the team leader can delete this project.', 1;
        END


        DELETE FROM [listify].TaskDependencies
        WHERE taskID IN (SELECT taskID FROM [listify].Tasks WHERE sectionID IN (SELECT sectionID FROM [listify].Sections WHERE projectID = @projectID))
           OR dependentTaskID IN (SELECT taskID FROM [listify].Tasks WHERE sectionID IN (SELECT sectionID FROM [listify].Sections WHERE projectID = @projectID));

        DELETE FROM [listify].TaskAssignees
        WHERE taskID IN (SELECT taskID FROM [listify].Tasks WHERE sectionID IN (SELECT sectionID FROM [listify].Sections WHERE projectID = @projectID));

        DELETE FROM [listify].Tasks WHERE sectionID IN (SELECT sectionID FROM [listify].Sections WHERE projectID = @projectID);
        DELETE FROM [listify].Sections WHERE projectID = @projectID;
        DELETE FROM [listify].ProjectAssignees WHERE projectID = @projectID;
        DELETE FROM [listify].Projects WHERE projectID = @projectID;

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;
GO
