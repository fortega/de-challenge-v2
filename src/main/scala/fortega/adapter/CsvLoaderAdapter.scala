package fortega.adapter

import fortega.port.LoaderPort
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SaveMode
import scala.util.Failure
import scala.util.Success

object CsvLoaderAdapter {
    import java.io.File.separator

    private val extension = ".csv"
    private val cleanChars = List(" ")
    private val cleanReplace = "-"

    def apply(outputPath: String, fileName: String, data: DataFrame): LoaderPort = {
        val cleanFilename = cleanChars.foldLeft(fileName)((name, cleanChar) => name.replace(cleanChar, cleanReplace))
        val filePath = outputPath + separator + cleanFilename + extension
        try {
            Success(data.write.option("header", true).mode(SaveMode.ErrorIfExists).csv(filePath))
        }catch{
            case error: Throwable => Failure(error)
        }
    }
}