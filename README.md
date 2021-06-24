# ScentDB Backend
[![GitHub license](https://img.shields.io/github/license/remus-selea/scentdb)](https://github.com/remus-selea/scentdb)
[![GitHub issues](https://img.shields.io/github/issues/remus-selea/scentdb)](https://github.com/remus-selea/scentdb/issues)
[![GitHub stars](https://img.shields.io/github/stars/remus-selea/scentdb)](https://github.com/remus-selea/scentdb/stargazers)  
[![Build Status](https://travis-ci.com/remus-selea/scentdb.svg?branch=master)](https://travis-ci.com/remus-selea/scentdb)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.github.remus-selea%3Ascentdb&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.remus-selea%3Ascentdb)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.github.remus-selea%3Ascentdb&metric=coverage)](https://sonarcloud.io/dashboard?id=com.github.remus-selea%3Ascentdb)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.github.remus-selea%3Ascentdb&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.github.remus-selea%3Ascentdb)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=com.github.remus-selea%3Ascentdb&metric=bugs)](https://sonarcloud.io/dashboard?id=com.github.remus-selea%3Ascentdb)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.github.remus-selea%3Ascentdb&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.github.remus-selea%3Ascentdb)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=com.github.remus-selea%3Ascentdb&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=com.github.remus-selea%3Ascentdb)

The backend of an online fragrance database cataloguing and presenting perfumes, perfumers, brands and aromas all of which are to be searchable.
ScentDB aspires to provide a comprehensive set of resources about fragrances through a REST API.

## Building and starting ScentDB locally

1. Open a terminal in the root directory of the project.
2. Build the project with `gradlew clean assemble`.
3. Build the application docker image with `docker build .`.
4. Start the application stack with `docker-compose up`.
