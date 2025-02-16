GO
CREATE PROCEDURE uspDeleteTask
    @taskID INT,
    @teamLeaderID INT  
AS
BEGIN 
    DECLARE @teamID INT, @isTeamLeader BIT, @sectionID INT, @taskPosition TINYINT;

    BEGIN TRANSACTION;
    BEGIN TRY

        SELECT 
            @teamID = tm.teamID,
            @sectionID = t.sectionID,
            @taskPosition = t.taskPosition,
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
            THROW 50003, 'Task does not exist.', 1;
        END

        IF @isTeamLeader IS NULL OR @isTeamLeader = 0
        BEGIN
            ROLLBACK;
            THROW 50004, 'Only the team leader can delete this task.', 1;
        END


        DELETE FROM TaskDependencies WHERE taskID = @taskID OR dependentTaskID = @taskID;
        DELETE FROM TaskAssignees WHERE taskID = @taskID;
        DELETE FROM Tasks WHERE taskID = @taskID;

 
        UPDATE Tasks
        SET taskPosition = taskPosition - 1
        WHERE sectionID = @sectionID AND taskPosition > @taskPosition;

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;