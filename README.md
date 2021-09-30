# ScentDB
[![Build Status](https://travis-ci.com/remus-selea/scentdb.svg?branch=master)](https://travis-ci.com/remus-selea/scentdb)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.github.remus-selea%3Ascentdb&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.remus-selea%3Ascentdb)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.github.remus-selea%3Ascentdb&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.github.remus-selea%3Ascentdb)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=com.github.remus-selea%3Ascentdb&metric=bugs)](https://sonarcloud.io/dashboard?id=com.github.remus-selea%3Ascentdb)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.github.remus-selea%3Ascentdb&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.github.remus-selea%3Ascentdb)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=com.github.remus-selea%3Ascentdb&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=com.github.remus-selea%3Ascentdb)

A Spring Boot backend web application of an online fragrance database cataloging and presenting perfumes, perfumers, brands and aromas that can all be searched and filtered.
ScentDB aspires to provide a comprehensive set of resources about fragrances through its REST API.

<h3>
  <a href="https://github.com/remus-selea/scentdb-ui">Go to the react frontend</a>
</h3>

--------

## Features
- Multi container Docker Compose application
- Built using Spring Boot and Java 11
- Log management using Elasticsearch, Logstash and Kibana (ELK Sack)
- Hibernate Search to take care of the database and the Elasticsearch index
- Oauth2 login with Google, Facebook and GitHub as the currently supported providers
- Elasticsearch powered fuzzy search, filtering and sorting
- Exposes a REST API to create, retrieve and delete perfumes, notes, perfumers and companies
- Image storage and retrieval from the filesystem
- Multiple Spring profiles for development and testing
- Continuous integration using Travis CI
- Static code analysis with SonarCloud

## Setting up the development environment
- Java, Docker and Docker Compose are required
- Create an empty .env file in the root directory of the project, and copy the contents of the .env.example into it. Change the environment variables to fit your needs.
- Open a terminal in the root directory of ScentDB.
- Build the project with `gradlew clean assemble`.
- Build the application docker image with `docker build .`.
- Start the application stack with `docker-compose up`.

## API documentation
- Once the application is up and running, Swagger UI is available at:  
`http://localhost:8321/api/swagger-ui`  
- There's also an insomnia collection available:  
[Download the insomnia collection here](https://github.com/remus-selea/scentdb/files/7254188/Insomnia_2021-09-29.zip)

## License
[MIT](https://choosealicense.com/licenses/mit/)
