USE ListifyDB
GO

CREATE VIEW TeamProjects AS
SELECT
	t.teamID,
	t.teamName,
	p.projectID,
	p.projectDescription,
	p.createdAt AS projectCreatedAt,
	p.updatedAt AS projectUpdatedAt
FROM 
	Projects p
INNER JOIN
	Teams t ON p.teamID = t.teamID