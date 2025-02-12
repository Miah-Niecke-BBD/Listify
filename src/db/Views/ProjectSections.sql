CREATE VIEW ProjectSections AS
SELECT
	p.projectID,
	p.projectName,
	p.projectDescription,
	p.createdAt AS projectCreatedAt,
	p.updatedAt AS projectUpdatedAt,
	s.sectionID,
	s.sectionName,
	s.sectionPosition,
	s.createdAt AS sectionCreatedAt,
	s.updatedAt AS sectionUpdatedAt
FROM 
	Projects p
INNER JOIN
	Sections s ON s.projectID = p.projectID
GO