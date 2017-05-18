package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("forall applies to each elements of the set") {
    new TestSets {
      assert(forall(s1, x => x == 1), "singleton set 1 element should equal 1")
      assert(forall(union(s1, s2), x => x <= 2), "set {1, 2} element should less or equal than 2")
    }
  }

  test("filter doesn't contain an elements of a set") {
    new TestSets {
      val empty = filter(s1, x => x == 2)
      assert(!empty(0), "empty set shouldn't include 0")
      assert(!empty(1), "empty set shouldn't include 1")
      assert(!empty(999), "empty set shouldn't include 999")

      val sameAsS2 = filter(s2, x => x == 2)
      assert(sameAsS2(2), "set s2 should include 2")
    }
  }

  test("exists contains at least an element") {
    new TestSets {
      assert(exists(s1, x => x == 1), "s1 should exist element 1 to fulfill x == 1")

      def odd(x: Int): Boolean = x % 2 == 1

      assert(exists(odd, x => x == 1), "1 is an odd")
      assert(!exists(x => List(1, 3, 4, 5, 7, 1000).contains(x), x => x == 2), "2 should not exist")
    }
  }

  test("map transform a set to another") {
    new TestSets {
      val s101 = map(s1, x => x + 100)

      assert(contains(s101, 101), "transformed set should contain new element")

      val list1 = map(x => List(1, 3, 4, 5, 7, 1000).contains(x), x => x - 1)
      assert(contains(list1, 0), "0 should be included")
      assert(contains(list1, 2), "2 should be included")
      assert(contains(list1, 3), "3 should be included")
      assert(contains(list1, 6), "6 should be included")
      assert(contains(list1, 999), "999 should be included")

      assert(!contains(list1, 1000), "1000 should not be included")
      assert(!contains(list1, 7), "7 should not be included")


      printSet(list1)
    }
  }


}
