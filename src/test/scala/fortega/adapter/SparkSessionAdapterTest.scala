package fortega.adapter

import org.scalatest.flatspec.AnyFlatSpec
import scala.util.Failure
import scala.util.Success
import fortega.Utils
import org.apache.spark.sql.SparkSession

class SparkSessionAdapterTest extends AnyFlatSpec {
  import fortega.Utils.isLocalMaster

  "SparkSessionAdapter" should "get local master by default" in {
      SparkSessionAdapter() match {
          case Failure(_) => fail
          case Success(spark) => isLocalMaster(spark)
      }
    }

    it should "return existing session even with None master" in {
      SparkSessionAdapter(None) match {
        case Failure(_) => fail
        case Success(spark) => isLocalMaster(spark)
      }
    }

    "SparkSessionAdapter.getMasterFromEnv" should "return None on server" in {
      SparkSessionAdapter.getMasterFromEnv(Map("SPARK_ENV_LOADED" -> "1")) match {
        case Some(_) => fail
        case None => succeed
      }
    }

    it should "return Option(local) on local" in {
      SparkSessionAdapter.getMasterFromEnv(Map()) match {
        case Some(_) => succeed
        case None => fail
      }
    }
}