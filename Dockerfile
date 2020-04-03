FROM openjdk:8-jdk-alpine

VOLUME /tmp

ARG JAR_FILE
COPY ${JAR_FILE} service.jar

RUN echo 'hosts: files mdns4_minimal [NOTFOUND=return] dns mdns4' >> /etc/nsswitch.conf && \
    grep '^networkaddress.cache.ttl=' ${JAVA_HOME}/jre/lib/security/java.security || echo 'networkaddress.cache.ttl=10' >> ${JAVA_HOME}/jre/lib/security/java.security && \
    grep '^networkaddress.cache.negative.ttl=' ${JAVA_HOME}/jre/lib/security/java.security || echo 'networkaddress.cache.negative.ttl=10' >> ${JAVA_HOME}/jre/lib/security/java.security


ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=distrib", "-jar","/service.jar"]