CREATE FUNCTION [listify]fn.GetTeamLeaderForProject
(
	@projectID INT
)
RETURNS INT
AS
BEGIN
	  DECLARE @teamLeaderID INT;

	  SELECT @teamLeaderID = userID  
	  FROM Projects p
		INNER JOIN
			Teams t ON t.teamID = p.teamID
		INNER JOIN
			TeamMembers tm ON tm.teamID = t.teamID
		WHERE projectID = @projectID AND tm.isTeamLeader = 1

	RETURN @teamLeaderID
END