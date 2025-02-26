CREATE VIEW vUserTeams AS
SELECT 
	u.userID,
    u.gitHubID,
	t.teamID,
	t.teamName,
	t.createdAt AS teamCreatedAt,
	t.updateAt AS teamUpdatedAt,
	tm.teamMemberID,
	tm.isTeamLeader
FROM 
    Users u
INNER JOIN
	TeamMembers tm ON u.userID = tm.userID
INNER JOIN
	Teams t ON tm.teamID = t.teamID
GO