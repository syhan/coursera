package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

import scala.collection.concurrent.TrieMap

import observatory.Interaction._
import observatory.Extraction._

@RunWith(classOf[JUnitRunner])
class InteractionTest extends FunSuite with Checkers {

  test("should get the tile location which is the top left coordinate correctly") {
    val loc1 = tileLocation(0, 0, 0)
    val loc2 = tileLocation(1, 0, 0)
    //val loc3 = tileLocation(1, 0, 1)
    //val loc4 = tileLocation(1, 1, 0)
    //val loc5 = tileLocation(1, 1, 1)

    assert(loc1 === loc2)
  }

  test("should get the tile correctly by using the subtile of a high level") {
    val colors: List[(Double, Color)] = List((60.0, Color(255, 255, 255)), (32.0, Color(255, 0, 0)), (12.0, Color(255, 255, 0)),
      (0, Color(0, 255, 255)), (-15.0, Color(0, 0, 255)), (-27.0, Color(255, 0, 255)),
      (-50.0, Color(33, 0, 107)), (-60.0, Color(0, 0, 0)))

    val yearly = locationYearlyAverageRecords(locateTemperatures(1986, "/stations.csv", "/1986.csv"))

    generateTiles(Iterable((1986, yearly)), (zoom: Int, x: Int, y: Int, year: Int, data: Iterable[(Location, Double)]) => {
      val img = tile(data, colors, zoom, x, y)
      val dest = new java.io.File(s"target/temperatures/$year/$zoom/$x-$y.png")
      dest.getParentFile.mkdirs()

      img.output(dest)
    })
  }

}
