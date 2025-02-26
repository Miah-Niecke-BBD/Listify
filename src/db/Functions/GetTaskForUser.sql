CREATE FUNCTION dbo.GetTasksForUser(@userID INT)
RETURNS TABLE
AS
RETURN (
    SELECT t.taskID, t.taskName, t.dueDate, t.taskPriority
    FROM Tasks t
    JOIN TaskAssignees ta ON t.taskID = ta.taskID
    WHERE ta.userID = @userID
);
GO
