package observatory


import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers
import observatory.Visualization._
import observatory.Extraction._

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers {

  test("should interpolate color correctly") {
    val c = interpolateColor(List((0.0, Color(255, 0, 0)), (2.147483647E9, Color(0, 0, 255))), 5.3687091175E8)

    assert(c.red === 191)
    assert(c.green === 0)
    assert(c.blue === 64)
  }

  test("exceeding the greatest value of a color scale should return the color associated with the greatest value") {
    val c = interpolateColor(List((-1.0,Color(255, 0, 0)), (15.39640384017234, Color(0,0,255))), 25.39640384017234)

    assert(c.red === 0)
    assert(c.green === 0)
    assert(c.blue === 255)
  }

  test("should predicate temperature correctly") {
    val t1 = predictTemperature(List((Location(0, 0), 10), (Location(-45, 90), 40)), Location(0, 0.001))
    val t2 = predictTemperature(List((Location(0, 0), 10), (Location(-45, 90), 40)), Location(-45, 90.001))
    val t3 = predictTemperature(List((Location(0, 0), 10), (Location(-45, 90), 40)), Location(-45, 90))

    println(t1)
    println(t2)

    assert(t3 === 40)
  }

  test("should output a image by given year correctly") {
    val colors: List[(Double, Color)] = List((60.0, Color(255, 255, 255)), (32.0, Color(255, 0, 0)), (12.0, Color(255, 255, 0)),
                                             (0, Color(0, 255, 255)), (-15.0, Color(0, 0, 255)), (-27.0, Color(255, 0, 255)),
                                             (-50.0, Color(33, 0, 107)), (-60.0, Color(0, 0, 0)))
    val locations = locationYearlyAverageRecords(locateTemperatures(1986, "/stations.csv", "/1986.csv"))
    //val locations = List((Location(0, 0), 10d), (Location(-45, 90), 40d))
    val img = visualize(locations, colors)
    img.output(new java.io.File("output.png"))
  }
}
