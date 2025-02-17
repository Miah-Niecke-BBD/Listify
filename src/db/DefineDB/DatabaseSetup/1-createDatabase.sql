CREATE DATABASE ListifyDB
GO
USE ListifyDB


---===============================  Tables  ===================================================---
GO
CREATE TABLE [listify].[Users] (
  [userID] INT PRIMARY KEY NOT NULL IDENTITY(1, 1),
  [gitHubID] VARCHAR(1700) UNIQUE NOT NULL,
  [createdAt] DATETIME NOT NULL,
  [updatedAt] DATETIME ,
)
GO

CREATE TABLE [listify].[Teams] (
  [teamID] INT PRIMARY KEY NOT NULL IDENTITY(1, 1),
  [teamName] VARCHAR(100) NOT NULL,
  [createdAt] DATETIME NOT NULL,
  [updatedAt] DATETIME ,
)
GO

CREATE TABLE [listify].[TeamMembers] (
  [teamMemberID] INT PRIMARY KEY NOT NULL IDENTITY(1, 1),
  [userID] INT NOT NULL,
  [teamID] INT NOT NULL,
  [isTeamLeader] BIT NOT NULL DEFAULT 0,
  FOREIGN KEY ([userID]) REFERENCES [listify].[Users] ([userID]) ON DELETE CASCADE,
  FOREIGN KEY ([teamID]) REFERENCES [listify].[Teams] ([teamID]) ON DELETE CASCADE
)
GO

CREATE TABLE [listify].[Projects] (
  [projectID] INT PRIMARY KEY NOT NULL IDENTITY(1, 1),
  [teamID] INT NOT NULL, 
  [projectName] VARCHAR(100) NOT NULL,
  [projectDescription] VARCHAR(500),
  [createdAt] DATETIME NOT NULL,
  [updatedAt] DATETIME,
  FOREIGN KEY ([teamID]) REFERENCES [listify].[Teams] ([teamID]) ON DELETE CASCADE
)
GO

CREATE TABLE [listify].[Sections] (
  [sectionID] INT PRIMARY KEY NOT NULL IDENTITY(1, 1),
  [projectID] INT NOT NULL,
  [sectionName] VARCHAR(100) NOT NULL,
  [sectionPosition] TINYINT NOT NULL,
  [createdAt] DATETIME NOT NULL,
  [updatedAt] DATETIME,
  FOREIGN KEY ([projectID]) REFERENCES [listify].[Projects] ([projectID]) ON DELETE CASCADE
)
GO

CREATE TABLE [listify].PriorityLabels (
  [priorityLabelID] TINYINT PRIMARY KEY NOT NULL,       
  [priorityLabelName] VARCHAR(15) NOT NULL,            
  CONSTRAINT CHK_PriorityLabelID CHECK ([priorityLabelID] BETWEEN 1 AND 3), 
);
GO

CREATE TABLE [listify].[Tasks] (
  [taskID] INT PRIMARY KEY IDENTITY(1, 1),
  [sectionID] INT NOT NULL,
  [parentTaskID] INT,
  [taskName] VARCHAR(100) NOT NULL,
  [taskDescription] VARCHAR(500),
  [taskPriority] TINYINT,
  [taskPosition] TINYINT NOT NULL,
  [dateCompleted] DATETIME,
  [dueDate] DATETIME,
  [createdAt] DATETIME NOT NULL,
  [updatedAt] DATETIME,
  FOREIGN KEY ([sectionID]) REFERENCES [listify].[Sections] ([sectionID]) ON DELETE CASCADE, 
  FOREIGN KEY ([parentTaskID]) REFERENCES [listify].[Tasks] (taskID),
  FOREIGN KEY ([taskPriority]) REFERENCES [listify].[PriorityLabels] (priorityLabelID)
)
GO

CREATE TABLE [listify].[TaskAssignees] (
  [taskAssigneeID] INT PRIMARY KEY NOT NULL IDENTITY(1, 1),
  [userID] INT NOT NULL,
  [taskID] INT NOT NULL,
  FOREIGN KEY ([userID]) REFERENCES [listify].[Users] ([userID]),
  FOREIGN KEY ([taskID]) REFERENCES [listify].[Tasks] ([taskID]) ON DELETE CASCADE
)
GO

CREATE TABLE [listify].[TaskDependencies] (
  [taskDependencyID] INT PRIMARY KEY NOT NULL IDENTITY(1, 1),
  [taskID] INT NOT NULL UNIQUE,
  [dependentTaskID] INT NOT NULL UNIQUE,
  FOREIGN KEY ([taskID]) REFERENCES [listify].[Tasks] ([taskID]) ON DELETE CASCADE,
  FOREIGN KEY ([dependentTaskID]) REFERENCES [listify].[Tasks] ([taskID]) 
)
GO

CREATE TABLE [listify].[ProjectAssignees] (
  [projectAssigneeID] INT PRIMARY KEY NOT NULL IDENTITY(1, 1),
  [userID] INT NOT NULL,
  [projectID] INT NOT NULL,
  FOREIGN KEY ([projectID]) REFERENCES [listify].[Projects] ([projectID]) ON DELETE CASCADE,
  FOREIGN KEY ([userID]) REFERENCES [listify].[Users] ([userID])
)
GO

INSERT INTO PriorityLabels (priorityLabelID, priorityLabelName)
VALUES (1, 'Low'), (2, 'Medium'), (3, 'High');

---===============================  ????  ===================================================---