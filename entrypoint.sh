#!/bin/sh

JAR=$(ls *.jar | head -n1)

echo JAR: $JAR
/spark/bin/spark-submit $JAR $@