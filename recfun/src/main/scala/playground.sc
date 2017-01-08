def pascal(c: Int, r: Int): Int = {
  if(c <= 1 || r <= 1 || r == c) 1 else pascal(c - 1, r - 1) + pascal(c, r - 1)
}

pascal(1, 1)
pascal(2, 2)
pascal(3, 2)

def balance(chars: List[Char]): Boolean = {
  def balanceIter(str: List[Char], counter: Int): Int = {
    if (str.isEmpty) counter else {
      val c = str.head
      val rest = str.tail
      if (c == '(') balanceIter(rest, counter + 1) else {
        if (c == ')') balanceIter(rest, counter - 1) else balanceIter(rest, counter)
      }
    }
  }

  balanceIter(chars, 0) == 0
}

val a = "((((just an)))) example".toList
balance(a)


def countChange(money: Int, coins: List[Int]): Int = {
  if (money == 0 ) 1 else {
    if (money < 0 || coins.length == 0) 0 else countChange(money, coins.tail) + countChange(money - coins.head, coins)
  }
}

countChange(100, List(1, 5, 10, 25, 50))
countChange(4, List(1, 2))