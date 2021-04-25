FROM maven:3.6.3-jdk-14

ADD . /usr/src/webcrawler
WORKDIR /usr/src/webcrawler
ENTRYPOINT ["mvn", "clean", "verify", "exec:java"]
