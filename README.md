# DINA-Web dnakey portal


The DINA-Web dnakey portal is a search tool to perform similarity searches between an unknown DNA sequence and a large number of sequences stored in several databases.

# pre-req


BLAST+ executables tool.

You can download from:
ftp://ftp.ncbi.nlm.nih.gov/blast/executables/blast+/2.2.30/ncbi-blast-2.2.30+-x64-linux.tar.gz


Solr:
solr-4.10.2


# Usage

Start solr service.

Download this source code.
cd to directory dina-dnakey-original

Update project-initdata.xml file with solr and blast path

To run the portal local:
mvn package

java -jar dina-dnakey-portal/target/dnakey-swarm.jar -Sinitdata

Now you can access the portal with url:

http://localhost:8080/dnakey

To run with docker:

make

This will package source code with maven, and build a docker image of dina-dnakey-portal ui, and start up docker container.

Now you can access the portal with url:

beta.dnakey.se

You also need to add beta.dnakey.se in /etc/hosts file.



