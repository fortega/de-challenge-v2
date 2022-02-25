package fortega.adapter

import fortega.port.SparkSessionPort
import org.apache.spark.sql.SparkSession
import scala.util.{ Failure, Success, Try }

object SparkSessionAdapter {
    private def isService = sys.env.contains("SPARK_ENV_LOADED")
    private val localMaster = "local"
    def apply(): SparkSessionPort =  Try {
        val builder = SparkSession.builder
        val spark = if(isService) builder.getOrCreate else builder.master(localMaster).getOrCreate
        spark.sparkContext.setLogLevel("OFF")
        spark
    }
}
