package fortega.model

import org.apache.spark.sql.DataFrame

/** Transformation process
  *
  * @param name
  *   name of the process (to report errors)
  * @param process
  *   transformation process
  */
final case class Transformation(name: String, process: DataFrame => DataFrame)
