CREATE FUNCTION fnGetTeamLeaderForTask
(
	@taskID INT
)
RETURNS INT
AS
BEGIN
	  DECLARE @teamLeaderID INT;

	  SELECT @teamLeaderID = userID  
	  FROM Tasks ta
		INNER JOIN 
			Sections s ON s.sectionID = ta.sectionID
		INNER JOIN
			Projects p ON p.projectID = s.projectID
		INNER JOIN
			Teams t ON t.teamID = p.teamID
		INNER JOIN
			TeamMembers tm ON tm.teamID = t.teamID
		WHERE ta.taskID = @taskID AND tm.isTeamLeader = 1

	RETURN @teamLeaderID
END