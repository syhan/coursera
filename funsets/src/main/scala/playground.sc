import funsets.FunSets.map

type Set = Int => Boolean
def contains(s: Set, elem: Int): Boolean = s(elem)

def singletonSet(elem: Int): Set = x => x == elem

val s1 = singletonSet(1)
contains(s1, 1)

def filter(s: Set, p: Int => Boolean): Set = elem => contains(s, elem) && p(elem)

val bound = 5

def forall(s: Set, p: Int => Boolean): Boolean = {
  def iter(a: Int): Boolean = {
    if (a > bound) true
    else if (contains(s, a)) p(a) && iter(a + 1)
    else iter(a + 1)
  }
  iter(-bound)
}

def exists(s: Set, p: Int => Boolean): Boolean = !forall(s, x => !p(x))



exists(x => List(1, 3, 4, 5, 7, 1000).contains(x), x => x == 1)

def map(s: Set, f: Int => Int): Set = x => forall(s, y => f(y) > 0)

