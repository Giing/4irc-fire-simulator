FROM anapsix/alpine-java
WORKDIR /usr/src/app
COPY target /usr/src/app/simulator
CMD ["java","-jar","/usr/src/app/simulator/4irc-simulator-maven-1.0-SNAPSHOT.jar"]