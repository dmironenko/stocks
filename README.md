# Description
This project consists of: 
- backend: Spring Boot application
- frontend: Angular application

# Requirement
Backend
- JDK 8

Frontend
- Angular CLI: 1.6.4
- Node: 8.9.0
- Angular: 5.2.6

Docker

# Technologies
Frontend and backend are separate application and implemented to run in the separate docker containers.

Backend build on top of spring-boot
- REST API          - spring-web
- access db         - spring-data and Hibernate
- in memory storage - H2

In memory db + Hibernate is used to handle concurrency and lastUpdate timestamp.

Frontend is angular application with material design.

# API
Method       | Path          | Description   |
------------ | ------------- | ------------- |
GET | /stocks      | get a page of stocks (query parameters ?page={pageNumber}&size={sizeOfpage})
GET | /stocks/{id} | get a stock by id
POST| /stocks      | create a stock
PUT | /stocks/{id} | update a stock

# Docker
Frontend and backend have Dockerfile-s to create images. 
- frontend runs in nginx server with additional configuration to access backend (see stocks-ui\nginx\default.conf)
- backend uses openjdk-8 container.

# How to create images
run ./create_image.sh in each project or ./create_images.sh from root

# How to run
Project contains docker-compose.yml file to run both containers.
To start applications(after images are built) run docker-compose up -d. Frontend will run on the port 80.

# How to run as developer
- backend:  run AssignmentApplication.main from your IDE with spring profile dev
- frontend: run npm install; npm start;