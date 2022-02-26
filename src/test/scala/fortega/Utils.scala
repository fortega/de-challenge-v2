package fortega

import java.io.File
import fortega.adapter.SparkSessionAdapter
import org.apache.spark.sql.SparkSession

object Utils {
    lazy val spark = SparkSessionAdapter().get

    lazy val data = {
        import spark.implicits._
        Seq(
            DataSample("E0", "18/01/10","Team1","Team2", 1, 1, "D", 10, 20),
            DataSample("E0", "18/01/10","Team2","Team1", 3, 1, "H", 60, 10)
        ).toDF
    }

    def isLocalMaster(spark: SparkSession) =  spark.sparkContext.master.startsWith("local")
    
    def deleteDir(path: String): Unit = {
        val file = new File(path)
        if (file.isDirectory) 
            file.list
                .map(path + File.separator + _)
                .foreach(deleteDir)
        file.delete
    }

    case class DataSample(div: String, date: String, homeTeam: String, awayTeam: String, fthg: Int, ftag: Int, ftr: String, hst: Int, ast: Int)
}