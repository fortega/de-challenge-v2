package fortega

import org.apache.spark.sql.SparkSession

object Utils {
    lazy val spark = SparkSession.builder.master("local").getOrCreate
}