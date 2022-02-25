package fortega.adapter

import org.scalatest.flatspec.AnyFlatSpec
import org.apache.spark.sql.SparkSession
import scala.util.Failure
import scala.util.Success

class CsvLoaderAdapterTest extends AnyFlatSpec {
    lazy val spark = SparkSession.builder.master("local").getOrCreate
    lazy val outputPath = "tmp"
    lazy val fileName = "fileName"

    "CsvLoaderAdapter" should "fail on null dataframe" in {
        CsvLoaderAdapter(outputPath, fileName, null) match {
            case Failure(_) => succeed
            case Success(_) => fail
        }
    }

    it should "succed to save dataframe" in {
        import spark.implicits._
        
        val data = Seq("test").toDF
        CsvLoaderAdapterTest.deleteDir(outputPath)
        CsvLoaderAdapter(outputPath, fileName, data) match {
            case Failure(_) => fail
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

object CsvLoaderAdapterTest {
    import java.nio.file.{ Files, Paths }

    def deleteDir(path: String): Unit = {
        Files.deleteIfExists(Paths.get(path))
    }
}
