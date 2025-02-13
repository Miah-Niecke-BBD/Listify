GO
CREATE VIEW UserTeamProjectsTasks AS
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
	Users u
INNER JOIN
	ProjectAssignees pa ON u.userID = pa.userID
INNER JOIN
	Projects p ON pa.projectID = p.projectID
INNER JOIN 
	Teams tm ON p.teamID = tm.teamID
INNER JOIN 
	Sections s ON s.projectID = p.projectID
INNER JOIN 
	Tasks t ON t.sectionID = s.sectionID
GO