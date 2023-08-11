# java-shareit

Service for sharing things. The service gives users the opportunity to choose what things they are willing to share, as well as find the right thing and rent it for a while.

The web service is written in Java 11 based on Spring Boot and has a multi-module structure. REST API, PostgreSQL, Hibernate ORM, Lombok, Docker.


Microservice architecture:

- gateway: controller that validates the input data;
- server: server side of the application (controllers, services, repositories)
- db: PostgreSql database

The user who adds a new item to the application is considered its owner. When adding a thing, it is possible to specify its short name and add a small description. A search is organized to search for things. To use the right thing, you need to book it. The item is always booked for certain dates. The owner of the item must confirm the booking. After the item is returned, the user who rented it has the opportunity to leave a review.

## ER Diagram
<img width="569" alt="scheme" src="https://github.com/Ivan-Mol/java-shareit/assets/94922468/eb269000-0eee-4071-b806-c6b429a8c4ba">
