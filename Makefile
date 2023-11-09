# Variables for the Makefile
APP_NAME=reactive-rest-api
JAR_FILE=target/*.jar
PORT=8080

.PHONY: build run clean

build:
	docker build -t $(APP_NAME) --build-arg JAR_FILE=$(JAR_FILE) .

run:
	docker run -p $(PORT):$(PORT) $(APP_NAME)

stop:
	docker stop $(APP_NAME)
	docker rm $(APP_NAME)

clean:
	docker system prune -af

all: build run

restart: clean build run

default: all
