package fortega.application

import fortega.model.Transformation

object ShotEffectivenessTransformation extends TransformationBase {
  import org.apache.spark.sql.expressions.Window
  import org.apache.spark.sql.functions.{col, rank, sum}

  /** Team with best relation of shots ending in goal per season.
    *
    * Original: Equipo con la mejor relación de disparos al arco terminando en
    * gol por temporada.
    * @return
    *   transformation
    */
  def apply() = Transformation(
    "shot effectiveness",
    df => {
      val base = df
        .where(divisionEPL)
        .select(
          toSeason(fixDate(col("date"))).as("Season"),
          col("HomeTeam"),
          col("AwayTeam"),
          col("HST").as("HomeShots"),
          col("AST").as("AwayShots"),
          col("FTHG").as("HomeGoals"),
          col("FTAG").as("AwayGoals")
        )
        .cache

      val home = base.select(
        col("Season"),
        col("HomeTeam").as("Team"),
        col("HomeShots").as("Shots"),
        col("HomeGoals").as("Goals")
      )

      val away = base.select(
        col("Season"),
        col("AwayTeam").as("Team"),
        col("AwayShots").as("Shots"),
        col("AwayGoals").as("Goals")
      )

      val effectiveness = home
        .union(away)
        .groupBy("Season", "Team")
        .agg(
          sum("Goals").as("Goals"),
          sum("Shots").as("Shots"),
          (sum("Goals") / sum("Shots")).as("Effectiveness")
        )

      effectiveness
        .withColumn(
          "rank",
          rank()
            .over(
              Window.partitionBy("Season").orderBy(col("Effectiveness").desc)
            )
        )
        .filter(col("rank") === 1)
        .drop("rank")
    }
  )
}
