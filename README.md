# TheRoom

[![Build Status](https://drone.kepr.org/api/badges/AdamBremholm/TheRoom/status.svg)](https://drone.kepr.org/AdamBremholm/TheRoom)
[![codecov](https://codecov.io/gh/AdamBremholm/TheRoom/branch/develop/graph/badge.svg)](https://codecov.io/gh/AdamBremholm/TheRoom)

## mySQL setup:
* create database theroom;
* create user 'springuser'@'%' identified by 'ThePassword';
* grant all on db_example.* to 'springuser'@'%';

## rabbitMQ:
* install erlang 
* install rabbitMQ
* enable rabbitMQ plugin rabbitmq_stomp
* optional: enable the control panel plugin 
*
* plugins are installed with rabbitmq-plugins in your sbin directory.
* rabbitmq-plugins enable rabbitmq_stomp
* rabbitmq-plugins enable rabbitmq_management


## How to run :

* Download latest release at https://github.com/AdamBremholm/TheRoom/releases/latest
* Start up rabbitmq and mysql services
* Check that port 8080 is open

* run the jar file with:
```
java -jar theroomapp-0.0.1-SNAPSHOT 
```
*Go to http://localhost:8080
