CREATE VIEW SectionTasks AS
SELECT
	s.sectionID,
	s.sectionName,
	s.createdAt AS sectionCreatedAt,
	s.updatedAt AS sectionUpdatedAt,
	t.taskID,
	t.parentTaskID,
	t.taskName,
	t.taskDescription,
	t.taskPriority,
	t.taskPosition,
	t.dateCompleted,
	t.dueDate,
	t.createdAt AS taskCreatedAt,
    t.updatedAt AS taskUpdatedAt
FROM
	Sections s
INNER JOIN
	Tasks t ON t.sectionID = s.sectionID