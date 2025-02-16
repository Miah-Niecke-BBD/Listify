GO
CREATE PROCEDURE uspDeleteTeam
    @teamID INT,
    @teamLeaderID INT
AS
BEGIN
    BEGIN TRANSACTION;
    BEGIN TRY
        DECLARE @isTeamLeader BIT;

        SELECT @isTeamLeader = isTeamLeader 
        FROM TeamMembers WITH (UPDLOCK, HOLDLOCK)
        WHERE userID = @teamLeaderID AND teamID = @teamID;

        IF @isTeamLeader IS NULL OR @isTeamLeader = 0
        BEGIN
            ROLLBACK;
            THROW 50012, 'Only the team leader can delete this team.', 1;
        END


        DELETE FROM TaskDependencies
        WHERE taskID IN (SELECT taskID FROM Tasks WHERE sectionID IN 
            (SELECT sectionID FROM Sections WHERE projectID IN 
                (SELECT projectID FROM Projects WHERE teamID = @teamID)))
        OR dependentTaskID IN (SELECT taskID FROM Tasks WHERE sectionID IN 
            (SELECT sectionID FROM Sections WHERE projectID IN 
                (SELECT projectID FROM Projects WHERE teamID = @teamID)));

        DELETE FROM TaskAssignees
        WHERE taskID IN (SELECT taskID FROM Tasks WHERE sectionID IN 
            (SELECT sectionID FROM Sections WHERE projectID IN 
                (SELECT projectID FROM Projects WHERE teamID = @teamID)));

        DELETE FROM Tasks
        WHERE sectionID IN (SELECT sectionID FROM Sections WHERE projectID IN 
            (SELECT projectID FROM Projects WHERE teamID = @teamID));

        DELETE FROM Sections
        WHERE projectID IN (SELECT projectID FROM Projects WHERE teamID = @teamID);

        DELETE FROM ProjectAssignees
        WHERE projectID IN (SELECT projectID FROM Projects WHERE teamID = @teamID);

        DELETE FROM Projects
        WHERE teamID = @teamID;

        DELETE FROM TeamMembers
        WHERE teamID = @teamID;

        DELETE FROM Teams
        WHERE teamID = @teamID;

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;
GO