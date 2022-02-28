package fortega.application

import fortega.model.Transformation

object GoalsAgainstTransformation extends TransformationBase {
  import org.apache.spark.sql.functions.{col, sum, rank}
  import org.apache.spark.sql.expressions.Window

  def apply() = Transformation(
    "goals against", // Equipo más goleado por temporada
    df => {
      val base = df
        .where(divisionEPL)
        .select(
          toSeason(fixDate(col("date"))).as("Season"),
          col("HomeTeam"),
          col("AwayTeam"),
          col("FTHG").as("HomeGoals"),
          col("FTAG").as("AwayGoals")
        )
        .cache

      val home = base.select(
        col("Season"),
        col("HomeTeam").as("Team"),
        col("AwayGoals").as("Goals")
      )

      val away = base.select(
        col("Season"),
        col("AwayTeam").as("Team"),
        col("HomeGoals").as("Goals")
      )

      val goals = home
        .union(away)
        .groupBy("Season", "Team")
        .agg(
          sum("Goals").as("Goals")
        )

      goals
        .withColumn(
          "rank",
          rank()
            .over(
              Window.partitionBy("Season").orderBy(col("Goals").desc)
            )
        )
        .filter(col("rank") === 1)
        .drop("rank")
    }
  )

}
