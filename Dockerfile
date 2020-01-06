FROM openjdk:8
VOLUME /tmp
ADD ./target/servicio-transaction-service-0.0.1-SNAPSHOT.jar servicio-transaction.jar
ENTRYPOINT ["java","-jar","/servicio-transaction.jar"]