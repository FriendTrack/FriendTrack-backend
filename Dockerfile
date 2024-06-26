FROM openjdk:21-jdk as build

ARG JAR_FILE=build/libs/FriendTracker-1.0.0-SNAPSHOT.jar
WORKDIR /build

ADD $JAR_FILE application.jar
RUN java -Djarmode=layertools -jar application.jar extract --destination extracted

FROM openjdk:21-jdk

RUN groupadd spring-boot-group && useradd -g spring-boot-group spring-boot
USER spring-boot
VOLUME /tmp
WORKDIR /application

COPY --from=build /build/extracted/dependencies .
COPY --from=build /build/extracted/spring-boot-loader .
COPY --from=build /build/extracted/snapshot-dependencies .
COPY --from=build /build/extracted/application .

ENTRYPOINT exec java ${JAVA_OPTS} org.springframework.boot.loader.launch.JarLauncher ${0} ${@}