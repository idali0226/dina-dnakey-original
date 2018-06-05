#!make
PWD=$(shell pwd)
 
all: build up

.PHONY: all

init:
	@echo "working directory is ${PWD}"

build: build-ui

build-ui:
	docker run -it --rm --name my-maven-project \
		-v $(PWD):/usr/src/mymaven \
		-v $(PWD)/m2:/root/.m2 \
		-w /usr/src/mymaven \
		maven:3 bash -c "mvn package"
	make -C dina-dnakey-portal


up-local:
	docker-compose -f docker-compose.local.yml up -d proxy
	docker-compose -f docker-compose.local.yml up -d ui

up:
#	docker-compose -f docker-compose.yml up -d blast
#	docker-compose -f docker-compose.yml up -d solr
	docker-compose -f docker-compose.yml up -d ui
	
down:
	docker-compose -f docker-compose.yml down

test:
 	xdg-open https://beta.dnakey.se/dnakey

# docker login
release:
	docker push dina/dnakey:v0.1
