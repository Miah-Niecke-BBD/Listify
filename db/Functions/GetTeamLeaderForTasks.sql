CREATE FUNCTION [listify].fnGetTeamLeaderForTask
(
	@taskID INT
)
RETURNS INT
AS
BEGIN
	  DECLARE @teamLeaderID INT;

	  SELECT @teamLeaderID = userID  
	  FROM [listify].Tasks ta
		INNER JOIN 
			[listify].Sections s ON s.sectionID = ta.sectionID
		INNER JOIN
			[listify].Projects p ON p.projectID = s.projectID
		INNER JOIN
			[listify].Teams t ON t.teamID = p.teamID
		INNER JOIN
			[listify].TeamMembers tm ON tm.teamID = t.teamID
		WHERE ta.taskID = @taskID AND tm.isTeamLeader = 1

	RETURN @teamLeaderID
END