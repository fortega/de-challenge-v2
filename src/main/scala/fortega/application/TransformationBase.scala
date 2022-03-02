package fortega.application

import scala.util.matching.Regex
import org.apache.spark.sql.Column

object TransformationBase {
  import org.apache.spark.sql.functions.{
    col,
    lit,
    month,
    to_date,
    udf,
    when,
    year
  }

  lazy val oneLiteal = lit(1)

  lazy val zeroLiteal = lit(0)

  lazy val divisionEPL = col("Div") === "E0"

  private lazy val dataFormatter: List[(Regex, String => String)] = List(
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

  def toSeason(column: Column): Column = {
    val date = to_date(column, "dd/MM/yyyy")
    val yearValue = year(date)
    when(month(date) < 6, yearValue - 1).otherwise(yearValue)
  }
}
