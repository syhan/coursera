package main

import (
	"fmt"
	"sort"
	"strconv"
)

func main() {
	var sli = make([]int, 0, 3)
	var i string

	for {
		fmt.Scan(&i)

		if i == "X" {
			break
		}

		num, _ := strconv.Atoi(i)
		sli = append(sli, num)

		sort.Ints(sli)
		fmt.Println(sli)

	}

}
