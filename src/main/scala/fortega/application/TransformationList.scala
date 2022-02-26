package fortega.application

import fortega.model.Transformation
import org.apache.spark.sql.Column;
import scala.util.matching.Regex

object TransformationList {
  import org.apache.spark.sql.functions._
  import org.apache.spark.sql.expressions.Window

  lazy val oneLiteal = lit(1)
  lazy val zeroLiteal = lit(0)
  lazy val divisionEPL = col("Div") === "E0"
  lazy val dataFormatter: List[(Regex, String => String)] = List(
    "[0-9]{4}-[0-9]{2}-[0-9]{2}".r -> (text => {
      val s = text.split("-")
      s"${s(2)}/${s(1)}/${s(0)}"
    }),
    "[0-9]{2}/[0-9]{2}/[0-9]{2}".r -> (text => {
      val s = text.split("/")
      s"${s(0)}/${s(1)}/20${s(2)}"
    }),
    "[0-9]{2}/[0-9]{2}/[0-9]{4}".r -> (text => text)
  )
  lazy val fixDate = udf { text: String =>
    dataFormatter.flatMap { case (regex, f) =>
      if (regex.matches(text)) Option(f(text)) else None
    }.head
  }

  private def toSeason(column: Column): Column = {
    val date = to_date(column, "dd/MM/yyyy")
    val yearValue = year(date)
    when(month(date) < 6, yearValue - 1).otherwise(yearValue)
  }

  def apply(): List[Transformation] = List(
    Transformation(
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
            row_number()
              .over(Window.partitionBy("Season").orderBy(col("Points").desc))
          )
      }
    ),
    Transformation(
      "best goal shot proportion",
      df => df
    ),
    Transformation(
      "most against goals",
      df => df
    )
  )
}
