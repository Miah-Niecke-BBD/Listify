
CREATE PROCEDURE listify.uspCreateUser
    @gitHubID VARCHAR(2083)
AS
BEGIN
    DECLARE @userID INT;

    BEGIN TRANSACTION
    BEGIN TRY

        INSERT INTO listify.Users (gitHubID, createdAt)
        VALUES (@gitHubID, SYSDATETIME());

        SET @userID = SCOPE_IDENTITY();

        EXEC listify.uspCreateMyProjectsTeam @userID, @gitHubID;

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;

        DECLARE @ErrorMessage NVARCHAR(MAX) = ERROR_MESSAGE();
        PRINT 'Error occurred: ' + @ErrorMessage;

        THROW;
    END CATCH
END;
