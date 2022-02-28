package fortega.application

import fortega.model.Transformation

object PositionTableTransformation extends TransformationBase {
  import org.apache.spark.sql.expressions.Window
  import org.apache.spark.sql.functions.{col, count, rank, sum, when}

  def apply() = Transformation(
    "position table", // Based on -> https://www.premierleague.com/tables
    df => {
      val base = df
        .where(divisionEPL)
        .select(
          toSeason(fixDate(col("date"))).as("Season"),
          col("HomeTeam"),
          col("AwayTeam"),
          col("FTHG").as("HomeGoals"),
          col("FTAG").as("AwayGoals"),
          when(col("FTR") === "H", oneLiteal)
            .otherwise(zeroLiteal)
            .as("HomeWin"),
          when(col("FTR") === "A", oneLiteal)
            .otherwise(zeroLiteal)
            .as("AwayWin"),
          when(col("FTR") === "D", oneLiteal)
            .otherwise(zeroLiteal)
            .as("Draw")
        )
        .cache

      val home = base.select(
        col("Season"),
        col("HomeTeam").as("Team"),
        col("HomeWin").as("Won"),
        col("Draw"),
        col("AwayWin").as("Lost"),
        col("HomeGoals").as("GF"),
        col("AwayGoals").as("GA"),
        (col("HomeGoals") - col("AwayGoals")).as("GD")
      )

      val away = base.select(
        col("Season"),
        col("AwayTeam").as("Team"),
        col("AwayWin").as("Won"),
        col("Draw"),
        col("HomeWin").as("Lost"),
        col("AwayGoals").as("GF"),
        col("HomeGoals").as("GA"),
        (col("AwayGoals") - col("HomeGoals")).as("GD")
      )

      home
        .union(away)
        .groupBy("Season", "Team")
        .agg(
          count("*").as("Played"),
          sum("Won").as("Won"),
          sum("Draw").as("Draw"),
          sum("Lost").as("Lost"),
          sum("GF").as("GF"),
          sum("GA").as("GA"),
          (sum("GF") - sum("GA")).as("GD"),
          (sum("Won") * 3 + sum("Draw")).as("Points")
        )
        .withColumn(
          "Position",
          rank()
            .over(Window.partitionBy("Season").orderBy(col("Points").desc))
        )
    }
  )
}
