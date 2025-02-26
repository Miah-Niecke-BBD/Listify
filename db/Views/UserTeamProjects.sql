CREATE VIEW listify.vUserTeamProjects AS
SELECT
	u.userID,
	u.githubID,
	pa.projectAssigneeID,
	p.projectID,
	p.projectName,
	p.projectDescription,
	p.createdAt AS projectCreatedAt,
	p.updatedAt AS projectUpdatedAt,
	t.teamID,
	t.teamName,
	t.createdAt AS teamCreatedAt,
	t.updatedAt AS teamUpdatedAt
FROM
	listify.Users u
INNER JOIN
	listify.ProjectAssignees pa ON u.userID = pa.userID
INNER JOIN
	listify.Projects p ON pa.projectID = p.projectID
INNER JOIN 
	listify.Teams t ON p.teamID = t.teamID
GO