# Deployment

To run this ETL you have 3 diferent options:

- Using IntelliJ
- Using docker
- Using SBT

## Using docker (recommended)

With this option you will run a "standalone" spark server inside a container. With this option you only need docker.

### Build the container

> docker build -t challenge .

where `challenge` is the tag name of the image

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

### How to run

> sbt "run data output"

Where:

- `data` is the input directory.
- `output` is the output directory.