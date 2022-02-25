package fortega.adapter

import fortega.port.ExtractorPort
import org.apache.spark.sql.{ DataFrame, SparkSession}
import scala.util.{ Success, Failure, Try }

object JsonExtractorAdapter {  
    def apply(spark: SparkSession, path: String): ExtractorPort =
        Try { spark.read.json(path) }
}
