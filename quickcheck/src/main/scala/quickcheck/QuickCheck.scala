package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] = for {
    i <- arbitrary[Int]
    n <- oneOf(const(empty), genHeap)
  } yield insert(i, n)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  def loopHeap(h: H): List[A] = {
    def iter(h: H, acc: List[A]): List[A] = {
      if (isEmpty(h)) acc else {
        iter(deleteMin(h), findMin(h) :: acc)
      }
    }

    iter(h, List())
  }

  def every(h: H, elems: List[A]): Boolean = elems.toSet.subsetOf(loopHeap(h).toSet)

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("meld1") = forAll { (h1: H, h2: H) =>
    val elems = loopHeap(h1) ++ loopHeap(h2)
    val h = meld(h1, h2)

    every(h, elems)
  }

}
