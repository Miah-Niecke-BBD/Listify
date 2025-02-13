CREATE FUNCTION dbo.UserExists(@gitHubID VARCHAR(2083))
RETURNS BIT
AS
BEGIN
    DECLARE @exists BIT = 0;
    IF @gitHubID IS NULL RETURN 0;

    IF EXISTS (SELECT 1 FROM Users WHERE gitHubID = @gitHubID)
        SET @exists = 1;

    RETURN @exists;
END;
GO
