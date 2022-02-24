package fortega.application

import fortega.model.Transformation

object TransformationList {
    import org.apache.spark.sql.functions._
    
    private lazy val list: List[Transformation] = List(
        Transformation("count all", input => input.select(count(input.columns(0)).as("count")))
    )

    def apply: List[Transformation] = list
}
