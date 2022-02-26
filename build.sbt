name := "de-challenge"
version := "0.0.0"
organization := "fortega"
scalaVersion := "2.13.8"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.2.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % "test"

coverageExcludedPackages := ".*App"