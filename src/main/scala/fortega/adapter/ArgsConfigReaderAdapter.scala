package fortega.adapter

import scala.util.{ Failure, Success }
import fortega.model.Config
import fortega.port.ConfigReaderPort

object ArgsConfigReaderAdapter {
    lazy val argumentsMust = "arguments must be: [inputPath], [outputPath]."
    def apply(cmdArgs: Array[String]): ConfigReaderPort = {
        if(cmdArgs == null)
            Failure(new IllegalArgumentException("null arguments"))
        else if(cmdArgs.isEmpty)
            Failure(new IllegalArgumentException(argumentsMust + " got empty"))
        else if(cmdArgs.length != 2)
            Failure(new IllegalArgumentException(argumentsMust + s" got: ${cmdArgs.reduce("[" + _ + "], [" + _ + "]")}"))
        else
            Success(Config(cmdArgs(0), cmdArgs(1)))
    }
}