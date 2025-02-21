CREATE FUNCTION [listify].fnGetTeamLeaderForSection
(
	@sectionID INT
)
RETURNS INT
AS
BEGIN
	  DECLARE @teamLeaderID INT;

	  SELECT @teamLeaderID = userID  
	  FROM [listify].Sections s
		INNER JOIN
			[listify].Projects p ON p.projectID = s.projectID
		INNER JOIN
			[listify].Teams t ON t.teamID = p.teamID
		INNER JOIN
			[listify].TeamMembers tm ON tm.teamID = t.teamID
		WHERE sectionID = @sectionID AND tm.isTeamLeader = 1

	RETURN @teamLeaderID
END
