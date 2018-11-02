package main

import (
	"fmt"
	"sync"
)

// Chopstick would be later put into a pool
type Chopstick struct {
}

// Philosopher is labelled with num
type Philosopher struct {
	num int
}

var pool = sync.Pool{
	New: func() interface{} {
		return new(Chopstick)
	},
}

var w sync.WaitGroup

func (p Philosopher) eat(host chan int) {
	defer w.Done()

	// get permission from the host
	<-host

	fmt.Printf("starting to eat %d\n", p.num)
	// pick up chopsticks in any order
	left := pool.Get()
	right := pool.Get()
	// then return the chopsticks
	pool.Put(left)
	pool.Put(right)
	fmt.Printf("finishing eating %d\n", p.num)

	host <- 1
}

func main() {
	// host allows no more than 2 philosophers to eat concurrently
	host := make(chan int, 2)

	// initialize chopsticks pool
	for i := 0; i < 5; i++ {
		pool.Put(new(Chopstick))
	}

	// initialize philosophers
	philosophers := make([]*Philosopher, 5)
	for i := 0; i < 5; i++ {
		// each philosopher is numbered, 1 through 5
		philosophers[i] = &Philosopher{i + 1}
	}

	// let them start
	for i := 0; i < 5; i++ {
		for j := 0; j < 3; j++ { // only eat 3 times
			w.Add(1)
			go philosophers[i].eat(host)
		}
	}

	host <- 1
	host <- 1

	w.Wait()
}
