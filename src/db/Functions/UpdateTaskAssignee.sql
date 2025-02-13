CREATE PROCEDURE UpdateTaskAssignee
    @taskID INT,
    @userID INT
AS
BEGIN
    BEGIN TRANSACTION

    BEGIN TRY
        DELETE FROM TaskAssignees WHERE taskID = @taskID;

        INSERT INTO TaskAssignees (taskID, userID)
        VALUES (@taskID, @userID);

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;
GO
