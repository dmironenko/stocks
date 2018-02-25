# Description
This project contains: 
- backend  - spring boot app
- frontend - angular app.

# Requirement
Backend:
Java 8

Frontend:
Angular CLI: 1.6.4
Node: 8.9.0
Angular: 5.2.6

Docker

# API
Method       | Path          | Description   |
------------ | ------------- | ------------- |
GET | /stocks      | get a page of stocks (query parameters ?page={pageNumber}&size={sizeOfpage})
GET | /stocks/{id} | get a stock by id
POST| /stocks      | create a stock
PUT | /stocks/{id} | update a stock

# Docker
Frontend and backend have Dockerfile-s to create images. 
- backend uses openjdk-8 container.
- frontend runs in nginx server with additional configuration to access backend (see stocks-ui\nginx\default.conf)

# How to create images
run ./create_image.sh in each project.

# How to run
Project contains docker-compose.yml file to run both containers.
run docker-compose up -d

# How to run as developer
- backend:  run AssignmentApplication.main from your IDE with spring profile dev
- frontend: run npm install; npm start;