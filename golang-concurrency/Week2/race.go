package main

import "fmt"

func main() {
	a := 0

	for i := 0; i < 2; i++ {
		// setup two goroutines and run simultaneously
		go func() {
			/*
			 the a++ would be interperate a = a + 1
			 so if the value was read this in this goroutines and at the same time changed in other goroutine
			 the later assignment operation would overwrite the value set in other goroutine
			*/
			a++
		}()
	}

	fmt.Println(a)

}
