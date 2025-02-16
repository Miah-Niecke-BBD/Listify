GO
CREATE PROCEDURE uspDeleteProject
    @projectID INT,
    @teamLeaderID INT  
AS
BEGIN 
    DECLARE @teamID INT, @isTeamLeader BIT;

    BEGIN TRANSACTION;
    BEGIN TRY
        
        SELECT @teamID = p.teamID
        FROM Projects p WITH (UPDLOCK, HOLDLOCK)
        WHERE p.projectID = @projectID;

        IF @teamID IS NULL
        BEGIN
            ROLLBACK;
            THROW 50008, 'Project does not exist.', 1;
        END


        SELECT @isTeamLeader = isTeamLeader
        FROM TeamMembers WITH (UPDLOCK, HOLDLOCK)
        WHERE teamID = @teamID AND userID = @teamLeaderID AND isTeamLeader = 1;

        IF @isTeamLeader IS NULL OR @isTeamLeader = 0
        BEGIN
            ROLLBACK;
            THROW 50009, 'Only the team leader can delete this project.', 1;
        END


        DELETE FROM TaskDependencies
        WHERE taskID IN (SELECT taskID FROM Tasks WHERE sectionID IN (SELECT sectionID FROM Sections WHERE projectID = @projectID))
           OR dependentTaskID IN (SELECT taskID FROM Tasks WHERE sectionID IN (SELECT sectionID FROM Sections WHERE projectID = @projectID));

        DELETE FROM TaskAssignees
        WHERE taskID IN (SELECT taskID FROM Tasks WHERE sectionID IN (SELECT sectionID FROM Sections WHERE projectID = @projectID));

        DELETE FROM Tasks WHERE sectionID IN (SELECT sectionID FROM Sections WHERE projectID = @projectID);
        DELETE FROM Sections WHERE projectID = @projectID;
        DELETE FROM ProjectAssignees WHERE projectID = @projectID;
        DELETE FROM Projects WHERE projectID = @projectID;

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;
GO
