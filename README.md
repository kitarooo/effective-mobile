# Readme for test project - Effective Mobile

Development of Task Management System using Java. The system provides creation, editing, deletion and viewing of tasks. Each task contains a title, description, status (e.g., "pending", "in progress", "completed") and priority (e.g., "high", "medium", "low"), as well as task author and executor. Only the API needs to be implemented.


The service supports authentication and authorization of users by email and password

Access to API must be authenticated using JWT token


Users can manage their tasks: create new tasks, edit existing ones, view and delete, change status and assign task executors


Users can view other users' tasks, and task executors can change the status of their tasks


Tasks can be commented on


The API allows receiving tasks of a particular author or executor, as well as all comments to them


The service handles errors correctly and returns clear messages


The service is well documented. APIs are described with the help of Open API and Swagger. Swagger UI is configured in the service


Several basic tests are written to test the basic functions of my system


Stack: Java, Spring, PostgreSQL, Docker.


There are 2 ways to launch the application: locally (without docker image of the application) and use docker

In both cases, you need Docker Desktop already installed

# Locally:

1) Copy the repository to your directory on your desktop

2) Open this repository using Intellijj IDEA or a similar development tool
 
3) Open the console manually in this directory or use the terminal inside IDEA and enter the command "docker-compose up -d" to start the database
 
4) Once the database has been successfully started, run the project locally

5) Documentation at url:http://localhost:8080/documentation/

# Docker 

1) As I pointed out earlier - you need Docker Desktop installed on your computer

2) Execute the above commands: copy the repository to your directory, open in IDEA

3) Go to the dev branch

4) In the terminal, type the command: docker-compose up -d

5) Test the application in Postman
