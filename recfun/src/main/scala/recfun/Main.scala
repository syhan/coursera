package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    val a = "((((just an) example".toList
    balance(a)
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if(c <= 0|| r <= 0 || r == c) 1 else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  
  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def abs(value: Int): Int = {
      if (value < 0) -value else value
    }
    def balanceIter(str: List[Char], counter: Int): Int = {
      if (str.isEmpty) counter else {
        val c = str.head
        val rest = str.tail
        if (c == '(') balanceIter(rest, counter + 1) else {
          if (c == ')') balanceIter(rest, abs(counter - 1)) else balanceIter(rest, counter)
        }
      }
    }

    balanceIter(chars, 0) == 0
  }

  
  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (money == 0 ) 1 else {
      if (money < 0 || coins.length == 0) 0 else countChange(money, coins.tail) + countChange(money - coins.head, coins)
    }
  }

}
