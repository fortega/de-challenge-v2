package fortega.adapter

import fortega.port.TransformPort
import org.apache.spark.sql.DataFrame
import scala.util.{ Failure, Success, Try }

object TransformAdapter {
    def apply(data: DataFrame, process: DataFrame => DataFrame): TransformPort =
        Try { data.transform(process) }
}