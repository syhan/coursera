package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import org.apache.spark.sql.SparkSession

import scala.math._
/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  val spark: SparkSession = SparkSession.builder().appName("Observatory").master("local").getOrCreate()
  val RADIUS = 6371

  def greatCircleDistance(location: Location, another: Location): Double =
    RADIUS * acos(
      sin(toRadians(location.lat)) * sin(toRadians(another.lat)) +
        cos(toRadians(location.lat)) * cos(toRadians(another.lat)) *
          cos(abs(toRadians(location.lon) - toRadians(another.lon))))

  def w(distance: Double) = 1 / pow(distance, 3)
  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    val distances = temperatures.map(t => (greatCircleDistance(location, t._1), t._2))

    distances.find(_._1 < 1.0) match {
      case Some(x) => x._2
      case None =>
        val p = distances.foldLeft((0d, 0d))((acc, curr) => (acc._1 + w(curr._1) * curr._2, acc._2 + w(curr._1)))
        p._1 / p._2
    }
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    def inter(p0: (Double, Int), p1: (Double, Int)) = (p0._2 + (value - p0._1) * (p1._2 - p0._2) / (p1._1 - p0._1)).round.toInt
    val m: List[(Double, Color)] = ((value, Color(-1, -1, -1)) :: points.toList).sortWith(_._1 < _._1)

    val index = m.indexWhere(t => t._2.red == -1 && t._2.blue == -1 && t._2.blue == -1)

    index match {
      case 0 => m(1)._2
      case last if last == m.size - 1 => m(last - 1)._2
      case i => {
        val upper = m(i + 1)
        val lower = m(i - 1)

        Color(
          inter((lower._1, lower._2.red),   (upper._1, upper._2.red)),
          inter((lower._1, lower._2.green), (upper._1, upper._2.green)),
          inter((lower._1, lower._2.blue),  (upper._1, upper._2.blue)))
      }
    }
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    val locations = for (y <- 90 to -89 by -1;x <- -180 to 179) yield Location(y, x)

    val pixels = locations.par
      .map(predictTemperature(temperatures, _))
      .map(interpolateColor(colors, _))
      .map(col => Pixel(col.red, col.green, col.blue, 255)).toArray

    Image(360, 180, pixels)
  }

}

