FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y pulseaudio

WORKDIR /musique

RUN mkdir -p lib

COPY ../target/musique-agent.jar lib/musique-agent.jar

CMD ["java", "-jar", "lib/musique-agent.jar"]

## WINDOWS
# docker run -e PULSE_SERVER=host.docker.internal musique-agent

## LINUX
# docker run -e PULSE_SERVER=localhost musique-agent





## INSTALLATION
# systemctl ...
# sudo apt-get install docker.io
# docker login ghcr.io
# docker pull musique-agent
# docker run -e PULSE_SERVER=localhost musique-agent -e username polemonunidade password 123456789

# .sh /scriptinstall.sh


## RUNTIME
# docker run -e PULSE_SERVER=localhost musique-agent -e username polemonunidade password 123456789

