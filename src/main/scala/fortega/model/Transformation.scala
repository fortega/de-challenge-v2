package fortega.model

import org.apache.spark.sql.DataFrame

final case class Transformation(name: String, process: DataFrame => DataFrame)