package fortega

import scala.util.Try
import org.apache.spark.sql.DataFrame
import fortega.model.Config
import org.apache.spark.sql.SparkSession

package object port {
  type ExtractorPort = Try[DataFrame]
  type ConfigReaderPort = Try[Config]
  type SparkSessionPort = Try[SparkSession]
  type LoaderPort = Try[Unit]
  type TransformPort = Try[DataFrame]
}
