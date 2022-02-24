package fortega

import fortega.adapter.{ ArgsConfigReaderAdapter, JsonExtractorAdapter, SparkSessionAdapter }
import fortega.application.TransformationList
import scala.util.Failure
import scala.util.Success
import fortega.model.{ Config, Transformation }
import fortega.adapter.CsvLoaderAdapter
import fortega.adapter.TransformationAdapter
import org.apache.spark.sql.SparkSession
import scala.collection.immutable

object App {
    def main(cmdArgs: Array[String]): Unit = {
        ArgsConfigReaderAdapter(cmdArgs) match {
            case Failure(error) => errorHandler("config", error)
            case Success(config) => {
                SparkSessionAdapter(config.sparkMaster) match {
                    case Failure(error) => errorHandler("spark", error)
                    case Success(spark) => runEtl(config, spark, TransformationList.apply)
                }
            }
        }
    }

    def runEtl(config: Config, spark: SparkSession, transformations: List[Transformation]): Unit =
        JsonExtractorAdapter(spark, config.inputPath) match {
            case Failure(error) => errorHandler("extractor", error)
            case Success(data) => {
                transformations.foreach(transformation =>
                    TransformationAdapter(transformation.process, data) match {
                        case Failure(error) => errorHandler("transformation", error)
                        case Success(transformed) =>
                            CsvLoaderAdapter(config.outputPath, transformation.name, transformed) match {
                                case Failure(error) => errorHandler("load", error)
                                case Success(_) => successHandler(transformation.name)
                            }
                    }
                )
                spark.stop()
            }
        }

    def errorHandler(processName: String, error: Throwable): Unit = {
        println(s"Error($processName): $error")
        sys.exit(1)
    }

    def successHandler(trasformationName: String) : Unit = {
        println(s"Success: $trasformationName")
    }
}