CREATE FUNCTION dbo.GetTasksByPriority(@projectID INT, @priority TINYINT)
RETURNS TABLE
AS
RETURN (
    SELECT taskID, taskName, taskPriority, dueDate, status
    FROM Tasks
    WHERE projectID = @projectID
    AND taskPriority = @priority
);
GO
