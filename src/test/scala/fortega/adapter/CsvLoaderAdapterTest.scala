package fortega.adapter

import org.scalatest.flatspec.AnyFlatSpec
import org.apache.spark.sql.SparkSession
import scala.util.Failure
import scala.util.Success

class CsvLoaderAdapterTest extends AnyFlatSpec {
    import fortega.Utils.{ spark, deleteDir, data }

    lazy val outputPath = "tmp"
    lazy val fileName = "fileName"

    "CsvLoaderAdapter" should "fail on null dataframe" in {
        CsvLoaderAdapter(outputPath, fileName, null) match {
            case Failure(_) => succeed
            case Success(_) => fail
        }
    }

    it should "succeed to save dataframe" in {
        deleteDir(outputPath)

        CsvLoaderAdapter(outputPath, fileName, data) match {
            case Failure(error) => {
                error.printStackTrace()
                fail
            }
            case Success(_) => succeed
        }
    }  

    it should "fail if output exists" in {
        CsvLoaderAdapter(outputPath, fileName, spark.emptyDataFrame) match {
            case Failure(_) => succeed
            case Success(_) => fail
        }
    }
}