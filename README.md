# Purpose and Description
I created this application for a capstone project at the end of my Software Development degree program. The project's topic required approval from faculty, and had three main components:
1. Formal written proposal document
2. Software development project (this GitHub repo)
3. Written report

The idea that I ran with to meet the capstone requirements as well as my personal goals for the project was to create a system for managing all things related to home ownership. To me, the topic was not the important part though. I had a few personal interests and goals for the project, and this topic allowed me to explore those.

# Personal Goals
I saw this capstone project as a way to challenge myself to learn something new, so I incorporated technologies and processes with which I had no previous experience. 

- Create a web application with two main components: 
  - REST API backed with a MySql database.
  - client website (simple, with basic functionality to utilize the API).
- Create the application using Spring Boot.
- Incorporate cloud technologies.
  - Used AWS S3 for storing files uploaded through the API.
  - Originally, the API was hosted using AWS Elastic Beanstalk and RDS, with the client website on Heroku.

**NOTE:**  
Now that I have finished this course and degree, I've reorganized the project and GitHub repo to make a demo easier to run. The project can now be run using docker-compose, which run the containerized database and Spring Boot application.

**NOTE:**  
This is in no way a production-ready application.

# Running the Demo Application
- Download the repository using git and navigate to the project directory:
  - `git clone https://github.com/clstephenson/home-info-system.git`
  - `cd home-info-system`
  - `cd docker`
- Start the database container: 
  - `docker-compose up`
  - Two containers will be started. One running a mysql server with test data, and another running the Spring Boot application.
  - Access the application in a web browser by going to http://localhost:5000.
    - Username: `user`
    - Password: `password`
