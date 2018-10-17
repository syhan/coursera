package main

import (
	"fmt"
	"math"
)

func main() {
	var i float64

	fmt.Scan(&i)

	fmt.Print(int64(math.Floor(i)))
}
