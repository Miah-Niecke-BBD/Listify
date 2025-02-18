CREATE FUNCTION [listify].fnUserExists(@gitHubID VARCHAR(2083))
RETURNS BIT
AS
BEGIN
    DECLARE @exists BIT = 0;
    IF EXISTS (SELECT 1 FROM [listify].Users WHERE gitHubID = @gitHubID)
        SET @exists = 1;
    RETURN @exists;
END;
