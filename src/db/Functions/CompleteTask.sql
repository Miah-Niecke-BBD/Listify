create Procedure CompleteTask
@taskID INT 
AS
Begin
update Tasks
set dateCompletes = GETDATE()
Where taskID = @taskID;
END;
