package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import scala.math._
/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    val RADIUS = 6371
    def greatCircleDistance(another: Location): Double = {
      RADIUS * acos(sin(location.lat) * sin(another.lat) + cos(location.lon) * cos(another.lon) * cos(abs(location.lat - another.lat)))
    }

    def w(distance: Double) = 1 / pow(distance, 2)

    val distances = temperatures
      .map({case (loc, temp) => (greatCircleDistance(loc), temp)})

    distances.find(_._1 < 1) match {
      case Some((_, temp)) => temp
      case None =>
        val p = distances.map(t => (w(t._1), w(t._1) * t._2)).reduce((p, n) => (p._1 + n._1, p._2 + n._2))
        p._2 / p._1
    }
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    def inter(p0: (Double, Int), p1: (Double, Int)) = (p0._2 + (value - p0._1) * (p1._2 - p0._2) / (p1._1 - p0._1)).toInt
    val m: List[(Double, Color)] = ((value, Color(-1, -1, -1)) :: points.toList).sortWith(_._1 < _._1)

    m.indexWhere(_._2.red == -1) match {
      case 0 => m(1)._2
      case s if s == m.size - 1 => m(s)._2
      case i => {
        val upper = m(i + 1)
        val lower = m(i - 1)

        Color(
          inter((upper._1, upper._2.red), (lower._1, lower._2.red)),
          inter((upper._1, upper._2.green), (lower._1, lower._2.green)),
          inter((upper._1, upper._2.blue), (lower._1, lower._2.blue)))
      }
    }
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    val locations = for (y <- -90 to 89;x <- -180 to 179) yield Location(x, y)
    val pixels = locations.map(loc => interpolateColor(colors, predictTemperature(temperatures, loc))).map(c => Pixel(c.red, c.green, c.blue, 0)).toArray

    Image(360, 180, pixels)
  }

}

