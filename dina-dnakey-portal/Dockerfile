FROM openjdk:8-jdk-stretch
 
WORKDIR /usr/local/blast
RUN curl ftp://ftp.ncbi.nlm.nih.gov/blast/executables/blast+/2.2.30/ncbi-blast-2.2.30+-x64-linux.tar.gz | tar -zxC /usr/local/blast --strip-components=1
 

COPY target/dnakey-swarm.jar /usr/src/myapp/
WORKDIR /usr/src/myapp

RUN mkdir -p /usr/local/blast/tempfastafiles /usr/local/blast/db
COPY ./data/dnakey/geo_coords.tsv /usr/geo_coords.tsv
COPY ./data/blast/db/* /usr/local/blast/db/

ENTRYPOINT [""]
EXPOSE 8080
CMD ["java", "-jar", "dnakey-swarm.jar", "-Sinitdata"]