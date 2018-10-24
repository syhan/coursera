package main

import (
	"fmt"
	"strconv"
)

/*
BubbleSort use bubble sort to sort given slice
*/
func BubbleSort(sli []int) {
	length := len(sli)
	for i := 0; i < length-1; i++ {
		changed := false
		for j := 0; j < length-i-1; j++ {
			if sli[j] > sli[j+1] {
				Swap(sli, j)
				changed = true
			}
		}

		if !changed {
			break
		}
	}
}

/*
Swap to exchange two values at position i of given slice
*/
func Swap(sli []int, i int) {
	tmp := sli[i]
	sli[i] = sli[i+1]
	sli[i+1] = tmp
}

func main() {
	var sli = make([]int, 0, 10)
	var s string

	i := 0
	for {
		if i == 10 {
			break
		}

		fmt.Scan(&s)

		num, _ := strconv.Atoi(s)
		sli = append(sli, num)

		BubbleSort(sli)

		fmt.Println(sli)

		i++
	}
}
