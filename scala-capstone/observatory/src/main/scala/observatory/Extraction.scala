package observatory

import java.nio.file.Paths
import java.time.LocalDate

import org.apache.spark.rdd.RDD
import org.apache.spark.sql._

/**
  * 1st milestone: data extraction
  */
object Extraction {

  val spark: SparkSession = SparkSession.builder().appName("Observatory").master("local").getOrCreate()

  def read(path: String): RDD[String] = spark.sparkContext.textFile(Paths.get(getClass.getResource(path).toURI).toString)

  def toCelsius(fahrenheit: Double): Double = (fahrenheit - 32) * 5 / 9

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    val station = read(stationsFile)
      .map(_.split(","))
      .filter(l => l.size >= 4 && !l(2).isEmpty && !l(3).isEmpty)
      .map(l => ((l(0), l(1)), Location(l(2).toDouble, l(3).toDouble)))

    val temperature = read(temperaturesFile)
      .map(_.split(",")).filter(_.size >= 5)
      .map(l => ((l(0), l(1)), (LocalDate.of(year, l(2).toInt, l(3).toInt), if (l(4) == "9999.9") 0 else l(4).toDouble)))

    station.join(temperature).map({case (_, (loc, (date, temp))) => (date, loc, toCelsius(temp))}).persist().collect()
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    spark.sparkContext.parallelize(records.toSeq)
      .map({case (_, loc, temp) => (loc, (temp, 1))})
      .reduceByKey((p, n) => (p._1 + n._1, p._2 + n._2))
      .mapValues({case (temp, n) => (temp / n * 10).round / 10d})
      .collect()
  }

}
