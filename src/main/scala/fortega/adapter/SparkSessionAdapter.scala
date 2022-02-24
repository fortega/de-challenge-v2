package fortega.adapter

import fortega.port.SparkSessionPort
import org.apache.spark.sql.SparkSession
import scala.util.{ Failure, Success }

object SparkSessionAdapter {
    def apply(master: String): SparkSessionPort = {
        try {
            val spark = SparkSession.builder.master(master).getOrCreate
            spark.sparkContext.setLogLevel("OFF")
            Success(spark)
        }catch {
            case error: Throwable => Failure(error)
        }
    }
}
