FROM java:8

ENV VERSION=1.1.0-SNAPSHOT

RUN wget https://bintray.com/artifact/download/jchampemont/wtfdyum/wtfdyum-${VERSION}.zip && \
    unzip wtfdyum-${VERSION}.zip && \
    rm wtfdyum-${VERSION}.zip && \
    cd wtfdyum-${VERSION} && \
    sed -i "s/wtfdyum.redis.server=localhost/wtfdyum.redis.server=redis/g" application.properties && \
    mv wtfdyum-${VERSION}.jar wtfdyum.jar

WORKDIR wtfdyum-${VERSION}

EXPOSE 8080

COPY ["startup.sh","./"]

RUN chmod +x ./startup.sh

ENTRYPOINT ./startup.sh
