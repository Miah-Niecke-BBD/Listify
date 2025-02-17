CREATE VIEW listify.vUserTeams AS
SELECT 
	u.userID,
    u.gitHubID,
	t.teamID,
	t.teamName,
	t.createdAt AS teamCreatedAt,
	t.updatedAt AS teamUpdatedAt,
	tm.teamMemberID,
	tm.isTeamLeader
FROM 
    listify.Users u
INNER JOIN
	listify.TeamMembers tm ON u.userID = tm.userID
INNER JOIN
	listify.Teams t ON tm.teamID = t.teamID
GO