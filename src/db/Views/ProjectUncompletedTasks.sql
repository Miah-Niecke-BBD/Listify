CREATE VIEW vProjectUncompletedTasks AS
SELECT
	p.projectID,
	p.projectName,
	t.taskID,
	t.sectionID,
	t.taskName,
	t.taskDescription,
	t.taskPosition,
	t.taskPriority,
	t.dateCompleted,
	t.dueDate,
	t.createdAt AS taskCreatedAt,
    t.updatedAt AS taskUpdatedAt
FROM
	Tasks t
INNER JOIN 
	Sections s ON t.sectionID = s.sectionID
INNER JOIN
	Projects p ON s.projectID = p.projectID
WHERE dateCompleted IS NULL