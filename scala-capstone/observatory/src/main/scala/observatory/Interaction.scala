package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.Visualization._
import scala.math._

/**
  * 3rd milestone: interactive visualization
  */
object Interaction {

  /**
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return The latitude and longitude of the top-left corner of the tile, as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(zoom: Int, x: Int, y: Int): Location = {
    val n = 1 << zoom
    val lat = toDegrees(atan(sinh(Pi - y / n * 2 * Pi)))
    val lon = x * 360d / n - 180d

    Location(lat, lon)
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return A 256×256 image showing the contents of the tile defined by `x`, `y` and `zooms`
    */
  def tile(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)], zoom: Int, x: Int, y: Int): Image = {
    val positions = for (j <- y * 256 until (y + 1) * 256; i <- x * 256 until (x + 1) * 256) yield (i, j)

    val pixels = positions.par
      .map(p => tileLocation(zoom + 8, p._1, p._2))
      .map(predictTemperature(temperatures, _))
      .map(interpolateColor(colors, _))
      .map(c => Pixel(c.red, c.green, c.blue, 127)).toArray

    Image(256, 256, pixels)
  }

  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    * @param yearlyData Sequence of (year, data), where `data` is some data associated with
    *                   `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](
    yearlyData: Iterable[(Int, Data)],
    generateImage: (Int, Int, Int, Int, Data) => Unit
  ): Unit = {
    (0 to 3).foreach(zoom => {
      for (x <- pow(2, zoom) - 1; y <- pow(2, zoom) - 1) {
        yearlyData.foreach {
          case (year, data) => generateImage(zoom, x, y, year, data)
        }
      }
    })
  }
}
