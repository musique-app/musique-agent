FROM arm64v8/openjdk:17-jdk

LABEL org.opencontainers.image.source=https://github.com/polemonLX/musique-agent

WORKDIR /musique

RUN apt-get update && apt-get install -y apt-utils
RUN apt-get install -y libc-bin && dpkg --configure -a
RUN apt-get install -y pulseaudio

RUN mkdir -p lib

COPY ../target/musique-agent.jar lib/musique-agent.jar

CMD ["java", "-jar", "lib/musique-agent.jar"]

## WINDOWS
# docker run -e PULSE_SERVER=host.docker.internal musique-agent

## LINUX
# docker run -e PULSE_SERVER=localhost musique-agent