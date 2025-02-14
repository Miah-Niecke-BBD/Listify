GO
CREATE PROCEDURE uspCreateProject
    @teamLeaderID INT,
    @teamID INT,
    @projectName VARCHAR(100),
    @projectDescription VARCHAR(500) = NULL
AS
BEGIN
    DECLARE @projectID INT;

    BEGIN TRANSACTION
    BEGIN TRY

        IF NOT EXISTS (
            SELECT 1 FROM TeamMembers 
            WHERE userID = @teamLeaderID AND teamID = @teamID AND isTeamLeader = 1
        )
        BEGIN
            PRINT 'Only team leaders can create projects';
        END;

        INSERT INTO Projects (teamID, projectName, projectDescription, createdAt)
        VALUES (@teamID, @projectName, @projectDescription, SYSDATETIME());

        SET @projectID = SCOPE_IDENTITY();

        INSERT INTO ProjectAssignees (userID, projectID)
        VALUES (@userID, @projectID);

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