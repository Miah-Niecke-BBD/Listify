GO
CREATE VIEW listify.vUserTeamProjectsTasks AS
SELECT
	u.userID,
	u.githubID,
	tm.teamID,
	tm.teamName,
	pa.projectAssigneeID,
	p.projectID,
	p.projectName,
	p.projectDescription,
	s.sectionID,
	s.sectionName,
	t.taskID,
	t.taskName,
	t.taskDescription,
	t.taskPriority,
	t.dueDate
FROM
	listify.Users u
INNER JOIN
	listify.ProjectAssignees pa ON u.userID = pa.userID
INNER JOIN
	listify.Projects p ON pa.projectID = p.projectID
INNER JOIN 
	listify.Teams tm ON p.teamID = tm.teamID
INNER JOIN 
	listify.Sections s ON s.projectID = p.projectID
INNER JOIN 
	listify.Tasks t ON t.sectionID = s.sectionID
GO