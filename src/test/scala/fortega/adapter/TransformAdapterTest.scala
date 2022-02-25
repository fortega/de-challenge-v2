package fortega.adapter

import org.scalatest.flatspec.AnyFlatSpec
import org.apache.spark.sql.DataFrame
import scala.util.Failure
import scala.util.Success

class TransformAdapterTest extends AnyFlatSpec {
    import fortega.Utils.{ spark, data }

    val identity: DataFrame => DataFrame = id => id

    val wrongColumn: DataFrame => DataFrame = {
        import spark.implicits._

        data => data.select($"wrongName")
    }

    "TransformAdapter" should "work on identity" in {
        TransformAdapter(data, identity) match {
            case Failure(_) => fail
            case Success(_) => succeed
        }
    }

    it should "fail on null data" in {
        TransformAdapter(null, identity) match {
            case Failure(_) => succeed
            case Success(_) => fail
        }
    }

    it should "fail on null transformation" in {
        TransformAdapter(data, null) match {
            case Failure(_) => succeed
            case Success(_) => fail
        }
    }

    it should "fail on bad transformation" in {
        TransformAdapter(data, wrongColumn) match {
            case Failure(_) => succeed
            case Success(_) => fail
        }
    }
}
