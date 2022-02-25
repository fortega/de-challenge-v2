package fortega.adapter

import fortega.port.TransformationPort
import org.apache.spark.sql.DataFrame
import scala.util.{ Failure, Success, Try }

object TransformationAdapter {
    def apply(process: DataFrame => DataFrame, data: DataFrame): TransformationPort =
        Try { data.transform(process) }
}