#!/bin/sh

JAR=$(ls *.jar | head -n1)

echo JAR: $JAR
echo ARGS: $@
/spark/bin/spark-submit $JAR $@