package observatory

import scala.math._
import com.sksamuel.scrimage.{Image, Pixel}
import observatory.Visualization.interpolateColor
import observatory.Interaction.tileLocation

/**
  * 5th milestone: value-added information visualization
  */
object Visualization2 {

  /**
    * @param x X coordinate between 0 and 1
    * @param y Y coordinate between 0 and 1
    * @param d00 Top-left value
    * @param d01 Bottom-left value
    * @param d10 Top-right value
    * @param d11 Bottom-right value
    * @return A guess of the value at (x, y) based on the four known values, using bilinear interpolation
    *         See https://en.wikipedia.org/wiki/Bilinear_interpolation#Unit_Square
    */
  def bilinearInterpolation(
    x: Double,
    y: Double,
    d00: Double,
    d01: Double,
    d10: Double,
    d11: Double
  ): Double = {
    d00 * (1.0 - x) * (1.0 - y) + d10 * x * (1.0 - y) + d01 * (1.0 - x) * y + d11 * x * y
  }

  def predictTemperature(loc: Location, grid: (Int, Int) => Double): Double = {
    val x0 = ceil(loc.lat).toInt
    val x1 = floor(loc.lat).toInt
    val y0 = ceil(loc.lon).toInt
    val y1 = floor(loc.lon).toInt

    bilinearInterpolation(loc.lat, loc.lon, grid(x0, y0), grid(x0, y1), grid(x1, y0), grid(x1, y1))
  }

  /**
    * @param grid Grid to visualize
    * @param colors Color scale to use
    * @param zoom Zoom level of the tile to visualize
    * @param x X value of the tile to visualize
    * @param y Y value of the tile to visualize
    * @return The image of the tile at (x, y, zoom) showing the grid using the given color scale
    */
  def visualizeGrid(
    grid: (Int, Int) => Double,
    colors: Iterable[(Double, Color)],
    zoom: Int,
    x: Int,
    y: Int
  ): Image = {
    val c = for {
      j <- 0 until 256
      i <- 0 until 256
    } yield interpolateColor(colors, predictTemperature(tileLocation(zoom + 8, x * 256 + i, y * 256 + j), grid))

    val pixels = c.par
      .map(c => Pixel(c.red, c.green, c.blue, 127)).toArray

    Image(256, 256, pixels)
  }

}
