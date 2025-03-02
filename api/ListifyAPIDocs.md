# Listify API Documentation

## Overview
Listify is a task management and productivity tool designed to help users organize and track their projects, teams, tasks, and other key elements in a collaborative environment. This document provides a description of some of the REST API endpoints available for Listify. Each endpoint is designed to manage different aspects of the application, such as users, projects, tasks, teams, and sections.

## Teams Endpoints (`/teams`)

### `GET /teams`
Retrieves a list of all teams.

#### Responses: 
- **200 OK**: Returns a list of teams

### `POST /teams`
Creates a new team.

#### Parameters:
**Team details** (e.g name, userID)

#### Responses:
- **201 CREATED**: Returns the created team.

### `GET /teams/{teamID}`
Retrieves a specific team by its ID.

#### Parameters:
**teamID** (path parameter, required): The ID of the team.

#### Responses:
- **200 OK**: Returns the team.
- **404 NOT FOUND**: Team not found.

### `PUT /teams/{teamID}`
Updates an existing team.

#### Parameters:
- **teamID** (path parameter, required): The ID of the team.
- **team details** (e.g., name, userID).
#### Responses:
- **200 OK**: Returns the updated team.
- **404 NOT FOUND**: Team not found.

### `POST /teams/{teamID}/assignMember`
Assigns a member to a specific team.

#### Parameters:
- **teamID** (path parameter, required): The ID of the team.
- **memberID** (query parameter, required): The ID of the member to be assigned.
#### Responses:
- **200 OK**: Member successfully assigned.
- **400 BAD REQUEST**: Missing or invalid parameters.


## Tasks Endpoints (`/tasks`)
### `GET /tasks`
Retrieves a list of all tasks.

#### Responses:
- **200 OK**: Returns a list of tasks.

### `GET /tasks/subtask/{parentTaskID}`
Retrieves subtasks of a specific parent task.

#### Parameters:
**parentTaskID** (path parameter, required): The ID of the parent task.
#### Responses:
- **200 OK**: Returns the list of subtasks.
- **404 NOT FOUND**: Parent task not found.

### `POST /tasks`
Creates a new task.

#### Parameters:
**task details** (e.g., title, description, priority, etc.).
#### Responses:
- **201 CREATED**: Returns the created task.
- **400 BAD REQUEST**: Missing required parameters.

### `POST /tasks/subtask/{parentTaskID}`
Creates a new subtask for a given parent task.

#### Parameters:
**parentTaskID** (path parameter, required): The ID of the parent task.
#### Responses:
- **201 CREATED**: Returns the created subtask.
- **400 BAD REQUEST**: Missing required parameters.

### `PUT /tasks/{id}`
Updates an existing task.

#### Parameters:
- **id** (path parameter, required): The ID of the task.
- **task details** (e.g., title, description, priority, etc.).
#### Responses:
- **200 OK**: Returns the updated task.
- **404 NOT FOUND**: Task not found.

## Projects Endpoints (`/projects`)
### `GET /projects`
Retrieves a list of all projects.

#### Responses:
200 OK: Returns a list of projects.

### `GET /projects/{id}`
Retrieves a project by its ID.

#### Parameters:
**id** (path parameter, required): The ID of the project.
#### Responses:
- **200 OK**: Returns the project.
- **404 NOT FOUND**: Project not found.

### `GET /projects/{id}/sections`
Retrieves sections within a specific project.

#### Parameters:
**id** (path parameter, required): The ID of the project.
#### Responses:
- **200 OK**: Returns the sections within the project.
- **404 NOT FOUND**: Project not found.

### `POST /projects`
Creates a new project.

#### Parameters (required):
- **teamLeaderID** (query parameter): ID of the team leader.
- **teamID** (query parameter): ID of the team associated with the project.
- **projectName** (query parameter): Name of the new project.
- **projectDescription** (query parameter, optional): Description of the project.
#### Responses:
- **201 CREATED**: Returns the created project.
- **400 BAD REQUEST**: Missing required parameters.

## Sections Endpoints (`/sections`)
### `GET /sections`
Retrieves a list of all sections.

#### Responses:
**200 OK**: Returns a list of sections.


### `GET /sections/{id}`
Retrieves a section by its ID.

#### Parameters:
**id** (path parameter, required): The ID of the section.
#### Responses:
- **200 OK**: Returns the section.
- **404 NOT FOUND**: Section not found.

### `GET /sections/{id}/tasks`
Retrieves tasks associated with a specific section.

#### Parameters:
**id** (path parameter, required): The ID of the section.
#### Responses:
- **200 OK**: Returns the tasks in the section.
- **404 NOT FOUND**: Section not found.

### `POST /sections`
Creates a new section.

#### Parameters:
**section details** (e.g., name, position).
#### Responses:
**201 CREATED**: Returns the created section.

### `PUT /sections/{id}`
Updates an existing section.

#### Parameters:
- **id** (path parameter, required): The ID of the section.
- **section details** (e.g., name, position).
#### Responses:
- **200 OK**: Returns the updated section.
- **404 NOT FOUND**: Section not found.

### `DELETE /sections/{id}`
Deletes a section by its ID.

#### Parameters:
**id** (path parameter, required): The ID of the section to be deleted.
#### Responses:
- **200 OK**: Section successfully deleted.
- **404 NOT FOUND**: Section not found.

---

## Conclusion
This documentation provides an overview of the Listify API and its endpoints. Use the appropriate HTTP methods to interact with the application's resources, ensuring that all required parameters are included in the request.