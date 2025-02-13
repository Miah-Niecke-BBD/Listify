GO
CREATE FUNCTION dbo.GetTotalProjectsByTeam(@teamID INT)
RETURNS INT
AS
BEGIN
    DECLARE @count INT;
    SELECT @count = COUNT(*) FROM Projects WHERE teamID = @teamID;
    RETURN @count;
END;
GO