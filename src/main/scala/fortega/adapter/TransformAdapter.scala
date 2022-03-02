package fortega.adapter

import fortega.port.TransformPort
import org.apache.spark.sql.DataFrame
import scala.util.{Failure, Success, Try}

object TransformAdapter {

  /** Transform data using the process.
    *
    * @param data
    *   source data
    * @param process
    *   the transformation process
    * @return
    *   transformation process result
    */
  def apply(data: DataFrame, process: DataFrame => DataFrame): TransformPort =
    Try { data.transform(process) }
}
