GO
CREATE PROCEDURE uspAssignTaskDependency
    @teamLeaderID INT,              
    @taskID INT,              
    @dependentTaskID INT      
AS
BEGIN
    DECLARE @teamID INT, @projectID INT, @dependentTaskProjectID INT;
    DECLARE @teamLeader BIT;

    BEGIN TRANSACTION
    BEGIN TRY

        SELECT @teamID = tm.teamID, 
               @projectID = p.projectID
        FROM Tasks t
        JOIN Sections s ON s.sectionID = t.sectionID
        JOIN Projects p ON p.projectID = s.projectID
        JOIN Teams tm ON tm.teamID = p.teamID
        WHERE t.taskID = @taskID;

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @teamLeaderID AND teamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            PRINT 'Only team leaders can assign dependencies';
            ROLLBACK;
        END;

        SELECT @dependentTaskProjectID = p.projectID
        FROM Tasks t
        JOIN Sections s ON s.sectionID = t.sectionID
        JOIN Projects p ON p.projectID = s.projectID
        WHERE t.taskID = @dependentTaskID;

        IF @projectID <> @dependentTaskProjectID
        BEGIN
            PRINT 'The dependent task does not belong to the same project';
            ROLLBACK;
        END;

        IF @taskID = @dependentTaskID
        BEGIN
            PRINT 'A task cannot depend on itself';
            ROLLBACK;
        END;

        IF EXISTS (
            SELECT 1 FROM TaskDependencies 
            WHERE taskID = @taskID AND dependentTaskID = @dependentTaskID
        )
        BEGIN
            PRINT 'Dependency already exists';
            ROLLBACK;
        END;

        IF EXISTS (
            SELECT 1 FROM TaskDependencies td
            JOIN TaskDependencies tsd ON td.dependentTaskID = tsd.taskID
            WHERE td.taskID = @dependentTaskID AND tsd.dependentTaskID = @taskID
        )
        BEGIN
            PRINT 'Circular dependency detected';
            ROLLBACK;
        END;

        INSERT INTO TaskDependencies (taskID, dependentTaskID)
        VALUES (@taskID, @dependentTaskID);

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        DECLARE @ErrorMessage NVARCHAR(MAX) = ERROR_MESSAGE();
        PRINT 'Error occurred: ' + @ErrorMessage;
        THROW;
    END CATCH
END;
GO