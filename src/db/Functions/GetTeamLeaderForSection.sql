CREATE FUNCTION fnGetTeamLeaderForSection
(
	@sectionID INT
)
RETURNS INT
AS
BEGIN
	  DECLARE @teamLeaderID INT;

	  SELECT @teamLeaderID = userID  
	  FROM Sections s
		INNER JOIN
			Projects p ON p.projectID = s.projectID
		INNER JOIN
			Teams t ON t.teamID = p.teamID
		INNER JOIN
			TeamMembers tm ON tm.teamID = t.teamID
		WHERE sectionID = @sectionID AND tm.isTeamLeader = 1

	RETURN @teamLeaderID
END
