package fortega

import fortega.adapter.{
  ArgsConfigReaderAdapter,
  JsonExtractorAdapter,
  SparkSessionAdapter
}
import scala.util.Failure
import scala.util.Success
import fortega.model.{Config, Transformation}
import fortega.adapter.CsvLoaderAdapter
import fortega.adapter.TransformAdapter
import org.apache.spark.sql.SparkSession
import scala.collection.immutable
import fortega.application.PositionTableTransformation
import fortega.application.ShotEffectivenessTransformation
import fortega.application.GoalsAgainstTransformation
import org.slf4j.LoggerFactory

object App {
  private val logger = LoggerFactory.getLogger("fortega.App")

  def main(cmdArgs: Array[String]): Unit =
    ArgsConfigReaderAdapter(cmdArgs) match {
      case Failure(error) => errorHandler("config", error)
      case Success(config) => {
        SparkSessionAdapter() match {
          case Failure(error) => errorHandler("spark", error)
          case Success(spark) => runEtl(config, spark)
        }
      }
    }

  /** Run the ETL.
    *
    * @param config
    *   configuration of the ETL
    * @param spark
    *   spark session to submit the ETL
    * @param transformations
    *   list of transformations to do
    */
  def runEtl(
      config: Config,
      spark: SparkSession,
      transformations: List[Transformation] = List(
        PositionTableTransformation(),
        ShotEffectivenessTransformation(),
        GoalsAgainstTransformation()
      )
  ): Unit =
    JsonExtractorAdapter(spark, config.inputPath) match {
      case Failure(error) => errorHandler("extractor", error)
      case Success(data) => {
        transformations.foreach(transformation =>
          TransformAdapter(data, transformation.process) match {
            case Failure(error) =>
              errorHandler(
                s"transformation ${transformation.name}",
                error,
                exit = false
              )
            case Success(transformed) =>
              CsvLoaderAdapter(
                config.outputPath,
                transformation.name,
                transformed
              ) match {
                case Failure(error) =>
                  errorHandler(
                    s"load ${transformation.name}",
                    error,
                    exit = false
                  )
                case Success(_) => successHandler(transformation.name)
              }
          }
        )
        spark.stop()
      }
    }

  /** Handle an error in the process
    *
    * @param processName
    *   process name
    * @param error
    *   error found
    * @param exit
    *   if true, halt the application
    */
  def errorHandler(
      processName: String,
      error: Throwable,
      exit: Boolean = true
  ): Unit = {
    logger.error(s"Error($processName): $error")
    if (exit) sys.exit(1)
  }

  /** Handle a success load..
    *
    * @param trasformationName
    *   transformation name
    */
  def successHandler(trasformationName: String): Unit = {
    logger.info(s"Success: $trasformationName")
  }
}
