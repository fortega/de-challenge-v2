package fortega.adapter

import fortega.port.ExtractorPort
import org.apache.spark.sql.{DataFrame, SparkSession}
import scala.util.{Success, Failure, Try}

object JsonExtractorAdapter {

  /** Read JSON files from path.
    *
    * @param spark
    *   spark session used to read
    * @param path
    *   file path to read
    * @return
    *   read result
    */
  def apply(spark: SparkSession, path: String): ExtractorPort =
    Try { spark.read.json(path) }
}
