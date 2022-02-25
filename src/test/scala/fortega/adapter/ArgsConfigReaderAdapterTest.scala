package fortega.adapter

import org.scalatest.flatspec.AnyFlatSpec
import scala.util.Failure
import scala.util.Success

class ArgsConfigReaderAdapterTest extends AnyFlatSpec {
    val arguments = Array("firstArg", "secondArg", "thirdArg")

    "ArgsConfigReaderAdapter" should "fail if cmdArgs is null" in {
        ArgsConfigReaderAdapter(null) match {
            case Failure(exception) => assert(exception.getClass == classOf[IllegalArgumentException])
            case Success(value) => fail
        }
    }

    it should "fail if cmdArgs have 1 args" in {
        ArgsConfigReaderAdapter(arguments.take(1)) match {
            case Failure(exception) => assert(exception.getClass == classOf[IllegalArgumentException])
            case Success(value) => fail
        }
    }

    it should "succed if cmdArgs have 2 args" in {
        ArgsConfigReaderAdapter(arguments.take(2)) match {
            case Failure(exception) => fail
            case Success(value) => succeed
        }
    }

    it should "fail if cmdArgs have 3 args" in {
        ArgsConfigReaderAdapter(arguments.take(3)) match {
            case Failure(exception) => assert(exception.getClass == classOf[IllegalArgumentException])
            case Success(value) => fail
        }
    }
  
}
