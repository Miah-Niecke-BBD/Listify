BEGIN TRANSACTION;

BEGIN TRY
    INSERT INTO [Projects] ([teamID], [projectName], [projectDescription], [createdAt], [updatedAt])
    VALUES ((SELECT [teamID] FROM [Teams] WHERE [teamName] = 'Dev Team'), 'Kubernetes Infrastructure', 'Setup and manage Kubernetes infrastructure for deployments.', '2023-05-15', '2024-05-15');

    DECLARE @ProjectID INT = (SELECT [projectID] FROM [Projects] WHERE [projectName] = 'Kubernetes Infrastructure');

    INSERT INTO [Sections] ([projectID], [sectionName], [sectionPosition], [createdAt], [updatedAt])
    VALUES
    (@ProjectID, 'Cluster Setup', 1, '2024-05-25', '2024-06-25'),
    (@ProjectID, 'Networking & Security', 2, '2024-05-25', '2024-05-25'),
    (@ProjectID, 'Monitoring & Scaling', 3, '2024-05-25', '2024-05-25');

    DECLARE @ClusterSetupID INT = (SELECT [sectionID] FROM [Sections] WHERE [projectID] = @ProjectID AND [sectionName] = 'Cluster Setup');
    DECLARE @NetworkingID INT = (SELECT [sectionID] FROM [Sections] WHERE [projectID] = @ProjectID AND [sectionName] = 'Networking & Security');
    DECLARE @MonitoringID INT = (SELECT [sectionID] FROM [Sections] WHERE [projectID] = @ProjectID AND [sectionName] = 'Monitoring & Scaling');

    INSERT INTO [Tasks] ([sectionID], [parentTaskID], [taskName], [taskDescription], [taskPriority], [taskPosition], [dueDate], [createdAt], [updatedAt])
    VALUES
    (@ClusterSetupID, NULL, 'Setup Kubernetes Cluster', 'Initialize and configure a new Kubernetes cluster.', 2, 1, '2024-07-01', '2024-06-01', '2024-06-01'),
    (@ClusterSetupID, NULL, 'Configure Nodes', 'Add worker nodes to the cluster and verify connectivity.', 2, 2, '2024-07-10', '2024-06-06', '2024-06-06'),
    (@ClusterSetupID, NULL, 'Install Helm', 'Deploy Helm to manage Kubernetes packages.', 1, 3, '2024-07-15', '2024-06-24', '2024-06-24'),
    
    (@NetworkingID, NULL, 'Configure Network Policies', 'Define network policies to control pod communication.', 2, 1, '2024-06-20', '2024-04-01', '2024-04-01'),
    (@NetworkingID, NULL, 'Setup Ingress Controller', 'Deploy an ingress controller for handling HTTP traffic.', 2, 2, '2024-06-25', '2024-06-15', '2024-06-15'),
    (@NetworkingID, NULL, 'Enable Service Mesh', 'Implement Istio or Linkerd for traffic management.', 3, 3, '2024-07-05', '2024-06-22', '2024-06-22'),
    
    (@MonitoringID, NULL, 'Setup Prometheus Monitoring', 'Deploy Prometheus for cluster observability.', 2, 1, '2024-06-30', '2024-04-01', '2024-04-01'),
    (@MonitoringID, NULL, 'Configure Logging with ELK', 'Implement ELK Stack for centralized logging.', 3, 2, '2024-07-08', '2024-06-16', '2024-06-16'),
    (@MonitoringID, NULL, 'Auto-Scaling Configuration', 'Set up horizontal pod autoscalers.', 3, 3, '2024-07-12', '2024-06-20', '2024-06-20');

    DECLARE @Task1 INT = (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Setup Kubernetes Cluster');
    DECLARE @Task2 INT = (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Configure Nodes');
    DECLARE @Task3 INT = (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Install Helm');

    INSERT INTO [Tasks] ([sectionID], [parentTaskID], [taskName], [taskDescription], [taskPriority], [taskPosition], [dueDate], [createdAt], [updatedAt], [dateCompleted])
    VALUES
    (@ClusterSetupID, @Task1, 'Initialize Kubernetes Master', 'Run kubeadm init and configure master node.', 2, 1, '2024-06-25', '2024-06-02', '2024-06-24', '2024-06-24'),
    (@ClusterSetupID, @Task2, 'Join Worker Nodes', 'Execute join command on worker nodes.', 2, 2, '2024-07-05', '2024-06-10', '2024-06-10', NULL),
    (@ClusterSetupID, @Task3, 'Verify Helm Installation', 'Ensure Helm is correctly installed and working.', 1, 3, '2024-07-10', '2024-05-24', '2024-05-24', NULL);

    INSERT INTO [ProjectAssignees] ([userID], [projectID])
    VALUES ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user1_github'), @ProjectID),
           ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user2_github'), @ProjectID),
           ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user3_github'), @ProjectID);

    INSERT INTO [TaskAssignees] ([userID], [taskID])
    VALUES
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user1_github'), @Task1),
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user1_github'), (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Initialize Kubernetes Master')),
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user1_github'), @Task2),
    
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user2_github'), @Task3),
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user2_github'), (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Verify Helm Installation')),
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user2_github'), (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Setup Prometheus Monitoring')),
    
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user3_github'), (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Setup Ingress Controller')),
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user3_github'), (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Configure Logging with ELK')),
    ((SELECT [userID] FROM [Users] WHERE [gitHubID] = 'user3_github'), (SELECT [taskID] FROM [Tasks] WHERE [taskName] = 'Auto-Scaling Configuration'));

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    ROLLBACK TRANSACTION;
    PRINT 'Transaction rolled back due to an error';
    THROW;
END CATCH;
