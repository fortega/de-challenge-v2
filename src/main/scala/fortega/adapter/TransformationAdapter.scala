package fortega.adapter

import org.apache.spark.sql.DataFrame
import scala.util.{ Failure, Success }

object TransformationAdapter {
    def apply(process: DataFrame => DataFrame, data: DataFrame) = {
        try {
            Success(data.transform(process))
        }catch {
            case error: Throwable => Failure(error)
        }
    }
}