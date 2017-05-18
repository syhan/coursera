val l1 = List('a', 'b', 'c', 'b')
val curr = 'f'
val c = l1.find(elem => elem == curr).getOrElse('e')
val l2 = 'e' :: l1

val l3 = l1 ::: l2

val arr1 = Array[Char]('a', 'b', 'c')
arr1(2) = 'd'

arr1.indexOf('d')
arr1.find(e => e == 'd').getOrElse('g')

arr1.indexWhere(e => e == 'd')
arr1.+:('e')
arr1.length

def times(chars: List[Char]): List[(Char, Int)] = {
  def timesIter(xs: List[Char], acc: Array[(Char, Int)]): Array[(Char, Int)] = {
    if (xs.isEmpty) acc else {
      val curr = xs.head
      val offset = acc.indexWhere(elem => elem._1 == curr)

      if (offset == -1) timesIter(xs.tail, acc.+:((curr, 1))) else {
        acc(offset) = (curr, acc(offset)._2 + 1)
        timesIter(xs.tail, acc)
      }
    }
  }

  timesIter(chars, Array[(Char, Int)]()).toList
}

times(l1)