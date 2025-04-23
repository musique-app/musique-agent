FROM arm64v8/openjdk:17-slim

LABEL org.opencontainers.image.source = "https://github.com/musique-app/musique-agent"

RUN apt-get update && apt-get install -y \
    pulseaudio \
    && rm -rf /var/lib/apt/lists/* \

WORKDIR /musique

RUN mkdir -p lib

COPY ../target/musique-agent.jar lib/musique-agent.jar

ENV PULSE_SERVER=unix:/run/pulse/native

CMD ["java", "-jar", "lib/musique-agent.jar"]

## WINDOWS
# docker run -e PULSE_SERVER=host.docker.internal musique-agent

## LINUX
# docker run -e PULSE_SERVER=localhost musique-agent
