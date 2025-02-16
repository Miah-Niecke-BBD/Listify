CREATE TABLE DependencyPriority (
    priorityID INT PRIMARY KEY,
    priorityLabel VARCHAR(50) NOT NULL
);

INSERT INTO DependencyPriority (priorityID, priorityLabel)
VALUES (1, 'Low'), (2, 'Medium'), (3, 'High');


ALTER TABLE Tasks ADD CONSTRAINT chk_taskPriority CHECK (taskPriority IN (1, 2, 3) OR taskPriority IS NULL)

SELECT 
    td.taskID, 
    td.dependentTaskID, 
    dp.priorityLabel 
FROM TaskDependencies td
LEFT JOIN DependencyPriority dp ON td.priorityID = dp.priorityID;