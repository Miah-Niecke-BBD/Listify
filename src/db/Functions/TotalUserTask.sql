GO
CREATE FUNCTION dbo.GetUserTasks(@userID INT)
RETURNS TABLE
AS
RETURN (
    SELECT t.taskID, t.taskName, t.taskDescription, t.dueDate
    FROM Tasks t
    JOIN TaskAssignees ta ON t.taskID = ta.taskID
    WHERE ta.userID = @userID
);
GO