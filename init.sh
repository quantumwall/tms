#!/bin/bash
./mvnw clean && ./mvnw package -Dmaven.test.skip=true && docker compose up
