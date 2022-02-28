FROM openjdk:8 as builder

RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add && \
    apt-get update -y && apt-get upgrade -y && apt-get install sbt

WORKDIR /code

COPY src ./src

COPY data ./data

COPY project ./project

COPY build.sbt .

RUN sbt clean test package

FROM openjdk:8

WORKDIR /spark

ADD https://dlcdn.apache.org/spark/spark-3.2.1/spark-3.2.1-bin-hadoop3.2-scala2.13.tgz .

RUN tar -xzf spark-3.2.1-bin-hadoop3.2-scala2.13.tgz && mv spark-3.2.1-bin-hadoop3.2-scala2.13/* . && rm spark-3.2.1-bin-hadoop3.2-scala2.13.tgz

WORKDIR /app

COPY --from=builder /code/target/scala-2.13/*.jar .

COPY entrypoint.sh .

ENTRYPOINT [ "sh", "entrypoint.sh" ]
