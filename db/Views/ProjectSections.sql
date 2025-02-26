CREATE VIEW listify.vProjectSections AS
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
	listify.Projects p
INNER JOIN
	listify.Sections s ON s.projectID = p.projectID
