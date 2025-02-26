CREATE VIEW vUserAssignedTasks AS
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
    Users u
INNER JOIN 
    TaskAssignees ta ON u.userID = ta.userID
INNER JOIN 
    Tasks t ON ta.taskID = t.taskID
INNER JOIN 
    Sections s ON t.sectionID = s.sectionID
INNER JOIN 
    Projects p ON s.projectID = p.projectID
GO