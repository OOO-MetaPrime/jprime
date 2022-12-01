FROM library/alpine:latest

CMD ["/bin/sh"]
ENV LANG=C.UTF-8
RUN /bin/sh -c echo '#!/bin/sh'; echo 'set -e'; echo; echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"'; > /usr/local/bin/docker-java-home && chmod +x /usr/local/bin/docker-java-home
ENV JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk
ENV PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin
ENV JAVA_VERSION=8u322
ENV JAVA_ALPINE_VERSION=8.345.01-r3
RUN /bin/sh -c set -x && apk add --no-cache openjdk8="$JAVA_ALPINE_VERSION" && [ "$JAVA_HOME"="$(docker-java-home)" ]

VOLUME /tmp

ARG JAR_FILE
COPY ${JAR_FILE} service.jar

ENTRYPOINT ["java","-XX:+UseG1GC","-XX:+UseStringDeduplication", "-XX:ParallelGCThreads=10", "-XX:ConcGCThreads=3", "-XX:InitiatingHeapOccupancyPercent=40", "-XX:+UseContainerSupport", "-XX:MinRAMPercentage=80.0", "-XX:MaxRAMPercentage=80.0", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=distrib", "-jar", "/service.jar"]
