name := "de-challenge"
version := "0.0.0"
organization := "fortega"
scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-sql" % "3.2.1" % "provided",
)
