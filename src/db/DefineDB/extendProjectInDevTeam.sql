BEGIN TRANSACTION;

BEGIN TRY
    DECLARE @ProjectID INT = (SELECT [projectID] FROM [Projects] WHERE [projectName] = 'Kubernetes Infrastructure');

    DECLARE @ClusterSetupID INT = (SELECT [sectionID] FROM [Sections] WHERE [projectID] = @ProjectID AND [sectionName] = 'Cluster Setup');
    DECLARE @NetworkingID INT = (SELECT [sectionID] FROM [Sections] WHERE [projectID] = @ProjectID AND [sectionName] = 'Networking & Security');
    DECLARE @MonitoringID INT = (SELECT [sectionID] FROM [Sections] WHERE [projectID] = @ProjectID AND [sectionName] = 'Monitoring & Scaling');
	
    INSERT INTO [Tasks] ([sectionID], [parentTaskID], [taskName], [taskDescription], [taskPriority], [taskPosition], [dueDate], [createdAt], [updatedAt], [dateCompleted])
    VALUES
    (@NetworkingID, NULL, 'Configure Firewall Rules', 'Set up firewall rules for Kubernetes cluster security.', 2, 4, '2024-11-15', '2024-10-15', '2024-11-14', '2024-11-14'),
    (@MonitoringID, NULL, 'Setup Grafana Dashboards', 'Create dashboards for monitoring cluster health.', 2, 4, '2024-10-10', '2024-09-09', '2024-10-09', '2024-10-09'),
    (@ClusterSetupID, NULL, 'Optimize Cluster Resources', 'Tune cluster resource allocation and limits.', 3, 4, '2024-09-20', '2024-08-19', '2024-09-19', '2024-09-19');
    
    DECLARE @Task4 INT = (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Configure Firewall Rules');
    DECLARE @Task5 INT = (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Setup Grafana Dashboards');
    DECLARE @Task6 INT = (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Optimize Cluster Resources');
    
    INSERT INTO [Tasks] ([sectionID], [parentTaskID], [taskName], [taskDescription], [taskPriority], [taskPosition], [dueDate], [createdAt], [updatedAt], [dateCompleted])
    VALUES
    (@NetworkingID, @Task4, 'Test Firewall Rules', 'Verify firewall settings allow necessary traffic.', 2, 1, '2024-11-10', '2024-09-09', '2024-11-09', '2024-11-09'),
    (@MonitoringID, @Task5, 'Integrate with Prometheus', 'Ensure Grafana pulls metrics from Prometheus.', 2, 1, '2024-10-05', '2024-09-04', '2024-10-04', '2024-10-04'),
    (@ClusterSetupID, @Task6, 'Analyze Resource Usage', 'Monitor pod resource consumption before optimization.', 3, 1, '2024-09-15', '2024-08-14', '2024-09-14', '2024-09-14');
    
    INSERT INTO [TaskAssignees] ([userID], [taskID])
    VALUES
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user2_github'), @Task4),
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user2_github'), (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Test Firewall Rules')),
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user2_github'), @Task5),
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user2_github'), (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Integrate with Prometheus')),
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user2_github'), @Task6),
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user2_github'), (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Analyze Resource Usage'));
    
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    ROLLBACK TRANSACTION;
    PRINT 'Transaction rolled back due to an error';
    THROW;
END CATCH;
