# Listify - Task Management Productivity Tool

Welcome to **Listify**, a task management and productivity tool built with Spring Boot. Listify helps teams collaborate, track progress, and manage projects efficiently by breaking down complex tasks and organizing them in an intuitive and structured way.

Whether you are managing individual tasks, assigning roles, or tracking task dependencies, Listify streamlines the process and ensures smooth project management.

## Features

- **Team Collaboration**: Manage teams and assign users to specific roles.
- **Project Management**: Organize and manage multiple projects per team.
- **Task Organization**: Categorize tasks into sections and set priorities.
- **Task Dependencies**: Track relationships between tasks to manage their order.
- **Calendar View**: Visualize tasks and projects using a calendar format.
- **Real-time Updates**: Stay updated with task progress and changes.

## Database Structure

Listify uses a relational database to manage its data, organized into the following entities:

- **Users**: Stores information about the users of the system.
- **Teams**: Holds data about different teams within the platform.
- **Projects**: Represents the projects associated with each team.
- **Sections**: Used to organize projects into multiple sections for better task categorization.
- **Tasks**: Stores individual tasks with details like priority, description, and completion status.
- **TaskAssignees**: Links users to specific tasks.
- **TaskDependencies**: Manages dependencies between tasks.
- **ProjectAssignees**: Tracks users assigned to specific projects.

Relationships between these entities are maintained using foreign keys to ensure referential integrity.

For detailed information on setting up the database and understanding its schema, please refer to the [DatabaseDocs.md](./db/DatabaseDocs.md) file.

## API Overview

The Listify API is built using Spring Boot and offers RESTful endpoints for easy interaction with the system. The key functionalities include:

- **Team Management**: Create, view, and manage teams.
- **Project Management**: Manage projects within teams, including creating and organizing sections.
- **Task Management**: Add, update, delete, and track tasks with support for task dependencies.
- **User Management**: Manage user accounts and their assignments within teams and projects.

For detailed API documentation, please refer to the [ListifyAPIDocs.md](./API/ListifyAPIDocs.md) file.

## Running Listify

To run Listify locally, follow these steps:

### Prerequisites

- Java 17 or later
- Maven

1. **Clone the Repository**
Clone the Listify repository to your local machine:

    ```bash 
    git clone https://github.com/Miah-Niecke-BBD/Listify.git
    ```

2. **Build the application:**

    ```bash
    mvn clean install
    ```

3. **Run the application:**

    ```bash
    mvn spring-boot:run
    ```
   
---

We hope that **Listify** helps you and your team stay organized and improve your productivity. Feel free to open issues or contribute to the project on GitHub
