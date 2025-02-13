CREATE PROCEDURE CompleteTask
@taskID INT 
AS
BEGIN
    BEGIN TRANSACTION
    BEGIN TRY
        IF NOT EXISTS (SELECT 1 FROM Tasks WHERE taskID = @taskID)
        BEGIN
            THROW 50010, 'Task does not exist.', 1;
        END;

        UPDATE Tasks
        SET dateCompleted = GETDATE()
        WHERE taskID = @taskID;

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;
GO
