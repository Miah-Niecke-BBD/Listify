CREATE FUNCTION GetActiveTaskCountForProject(@projectID INT)
RETURNS INT
AS
BEGIN
    DECLARE @activeTasks INT;

    SELECT @activeTasks = COUNT(*)
    FROM Tasks
    WHERE projectID = @projectID AND dateCompleted IS NULL;

    RETURN @activeTasks;
END;
GO
