CREATE VIEW listify.vTeamProjects AS
SELECT
	t.teamID,
	t.teamName,
	p.projectID,
	p.projectDescription,
	p.createdAt AS projectCreatedAt,
	p.updatedAt AS projectUpdatedAt
FROM 
	listify.Projects p
INNER JOIN
	listify.Teams t ON p.teamID = t.teamID