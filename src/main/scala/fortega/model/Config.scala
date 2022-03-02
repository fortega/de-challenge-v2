package fortega.model

/** Configuration to run the ETL
  *
  * @param inputPath
  *   input path to read files
  * @param outputPath
  *   output path to save files
  */
final case class Config(inputPath: String, outputPath: String)
