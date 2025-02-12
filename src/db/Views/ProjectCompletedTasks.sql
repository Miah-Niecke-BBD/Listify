CREATE VIEW ProjectCompletedTasks AS
SELECT
	t.taskID,
	t.taskName,
	t.taskDescription,
	t.taskPosition,
	t.taskPriority,
	t.dateCompleted,
	t.dueDate,
	t.createdAt AS taskCreatedAt,
    t.updatedAt AS taskUpdatedAt,
	p.projectID,
	p.projectName
FROM
	Tasks t
INNER JOIN 
	Sections s ON t.sectionID = s.sectionID
INNER JOIN
	Projects p ON s.projectID = p.projectID
WHERE dateCompleted IS NOT NULL