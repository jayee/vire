# vire - Video Recorder

vire is a simple recording service.
It uses ffmpeg to record a stream from rtsp sources (like a web camera).

The recordings can be started and stopped by using the REST service:

>GET host:8181/record/start  
>GET host:8181/record/stop

##Install

The service is preferable run with docker/docker-compose. For example

>RUN_AS=$(id -u):$(id -g) docker-compose up --no-build

### Requirements
Environment variable to the streams to record must be set, see docker-compose.yml

>VIRE_STREAMS=rtsp://stream1,rtsp://stream2

Also, the in-container directory /data must be mapped to a docker volume or host directory. 

---
This project uses Quarkus, the Supersonic Subatomic Java Framework.
If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .
