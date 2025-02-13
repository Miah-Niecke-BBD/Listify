USE ListifyDB

-- Inserting sample Users
INSERT INTO [Users] ([gitHubID], [createdAt], [updatedAt])
VALUES 
  ('user1_github', GETDATE(), GETDATE()),
  ('user2_github', GETDATE(), GETDATE()),
  ('user3_github', GETDATE(), GETDATE()),
  ('user4_github', GETDATE(), GETDATE()),
  ('user5_github', GETDATE(), GETDATE()),
  ('user6_github', GETDATE(), GETDATE()),
  ('user7_github', GETDATE(), GETDATE()),
  ('user8_github', GETDATE(), GETDATE()),
  ('user9_github', GETDATE(), GETDATE()),
  ('user10_github', GETDATE(), GETDATE()),
  ('user11_github', GETDATE(), GETDATE()),
  ('user12_github', GETDATE(), GETDATE()),
  ('user13_github', GETDATE(), GETDATE()),
  ('user14_github', GETDATE(), GETDATE()),
  ('user15_github', GETDATE(), GETDATE());

-- Inserting "My Projects" teams for each user
INSERT INTO [Teams] ([teamName], [createdAt], [updatedAt])
VALUES 
  ('My Projects', GETDATE(), GETDATE()), -- for user 1
  ('My Projects', GETDATE(), GETDATE()), -- for user 2
  ('My Projects', GETDATE(), GETDATE()), -- for user 3
  ('My Projects', GETDATE(), GETDATE()), -- for user 4
  ('My Projects', GETDATE(), GETDATE()), -- for user 5
  ('My Projects', GETDATE(), GETDATE()), -- for user 6
  ('My Projects', GETDATE(), GETDATE()), -- for user 7
  ('My Projects', GETDATE(), GETDATE()), -- for user 8
  ('My Projects', GETDATE(), GETDATE()), -- for user 9
  ('My Projects', GETDATE(), GETDATE()), -- for user 10
  ('My Projects', GETDATE(), GETDATE()), -- for user 11
  ('My Projects', GETDATE(), GETDATE()), -- for user 12
  ('My Projects', GETDATE(), GETDATE()), -- for user 13
  ('My Projects', GETDATE(), GETDATE()), -- for user 14
  ('My Projects', GETDATE(), GETDATE()); -- for user 15

-- Inserting "My List" and "Home" projects for "My Projects" team
-- For simplicity, we're assuming user IDs are in sequential order starting from 1.
INSERT INTO [Projects] ([teamID], [projectName], [projectDescription], [createdAt], [updatedAt])
VALUES
  (1, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (1, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (2, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (2, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (3, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (3, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (4, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (4, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (5, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (5, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (6, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (6, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (7, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (7, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (8, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (8, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (9, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (9, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (10, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (10, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (11, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (11, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (12, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (12, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (13, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (13, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (14, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (14, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE()),

  (15, 'My List', 'Personal list of tasks and items.', GETDATE(), GETDATE()),
  (15, 'Home', 'Tasks related to home management.', GETDATE(), GETDATE());

-- Inserting Sections for "My List" and "Home" Projects (for simplification, we’ll add only one section for each)
INSERT INTO [Sections] ([projectID], [sectionName], [sectionPosition], [createdAt], [updatedAt])
VALUES 
  (1, 'To Do', 1, GETDATE(), GETDATE()),
  (2, 'To Do', 1, GETDATE(), GETDATE()),
  (3, 'To Do', 1, GETDATE(), GETDATE()),
  (4, 'To Do', 1, GETDATE(), GETDATE()),
  (5, 'To Do', 1, GETDATE(), GETDATE()),
  (6, 'To Do', 1, GETDATE(), GETDATE()),
  (7, 'To Do', 1, GETDATE(), GETDATE()),
  (8, 'To Do', 1, GETDATE(), GETDATE()),
  (9, 'To Do', 1, GETDATE(), GETDATE()),
  (10, 'To Do', 1, GETDATE(), GETDATE()),
  (11, 'To Do', 1, GETDATE(), GETDATE()),
  (12, 'To Do', 1, GETDATE(), GETDATE()),
  (13, 'To Do', 1, GETDATE(), GETDATE()),
  (14, 'To Do', 1, GETDATE(), GETDATE()),
  (15, 'To Do', 1, GETDATE(), GETDATE());

-- Inserting Tasks for the "To Do" section in the "My List" project (Example task for each user)
INSERT INTO [Tasks] ([sectionID], [taskName], [taskDescription], [taskPriority], [taskPosition], [createdAt], [updatedAt])
VALUES
  (1, 'Buy groceries', 'Buy milk, eggs, and bread', 0, 1, GETDATE(), GETDATE()),
  (2, 'Clean the house', 'Vacuum and mop the floors', 0, 1, GETDATE(), GETDATE()),
  (3, 'Fix the car', 'Take the car to the mechanic', 0, 1, GETDATE(), GETDATE()),
  (4, 'Work on website', 'Complete homepage layout', 0, 1, GETDATE(), GETDATE()),
  (5, 'Plan vacation', 'Book tickets for the trip', 0, 1, GETDATE(), GETDATE()),
  (6, 'Organize closet', 'Sort through clothes', 0, 1, GETDATE(), GETDATE()),
  (7, 'Prepare meeting notes', 'Prepare agenda for the meeting', 0, 1, GETDATE(), GETDATE()),
  (8, 'Update resume', 'Make edits to resume and LinkedIn profile', 0, 1, GETDATE(), GETDATE()),
  (9, 'Read book', 'Finish reading the book', 0, 1, GETDATE(), GETDATE()),
  (10, 'Complete project', 'Work on the final presentation', 0, 1, GETDATE(), GETDATE()),
  (11, 'Write blog post', 'Publish a new article on the blog', 0, 1, GETDATE(), GETDATE()),
  (12, 'Buy new laptop', 'Check reviews and prices', 0, 1, GETDATE(), GETDATE()),
  (13, 'Study for exam', 'Prepare notes for the exam', 0, 1, GETDATE(), GETDATE()),
  (14, 'Declutter workspace', 'Clear the desk and organize files', 0, 1, GETDATE(), GETDATE()),
  (15, 'Bake cookies', 'Make chocolate chip cookies', 0, 1, GETDATE(), GETDATE());

  -- Inserting TeamMembers (linking users to their "My Projects" team)
-- Assuming each user has only one team, which is "My Projects"
INSERT INTO [TeamMembers] ([userID], [teamID], [isTeamLeader])
VALUES
  (1, 1, 1), -- user 1
  (2, 2, 1), -- user 2
  (3, 3, 1), -- user 3
  (4, 4, 1), -- user 4
  (5, 5, 1), -- user 5
  (6, 6, 1), -- user 6
  (7, 7, 1), -- user 7
  (8, 8, 1), -- user 8
  (9, 9, 1), -- user 9
  (10, 10, 1), -- user 10
  (11, 11, 1), -- user 11
  (12, 12, 1), -- user 12
  (13, 13, 1), -- user 13
  (14, 14, 1), -- user 14
  (15, 15, 1); -- user 15

-- Inserting TaskAssignees (No assignees for tasks in "My Projects" as per guidelines)
-- This step is skipped as tasks for "My Projects" should not have assignees. If we needed this, 
-- we would assign users to tasks, but no assignees are added here because it's explicitly mentioned.

-- Inserting TaskDependencies (No dependencies in "My Projects" tasks)
-- Task dependencies are not required for "My Projects" tasks. So, this is also skipped.

-- Inserting ProjectAssignees (Linking users to their projects)
-- Each user is assigned to their own "My List" and "Home" projects
INSERT INTO [ProjectAssignees] ([userID], [projectID])
VALUES
  (1, 1), -- user 1 to "My List"
  (1, 2), -- user 1 to "Home"
  (2, 3), -- user 2 to "My List"
  (2, 4), -- user 2 to "Home"
  (3, 5), -- user 3 to "My List"
  (3, 6), -- user 3 to "Home"
  (4, 7), -- user 4 to "My List"
  (4, 8), -- user 4 to "Home"
  (5, 9), -- user 5 to "My List"
  (5, 10), -- user 5 to "Home"
  (6, 11), -- user 6 to "My List"
  (6, 12), -- user 6 to "Home"
  (7, 13), -- user 7 to "My List"
  (7, 14), -- user 7 to "Home"
  (8, 15), -- user 8 to "My List"
  (8, 16), -- user 8 to "Home"
  (9, 17), -- user 9 to "My List"
  (9, 18), -- user 9 to "Home"
  (10, 19), -- user 10 to "My List"
  (10, 20), -- user 10 to "Home"
  (11, 21), -- user 11 to "My List"
  (11, 22), -- user 11 to "Home"
  (12, 23), -- user 12 to "My List"
  (12, 24), -- user 12 to "Home"
  (13, 25), -- user 13 to "My List"
  (13, 26), -- user 13 to "Home"
  (14, 27), -- user 14 to "My List"
  (14, 28), -- user 14 to "Home"
  (15, 29), -- user 15 to "My List"
  (15, 30); -- user 15 to "Home"
