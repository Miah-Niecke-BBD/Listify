BEGIN TRANSACTION;

BEGIN TRY
    INSERT INTO [listify].[Projects] ([teamID], [projectName], [projectDescription], [createdAt], [updatedAt])
    VALUES ((SELECT [teamID] FROM [listify].[Teams] WHERE [teamName] = 'Dev Team'), 'Kubernetes Infrastructure', 'Setup and manage Kubernetes infrastructure for deployments.', '2023-05-15', '2024-05-15');

    DECLARE @ProjectID INT = (SELECT [projectID] FROM [listify].[Projects] WHERE [projectName] = 'Kubernetes Infrastructure');

    INSERT INTO [listify].[Sections] ([projectID], [sectionName], [sectionPosition], [createdAt], [updatedAt])
    VALUES
    (@ProjectID, 'Cluster Setup', 0, '2024-05-25', '2024-06-25'),
    (@ProjectID, 'Networking & Security', 1, '2024-05-25', '2024-05-25'),
    (@ProjectID, 'Monitoring & Scaling', 2, '2024-05-25', '2024-05-25');

    DECLARE @ClusterSetupID INT = (SELECT [sectionID] FROM [listify].[Sections] WHERE [projectID] = @ProjectID AND [sectionName] = 'Cluster Setup');
    DECLARE @NetworkingID INT = (SELECT [sectionID] FROM [listify].[Sections] WHERE [projectID] = @ProjectID AND [sectionName] = 'Networking & Security');
    DECLARE @MonitoringID INT = (SELECT [sectionID] FROM [listify].[Sections] WHERE [projectID] = @ProjectID AND [sectionName] = 'Monitoring & Scaling');

    INSERT INTO [listify].[Tasks] ([sectionID], [parentTaskID], [taskName], [taskDescription], [taskPriority], [taskPosition], [dueDate], [createdAt], [updatedAt])
    VALUES
    (@ClusterSetupID, NULL, 'Setup Kubernetes Cluster', 'Initialize and configure a new Kubernetes cluster.', 2, 0, '2024-07-01', '2024-06-01', '2024-06-01'),
    (@ClusterSetupID, NULL, 'Configure Nodes', 'Add worker nodes to the cluster and verify connectivity.', 2, 1, '2024-07-10', '2024-06-06', '2024-06-06'),
    (@ClusterSetupID, NULL, 'Install Helm', 'Deploy Helm to manage Kubernetes packages.', 1, 2, '2024-07-15', '2024-06-24', '2024-06-24'),
    
    (@NetworkingID, NULL, 'Configure Network Policies', 'Define network policies to control pod communication.', 2, 0, '2024-06-20', '2024-04-01', '2024-04-01'),
    (@NetworkingID, NULL, 'Setup Ingress Controller', 'Deploy an ingress controller for handling HTTP traffic.', 2, 1, '2024-06-25', '2024-06-15', '2024-06-15'),
    (@NetworkingID, NULL, 'Enable Service Mesh', 'Implement Istio or Linkerd for traffic management.', 3, 2, '2024-07-05', '2024-06-22', '2024-06-22'),
    
    (@MonitoringID, NULL, 'Setup Prometheus Monitoring', 'Deploy Prometheus for cluster observability.', 2, 0, '2024-06-30', '2024-04-01', '2024-04-01'),
    (@MonitoringID, NULL, 'Configure Logging with ELK', 'Implement ELK Stack for centralized logging.', 3, 1, '2024-07-08', '2024-06-16', '2024-06-16'),
    (@MonitoringID, NULL, 'Auto-Scaling Configuration', 'Set up horizontal pod autoscalers.', 3, 2, '2024-07-12', '2024-06-20', '2024-06-20');

    DECLARE @Task1 INT = (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Setup Kubernetes Cluster');
    DECLARE @Task2 INT = (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Configure Nodes');
    DECLARE @Task3 INT = (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Install Helm');

    INSERT INTO [listify].[Tasks] ([sectionID], [parentTaskID], [taskName], [taskDescription], [taskPriority], [taskPosition], [dueDate], [createdAt], [updatedAt], [dateCompleted])
    VALUES
    (@ClusterSetupID, @Task1, 'Initialize Kubernetes Master', 'Run kubeadm init and configure master node.', 2, 0, '2024-06-25', '2024-06-02', '2024-06-24', '2024-06-24'),
    (@ClusterSetupID, @Task2, 'Join Worker Nodes', 'Execute join command on worker nodes.', 2, 1, '2024-07-05', '2024-06-10', '2024-06-10', NULL),
    (@ClusterSetupID, @Task3, 'Verify Helm Installation', 'Ensure Helm is correctly installed and working.', 1, 2, '2024-07-10', '2024-05-24', '2024-05-24', NULL);

    INSERT INTO [listify].[ProjectAssignees] ([userID], [projectID])
    VALUES ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user1_github'), @ProjectID),
           ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user2_github'), @ProjectID),
           ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user3_github'), @ProjectID);

    INSERT INTO [listify].[TaskAssignees] ([userID], [taskID])
    VALUES
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user1_github'), @Task1),
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user1_github'), (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Initialize Kubernetes Master'))),
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user1_github'), @Task2),
    
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user2_github'), @Task3),
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user2_github'), (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Verify Helm Installation'))),
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user2_github'), (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Setup Prometheus Monitoring'))),
    
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user3_github'), (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Setup Ingress Controller'))),
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user3_github'), (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Configure Logging with ELK'))),
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user3_github'), (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Auto-Scaling Configuration')));

    INSERT INTO [listify].[Tasks] ([sectionID], [parentTaskID], [taskName], [taskDescription], [taskPriority], [taskPosition], [dueDate], [createdAt], [updatedAt], [dateCompleted])
    VALUES
    (@NetworkingID, NULL, 'Configure Firewall Rules', 'Set up firewall rules for Kubernetes cluster security.', 2, 3, '2024-11-15', '2024-10-15', '2024-11-14', '2024-11-14'),
    (@MonitoringID, NULL, 'Setup Grafana Dashboards', 'Create dashboards for monitoring cluster health.', 2, 3, '2024-10-10', '2024-09-09', '2024-10-09', '2024-10-09'),
    (@ClusterSetupID, NULL, 'Optimize Cluster Resources', 'Tune cluster resource allocation and limits.', 3, 3, '2024-09-20', '2024-08-19', '2024-09-19', '2024-09-19');
    
    DECLARE @Task4 INT = (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Configure Firewall Rules');
    DECLARE @Task5 INT = (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Setup Grafana Dashboards');
    DECLARE @Task6 INT = (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Optimize Cluster Resources');
    
    INSERT INTO [listify].[Tasks] ([sectionID], [parentTaskID], [taskName], [taskDescription], [taskPriority], [taskPosition], [dueDate], [createdAt], [updatedAt], [dateCompleted])
    VALUES
    (@NetworkingID, @Task4, 'Test Firewall Rules', 'Verify firewall settings allow necessary traffic.', 2, 0, '2024-11-10', '2024-09-09', '2024-11-09', '2024-11-09'),
    (@MonitoringID, @Task5, 'Integrate with Prometheus', 'Ensure Grafana pulls metrics from Prometheus.', 2, 0, '2024-10-05', '2024-09-04', '2024-10-04', '2024-10-04'),
    (@ClusterSetupID, @Task6, 'Analyze Resource Usage', 'Monitor pod resource consumption before optimization.', 3, 0, '2024-09-15', '2024-08-14', '2024-09-14', '2024-09-14');
    
    INSERT INTO [listify].[TaskAssignees] ([userID], [taskID])
    VALUES
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user2_github'), @Task4),
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user2_github'), (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Test Firewall Rules'))),
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user2_github'), @Task5),
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user2_github'), (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Integrate with Prometheus'))),
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user2_github'), @Task6),
    ((SELECT [userID] FROM [listify].[Users] WHERE [gitHubID] = 'user2_github'), (SELECT [taskID] FROM [listify].[Tasks] WHERE [taskName] = 'Analyze Resource Usage')));

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    ROLLBACK TRANSACTION;
    PRINT 'Transaction rolled back due to an error';
    THROW;
END CATCH;
