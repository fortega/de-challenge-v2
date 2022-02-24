package fortega.adapter

import fortega.port.ExtractorPort
import org.apache.spark.sql.{ DataFrame, SparkSession}
import scala.util.{Success, Failure}

object JsonExtractorAdapter {  
    def apply(spark: SparkSession, path: String): ExtractorPort = {
        try {
            Success(spark.read.json(path))
        }catch{
            case e: Throwable => Failure(e)
        }
    }
}
