GO
CREATE PROCEDURE uspDeleteUserFromTask
    @userID INT,
    @taskID INT,
    @teamLeaderID INT  
AS
BEGIN 
    DECLARE @teamID INT, @isTeamLeader BIT;

    BEGIN TRANSACTION;
    BEGIN TRY

        SELECT 
            @teamID = tm.teamID,
            @isTeamLeader = tms.isTeamLeader
        FROM Tasks t
        JOIN Sections s ON s.sectionID = t.sectionID
        JOIN Projects p ON p.projectID = s.projectID
        JOIN Teams tm ON tm.teamID = p.teamID
        JOIN TeamMembers tms ON tms.teamID = tm.teamID AND tms.userID = @teamLeaderID
        WHERE t.taskID = @taskID;

        IF @teamID IS NULL
        BEGIN
            ROLLBACK;
            THROW 50006, 'Task does not exist.',1;
        END

        IF @isTeamLeader IS NULL OR @isTeamLeader = 0
        BEGIN
            ROLLBACK;
            THROW 50007, 'Only the team leader of the associated team can remove users from this task.',1;
        END


        DELETE FROM TaskAssignees
        WHERE userID = @userID AND taskID = @taskID;


        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;
GO