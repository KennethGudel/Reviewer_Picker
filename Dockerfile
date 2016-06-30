# Reviewer Picker Dockerfile
# Version: 0.1.0

# Image builds from the official Docker Java Image

FROM java:8

MAINTAINER Kenneth Gudel <KennethGudel@gmail.com>

WORKDIR /

USER daemon

ENTRYPOINT ["java", "-jar", "/opt/reviewer_picker-assembly-0.1.0.jar"]

EXPOSE 8080

COPY target/scala-2.11/reviewer_picker-assembly-0.1.0.jar /opt/reviewer_picker-assembly-0.1.0.jar
