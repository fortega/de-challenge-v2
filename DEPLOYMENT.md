# Deployment

To run this ETL you have diferent options:

- Docker
- SBT (Scala Building Tool)

## Using docker (recommended)

With this option you will run a "standalone" spark server inside a container. With this option you only need docker.

### Build the container

> docker build -t challenge .

Where `challenge` is the tag name of the image.

The "unit tests" run before packaging the app to stop the build in case of problems.

### Run the container

> docker run -it --rm -v $PWD/data:/app/input -v $PWD/output:/app/output challenge input output

Where:

- `-v $PWD/data:/app/input` mount the `data` directory in the current folder in `/app/input`
- `-v $PWD/output:/app/output` mount the `output` directory in current folder to `/app/output`
- `challenge` is the tag name of the image to run.
- `input` is the imput directory (inside container).
- `output` is the output directory (inside container).

## Using sbt

### Requirements

- Java Runtime Environment 8 (OpenJDK HotSpot 1.8u312 recommended).
- SBT 1.4.9 +

### Run tests with coverage report

> sbt coverage test coverageReport

The report could be found in [target/scala-2.13/scoverage-report](target/scala-2.13/scoverage-report/index.html).
The `covertura.xml` file is generated to enforce coverage in a CI/CD enabled environment (out of the scope of this proyect).

### Run application directy

> sbt "run data output"

Where:

- `data` is the input directory.
- `output` is the output directory.

### Generate package (JAR)

To run this job in a [cluster](https://spark.apache.org/docs/latest/cluster-overview.html) (for example [Google Dataproc](https://cloud.google.com/dataproc/)) you need to "package" this application into a "JAR".
To generate the package run:

> sbt package

The package (JAR) is found in `target/scala-2.13`.

The way to submit a job changes for every cluster type. Please check the [spark documentation](https://spark.apache.org/docs/latest/submitting-applications.html) for more information.