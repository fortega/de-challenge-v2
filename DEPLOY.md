# How to build

> docker build -t challenge . 

# How to run

docker run -it --rm -v $PWD/data:/app/input -v $PWD/output:/app/output challenge local input output