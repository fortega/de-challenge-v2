package fortega.adapter

import fortega.port.SparkSessionPort
import org.apache.spark.sql.SparkSession
import scala.util.{Failure, Success, Try}

object SparkSessionAdapter {
  def getMasterFromEnv(
      env: Map[String, String] = sys.env
  ): Option[String] =
    if (!env.contains("SPARK_ENV_LOADED")) Option("local") else None

  def apply(master: Option[String] = getMasterFromEnv()): SparkSessionPort =
    Try {
      val spark = master match {
        case Some(value) => SparkSession.builder.master(value).getOrCreate
        case None        => SparkSession.builder.getOrCreate
      }
      spark.sparkContext.setLogLevel("OFF")
      spark
    }
}
