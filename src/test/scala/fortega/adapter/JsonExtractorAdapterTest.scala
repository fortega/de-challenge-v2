package fortega.adapter

import org.scalatest.flatspec.AnyFlatSpec
import scala.util.Failure
import scala.util.Success

class JsonExtractorAdapterTest extends AnyFlatSpec {
    import fortega.Utils.spark

    private val path = "data"
    "JsonExtractorAdapter" should "fail on null spark" in {
        JsonExtractorAdapter(null, path) match {
            case Failure(_) => succeed
            case Success(_) => fail
        }
    }

    it should "fail on null path" in {
        JsonExtractorAdapter(spark, null) match {
            case Failure(_) => succeed
            case Success(_) => fail
        }
    }

    it should s"succeed on read $path" in {
        JsonExtractorAdapter(spark, path) match {
            case Failure(_) => fail
            case Success(_) => succeed
        }
    }
}
