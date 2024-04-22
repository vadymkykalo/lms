# Define a standard target that will be executed by default if a specific target is not specified.
.PHONY: up down build

up:
	docker-compose up -d

down:
	docker-compose down

downv:
	docker-compose down -v

rebuild:
	down build up

build:
	docker-compose build

ps:
	docker-compose ps