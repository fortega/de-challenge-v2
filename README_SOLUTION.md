# Solutions

## ETL

This project uses the [Scala programming language](https://www.scala-lang.org/) with [Apache Spark](https://spark.apache.org/) as processing engine. Is implemented using the "[ports and adapters](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software))" pattern to make it easier to adapt to new input-output types keeping the logic (transformations) intact.

This folder is a Scala project that uses [SBT](https://www.scala-sbt.org/) as build tool. The `src` folder contain all the project files (including the unit tests). Please check [DEPLOYMENT.md](DEPLOYMENT.md) for instructions of how to run / test this project.

## Architecture case

The `architecture case` folder contains a [readme file](architecture%20case/README.md) with the solutions and a small explanation of the suggested architecture for different environments.

## Deployment

The [DEPLOYMENT.md](DEPLOYMENT.md) file contains instructions of how to run this project using Docker y SBT. Also include information to package the project for a cluster (recommended if the size of the dataset increases to more than 1GB).