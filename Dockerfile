FROM openjdk:16

WORKDIR /quickFood

COPY . /quickFood

RUN javac *.javac

CMD java quickFood