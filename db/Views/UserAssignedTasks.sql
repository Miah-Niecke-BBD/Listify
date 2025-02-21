CREATE VIEW listify.vUserAssignedTasks AS
SELECT 
    u.userID,
    u.gitHubID,
    t.taskID,
    t.taskName,
    t.taskDescription,
    t.taskPriority,
    t.dueDate,
    t.dateCompleted,
    t.createdAt AS taskCreatedAt,
    t.updatedAt AS taskUpdatedAt,
    s.sectionID,
    s.sectionName,
    p.projectID,
    p.projectName
FROM 
    listify.Users u
INNER JOIN 
    listify.TaskAssignees ta ON u.userID = ta.userID
INNER JOIN 
    listify.Tasks t ON ta.taskID = t.taskID
INNER JOIN 
    listify.Sections s ON t.sectionID = s.sectionID
INNER JOIN 
    listify.Projects p ON s.projectID = p.projectID
