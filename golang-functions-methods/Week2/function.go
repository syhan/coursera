package main

import (
	"fmt"
	"strconv"
)

/*
GenDisplaceFn generates a function to compute displacement after certain time period
*/
func GenDisplaceFn(a, v0, s0 float64) func(float64) float64 {
	return func(t float64) float64 {
		return a*t*t/2 + v0*t + s0
	}
}

func main() {
	var a string
	fmt.Print("Acceleration? ")
	fmt.Scan(&a)
	acc, _ := strconv.ParseFloat(a, 64)

	var v0 string
	fmt.Print("Initial velocity? ")
	fmt.Scan(&v0)
	vel, _ := strconv.ParseFloat(v0, 64)

	var s0 string
	fmt.Print("Initial displacement? ")
	fmt.Scan(&s0)
	dis, _ := strconv.ParseFloat(s0, 64)

	var t string
	fmt.Print("Time? ")
	fmt.Scan(&t)
	time, _ := strconv.ParseFloat(t, 64)

	fn := GenDisplaceFn(acc, vel, dis)
	d := fn(time)

	fmt.Printf("Displacement would be %f after time %f with given acceleration %f, initial velocity %f and initial displacement %f\n", d, time, acc, vel, dis)
}
