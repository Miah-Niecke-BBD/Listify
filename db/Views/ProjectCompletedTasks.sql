CREATE VIEW listify.vProjectCompletedTasks AS
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
	listify.Tasks t
INNER JOIN 
	listify.Sections s ON t.sectionID = s.sectionID
INNER JOIN
	listify.Projects p ON s.projectID = p.projectID
WHERE dateCompleted IS NOT NULL