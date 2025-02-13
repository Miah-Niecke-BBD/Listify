CREATE FUNCTION dbo.GetOverdueTasks(@projectID INT)
RETURNS TABLE
AS
RETURN (
    SELECT taskID, taskName, dueDate, status
    FROM Tasks
    WHERE projectID = @projectID
    AND dueDate < GETDATE()
    AND isCompleted = 0
);
GO
