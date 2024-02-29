FROM openjdk:17-jdk

VOLUME /tmp

EXPOSE 8082

ARG JAR_FILE=target/data-entry-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar


ENV SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3307/school_management_4
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=1234

ENV SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQLDialect

ENTRYPOINT ["java","-jar","/app.jar"]
