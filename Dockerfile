FROM library/alpine:latest

CMD ["/bin/sh"]
ENV LANG=C.UTF-8 \
    JAVA_VERSION=17.0.7 \
    JAVA_ALPINE_VERSION=17.0.7_p7-r1 \
    JAVA_HOME=/usr/lib/jvm/java-17-openjdk \
    PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/lib/jvm/java-17-openjdk/jre/bin:/usr/lib/jvm/java-17-openjdk/bin
RUN /bin/sh -c echo '#!/bin/sh'; echo 'set -e'; echo; echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"';  > /usr/local/bin/docker-java-home && chmod +x /usr/local/bin/docker-java-home && \
/bin/sh -c set -x && apk add --no-cache openjdk17="$JAVA_ALPINE_VERSION" && [ "$JAVA_HOME"="$(docker-java-home)" ] && \
echo 'jdk.tls.disabledAlgorithms=SSLv3, RC4, DES, MD5withRSA, DH keySize < 1024, EC keySize < 224, 3DES_EDE_CBC, anon, NULL, include jdk.disabled.namedCurves' > /usr/lib/jvm/javaEnableLegacyTLS.security

VOLUME /tmp

ARG JAR_FILE
COPY ${JAR_FILE} service.jar

ENTRYPOINT ["java","-XX:+UseG1GC","--add-opens=java.base/sun.reflect.annotation=ALL-UNNAMED","--add-opens=java.base/sun.security.pkcs=ALL-UNNAMED","--add-opens=java.base/sun.security.util=ALL-UNNAMED","--add-opens=java.base/sun.security.x509=ALL-UNNAMED","-XX:+UseStringDeduplication", "-XX:ParallelGCThreads=10", "-XX:ConcGCThreads=3", "-XX:InitiatingHeapOccupancyPercent=40", "-XX:+UseContainerSupport", "-XX:MinRAMPercentage=80.0", "-XX:MaxRAMPercentage=80.0", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=distrib", "-jar", "/service.jar"]
