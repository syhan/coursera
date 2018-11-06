package main

import (
	"fmt"
	"sync"
	"time"
)

func main() {
	a := 0
	var w sync.WaitGroup

	for i := 0; i < 2; i++ {
		/*
		 setup two goroutines and run simultaneously
		*/
		w.Add(1)
		go func() {
			for j := 0; j < 1000; j++ {
				/*
					the a++ would be interperate a = a + 1
					since interleaving is totally random, the read and set opeartion cannot be predicted the sequence
					in different goroutines.
					so if the value was read this in this goroutines and at the same time changed in other goroutine
					the later assignment operation would overwrite the value set in other goroutine
				*/
				a++
				time.Sleep(1 * time.Millisecond)
			}

			w.Done()
		}()
	}

	// wait for the two goroutines finish
	w.Wait()
	/*
	 the result could be random number which less than 2000
	 if there's no race between the two goroutines the value should be precisely 2000
	 since the two goroutines separately increase the a 1000 times
	 but since there's some interleaving to make the value was override by another goroutine
	*/
	fmt.Println("finally a becomes", a)

}
