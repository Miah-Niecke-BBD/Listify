GO
CREATE PROCEDURE [listify].uspDeleteTeam
    @teamID INT,
    @teamLeaderID INT
AS
BEGIN
    BEGIN TRANSACTION;
    BEGIN TRY
        DECLARE @isTeamLeader BIT;

        SELECT @isTeamLeader = isTeamLeader 
        FROM [listify].TeamMembers WITH (UPDLOCK, HOLDLOCK)
        WHERE userID = @teamLeaderID AND teamID = @teamID;

        IF @isTeamLeader IS NULL OR @isTeamLeader = 0
        BEGIN
            ROLLBACK;
            THROW 50012, 'Only the team leader can delete this team.', 1;
        END


        DELETE FROM [listify].TaskDependencies
        WHERE taskID IN (SELECT taskID FROM [listify].Tasks WHERE sectionID IN 
            (SELECT sectionID FROM [listify].Sections WHERE projectID IN 
                (SELECT projectID FROM [listify].Projects WHERE teamID = @teamID)))
        OR dependentTaskID IN (SELECT taskID FROM [listify].Tasks WHERE sectionID IN 
            (SELECT sectionID FROM [listify].Sections WHERE projectID IN 
                (SELECT projectID FROM [listify].Projects WHERE teamID = @teamID)));

        DELETE FROM [listify].TaskAssignees
        WHERE taskID IN (SELECT taskID FROM [listify].Tasks WHERE sectionID IN 
            (SELECT sectionID FROM [listify].Sections WHERE projectID IN 
                (SELECT projectID FROM [listify].Projects WHERE teamID = @teamID)));

        DELETE FROM [listify].Tasks
        WHERE sectionID IN (SELECT sectionID FROM [listify].Sections WHERE projectID IN 
            (SELECT projectID FROM [listify].Projects WHERE teamID = @teamID));

        DELETE FROM [listify].Sections
        WHERE projectID IN (SELECT projectID FROM [listify].Projects WHERE teamID = @teamID);

        DELETE FROM [listify].ProjectAssignees
        WHERE projectID IN (SELECT projectID FROM [listify].Projects WHERE teamID = @teamID);

        DELETE FROM [listify].Projects
        WHERE teamID = @teamID;

        DELETE FROM [listify].TeamMembers
        WHERE teamID = @teamID;

        DELETE FROM [listify].Teams
        WHERE teamID = @teamID;

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;
GO