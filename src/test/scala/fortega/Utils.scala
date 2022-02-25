package fortega

import java.io.File
import fortega.adapter.SparkSessionAdapter
import org.apache.spark.sql.SparkSession

object Utils {
    lazy val spark = SparkSessionAdapter().get

    lazy val data = {
        import spark.implicits._
        Seq("test1", "test2").toDF
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
}