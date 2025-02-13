REATE FUNCTION GetTaskDaysLeft(@taskID INT)
RETURNS INT
AS
BEGIN
    DECLARE @dueDate DATETIME, @daysLeft INT;

    SELECT @dueDate = dueDate
    FROM Tasks
    WHERE taskID = @taskID;

    SET @daysLeft = DATEDIFF(DAY, GETDATE(), @dueDate);

    RETURN @daysLeft;
END;
GO
