# ListifyDB Documentation

## Purpose
The `ListifyDB` is the database structure for the **Listify** task management application. This database is designed to manage users, teams, projects, sections, tasks, and the relationships between these entities. The schema supports features such as task assignment, task dependencies, project management, and team collaboration.

The purpose of the database is to provide a structured and efficient way of storing and managing data related to tasks and projects for teams. It includes relationships between users and their roles within teams, as well as the hierarchical structure of tasks within projects.

---

## Database Schema Overview
The database consists of several key tables, each serving a specific purpose:

- **Users**: Stores information about users
- **Teams**: Contains data about different teams within the application.
- **TeamMembers**: Links users to teams and tracks their roles.
- **Projects**: Contains data about projects belonging to teams.
- **Sections**: Each project can have multiple sections for better task categorization.
- **Tasks**: Stores tasks with attributes such as priority, description, and completion status.
- **TaskAssignees**: Tracks which users are assigned to tasks.
- **TaskDependencies**: Tracks dependencies between tasks.
- **ProjectAssignees**: Tracks users assigned to projects.

The relationships between tables are managed via **foreign keys** to maintain referential integrity.

---

## Setting Up the Database
Follow the steps below to set up the `ListifyDB` and populate it with initial data. The following files are found in the `DefineDB` folder.

### Step 1: Create the Database
To set up the database, execute the `1-createDatabase.sql` script to create all the necessary tables. This will define the structure and relationships between the entities in the database.

### Step 2: Populate the Database with Sample Data
Once the database tables are created, execute the `2-populateDatabase.sql` script. This script adds sample data to the database, including users, teams, projects, sections, tasks, and assignments.

### Step 3: Add a Marketing Team
To simulate the addition of a new team, execute the `3-addMarketingTeam.sql` script. This will create a new team in the Teams table and add relevant members and projects as part of the setup.

### Step 4: Add a Project in the Development Team
To add a new project under the Development team, execute the `4-addProjectInDevTeam.sql` script. This will create a new project, assign sections to it, and set up tasks under that project.

## Functions
Functions are located in the `Functions` folder and provide reusable logic for specific operations within the database. These can be used retrieve specific data points without needing to repeatedly write complex queries.

## Stored Procedures
All stored procedures used in the ListifyDB are located in the `Procedures` folder. These procedures are designed to simplify common database operations, such as task assignment, project management, and team membership. You can use these stored procedures to interact with the database efficiently.

## Views
Views in the `Views` folder are pre-defined queries that aggregate data for easier access. These views simplify the process of retrieving complex datasets without writing extensive SQL queries.