# Architectural case

## Data processing model

![logic diagram](img/logic-diagram.png)

I recommend using the "[Beam model](https://www.oreilly.com/radar/the-world-beyond-batch-streaming-101/)" as architecture solution. The main reasons are:

- Handle of batch and stream data for input and output: 
  - Is posible to cross data from static and real time sources.
  - Writing to static (like buckets or databases) and real time (like topics) destinations at the same time allow to share the same data in multiples ways.
- Is possible to group data in [fixed times windows](https://beam.apache.org/documentation/programming-guide/#windowing). This make posible to send information to "bet sites" every 5 minutes.
- Handling streaming errors gracefully:
  - This process include "player telemetry" from external service, so its more prone to have reliability problems (like late data arrival or in wrong order).
  - In case of problems with already processed data, the Beam Model provide ways to reprocess the data (and modify the already saved data) or discard it.
- Only one data-pipe, unlike the "[lambda architecture](https://en.wikipedia.org/wiki/Lambda_architecture)", makes it easier to mantain and need less infraestructure.
- Exactly-once processing to warranty the data quality.

## Software proposals / recomendations

The "Beam model" is widely [promoted by Google](https://cloud.google.com/blog/products/gcp/why-apache-beam-a-google-perspective), but it can be implemented in diferrent platforms.
I recommends 3 platforms, basend on the existing hardware infraestructure:

- Bare-metal servers: For more traditional "server" infraestructure (servers or virtual machines).
- Container native: For Kubernetes clusters.
- Google Cloud Platform: For no investment or "pay as you go".

| Type / Platform | Streaming service ![Icon Streaming](img/icon-streaming.png) | Storage service ![Icon Storage](img/icon-storage.png) | Processing Service ![Icon Processing](img/icon-processing.png) |
|---|---|---|---|
| Bare Metal | Apache Kafka | Apache Hadoop (HDFS) | Apache Beam (process) + Apache Flink (runner) |
| Container native | NATS | MinIO | Akka cluster |
| Google Cloud Platform | PubSub | Input: CloudStorage / Output: BigQuery | DataFlow |
