# Deployment

To run this ETL you have diferent options:

- Using docker
- Using SBT

## Using docker (recommended)

With this option you will run a "standalone" spark server inside a container. With this option you only need docker.

### Build the container

> docker build -t challenge .

Where `challenge` is the tag name of the image.

The test are run before packaging the app to stop the build in case of problems.

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

- Java Runtime Environment 8 (OpenJDK HotSpot recommended).
- SBT 1.4.9 +

### Run tests with coverage report

> sbt coverage test coverageReport

The report could be found in [target/scala-2.13/scoverage-report](target/scala-2.13/scoverage-report/index.html).
The `covertura.xml` file is generated to enforce coverage in a CI/CD enabled environment (out of the scope of this proyect).

### Run application

> sbt "run data output"

Where:

- `data` is the input directory.
- `output` is the output directory.