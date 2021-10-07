FROM openjdk:8-jdk-alpine

VOLUME /tmp

ARG JAR_FILE
COPY ${JAR_FILE} service.jar

RUN echo 'hosts: files mdns4_minimal [NOTFOUND=return] dns mdns4' >> /etc/nsswitch.conf && \
    grep '^networkaddress.cache.ttl=' ${JAVA_HOME}/jre/lib/security/java.security || echo 'networkaddress.cache.ttl=10' >> ${JAVA_HOME}/jre/lib/security/java.security && \
    grep '^networkaddress.cache.negative.ttl=' ${JAVA_HOME}/jre/lib/security/java.security || echo 'networkaddress.cache.negative.ttl=10' >> ${JAVA_HOME}/jre/lib/security/java.security


ENTRYPOINT ["java","-XX:+UseG1GC","-XX:+UseStringDeduplication", "-XX:ParallelGCThreads=10", "-XX:ConcGCThreads=3", "-XX:InitiatingHeapOccupancyPercent=40", "-XX:+UseContainerSupport", "-XX:MinRAMPercentage=80.0", "-XX:MaxRAMPercentage=80.0", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=distrib", "-jar", "/service.jar"]