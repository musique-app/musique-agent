FROM arm64v8/openjdk:17-slim

LABEL org.opencontainers.image.source="https://github.com/musique-app/musique-agent"

RUN apt-get update && apt-get install -y \
    pulseaudio \
    && rm -rf /var/lib/apt/lists/* \

WORKDIR /musique

RUN mkdir -p lib

COPY ../target/musique-agent.jar lib/musique-agent.jar

ENV PULSE_SERVER=unix:/run/pulse/native

CMD ["java", "-jar", "lib/musique-agent.jar"]

#docker run --rm --privileged multiarch/qemu-user-static --reset -p yes
#docker buildx create --use
#docker buildx inspect --bootstrap
#docker buildx build --platform linux/arm64 -t ghcr.io/musique-app/musique-agent --push -f buildfiles/Dockerfile .


#sudo echo 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 | docker login ghcr.io -u musique-app --password-stdin
#sudo docker pull ghcr.io/musique-app/musique-agent:latest
#aplay -l
#sudo amixer cset numid=3 1
#pulseaudio --start
#pulseaudio --check
#sudo docker run --rm --device /dev/snd -v /run/user/1000/pulse:/run/pulse -e USERNAME=polemonunidade -e PASSWORD=123456789 -e PULSE_SERVER=unix:/run/pulse/native ghcr.io/musique-app/musique-agent:latest

## WINDOWS
# docker run -e USERNAME=polemonunidade -e PASSWORD=123456789 -e PULSE_SERVER=host.docker.internal musique-agent

## LINUX
# sudo docker run -e USERNAME=polemonunidade -e PASSWORD=123456789 -e PULSE_SERVER=localhost musique-agent
