GO
CREATE PROCEDURE [listify].uspDeleteTask
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
        FROM [listify].Tasks t
        JOIN [listify].Sections s ON s.sectionID = t.sectionID
        JOIN [listify].Projects p ON p.projectID = s.projectID
        JOIN [listify].Teams tm ON tm.teamID = p.teamID
        JOIN [listify].TeamMembers tms ON tms.teamID = tm.teamID AND tms.userID = @teamLeaderID
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


        DELETE FROM [listify].TaskDependencies WHERE taskID = @taskID OR dependentTaskID = @taskID;
        DELETE FROM [listify].TaskAssignees WHERE taskID = @taskID;
        DELETE FROM [listify].Tasks WHERE taskID = @taskID;

 
        UPDATE [listify].Tasks
        SET taskPosition = taskPosition - 1
        WHERE sectionID = @sectionID AND taskPosition > @taskPosition;

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
		DECLARE @ErrorMessage NVARCHAR(MAX) = ERROR_MESSAGE();
        PRINT 'Error occurred: ' + @ErrorMessage;
        THROW;
    END CATCH
END;