
CREATE PROCEDURE listify.uspAssignTaskDependency
    @teamLeaderID INT,              
    @taskID INT,              
    @dependentTaskID INT      
AS
BEGIN
    DECLARE @teamID INT, @projectID INT, @dependentTaskProjectID INT;
    DECLARE @teamLeader BIT;
    DECLARE @currentTaskID INT;

    BEGIN TRANSACTION
    BEGIN TRY
        SET DEADLOCK_PRIORITY LOW;

        SELECT @teamID = tm.teamID, 
               @projectID = p.projectID
        FROM listify.Tasks t
        JOIN listify.Sections s ON s.sectionID = t.sectionID
        JOIN listify.Projects p ON p.projectID = s.projectID
        JOIN listify.Teams tm ON tm.teamID = p.teamID
        WHERE t.taskID = @taskID;

        IF NOT EXISTS (
            SELECT 1 FROM listify.TeamMembers 
            WHERE userID = @teamLeaderID AND teamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
			ROLLBACK;
            THROW 50020, 'Only team leaders can assign dependencies', 1;
			
        END;

        SELECT @dependentTaskProjectID = p.projectID
        FROM listify.Tasks t
        JOIN listify.Sections s ON s.sectionID = t.sectionID
        JOIN listify.Projects p ON p.projectID = s.projectID
        WHERE t.taskID = @dependentTaskID;

        IF @projectID <> @dependentTaskProjectID
        BEGIN
			ROLLBACK;
            THROW 50022, 'The dependent task does not belong to the same project', 1;
        END;

        IF @taskID = @dependentTaskID
        BEGIN
			ROLLBACK;
            THROW 50023, 'A task cannot depend on itself', 1;
        END;

        IF EXISTS (
            SELECT 1 FROM listify.TaskDependencies 
            WHERE taskID = @taskID AND dependentTaskID = @dependentTaskID
        )
        BEGIN
			ROLLBACK;
            THROW 50024, 'Dependency already exists', 1;
        END;

        SET @currentTaskID = @dependentTaskID;

        WHILE EXISTS (
            SELECT 1 FROM listify.TaskDependencies 
            WHERE taskID = @currentTaskID AND dependentTaskID = @taskID
        )
        BEGIN
			ROLLBACK;
            THROW 50025, 'Circular dependency detected', 1;
        END;

        INSERT INTO listify.TaskDependencies (taskID, dependentTaskID)
        VALUES (@taskID, @dependentTaskID);

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;
