CREATE FUNCTION [listify].fnGetTeamLeaderForProject
(
	@projectID INT
)
RETURNS INT
AS
BEGIN
	  DECLARE @teamLeaderID INT;

	  SELECT @teamLeaderID = userID  
	  FROM [listify].Projects p
		INNER JOIN
			[listify].Teams t ON t.teamID = p.teamID
		INNER JOIN
			[listify].TeamMembers tm ON tm.teamID = t.teamID
		WHERE projectID = @projectID AND tm.isTeamLeader = 1

	RETURN @teamLeaderID
END