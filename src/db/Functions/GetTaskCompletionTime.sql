CREATE FUNCTION dbo.GetTaskCompletionTime(@taskID INT)
RETURNS INT
AS
BEGIN
    DECLARE @completionTime INT, @dueDate DATETIME, @completedDate DATETIME;

    SELECT @dueDate = dueDate, @completedDate = dateCompleted
    FROM Tasks
    WHERE taskID = @taskID;

    IF @completedDate IS NULL
        RETURN NULL;

    SET @completionTime = DATEDIFF(DAY, @dueDate, @completedDate);

    RETURN @completionTime;
END;
GO
