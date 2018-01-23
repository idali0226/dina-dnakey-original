FROM openjdk:8u111-jdk-alpine 

RUN apk update && apk add --no-cache tini openssl bash

  
COPY dina-dnakey-portal/target/dnakey-swarm.jar /usr/src/myapp/ 
WORKDIR /usr/src/myapp

ENTRYPOINT ["/sbin/tini", "--"]
EXPOSE 8080
CMD java -jar dnakey-swarm.jar -Sinitdata