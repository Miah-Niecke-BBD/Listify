CREATE PROCEDURE UpdateTask
    @taskID INT,
    @taskName VARCHAR(100),
    @taskDescription VARCHAR(500),
    @taskPriority TINYINT,
    @dueDate DATETIME
AS
BEGIN
    UPDATE Tasks
    SET taskName = @taskName,
        taskDescription = @taskDescription,
        taskPriority = @taskPriority,
        dueDate = @dueDate,
        updatedAt = GETDATE()
    WHERE taskID = @taskID;
END;
