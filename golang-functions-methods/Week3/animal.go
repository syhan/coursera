package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

type animal struct {
	food       string
	locomotion string
	noise      string
}

func (a *animal) Eat() {
	fmt.Println(a.food)
}

func (a *animal) Move() {
	fmt.Println(a.locomotion)
}

func (a *animal) Speak() {
	fmt.Println(a.noise)
}

func main() {
	reader := bufio.NewReader(os.Stdin)

	for {
		fmt.Print("> ")
		input, _ := reader.ReadString('\n')
		s := strings.Split(strings.TrimSpace(input), " ")

		a := new(animal)
		switch s[0] {
		case "cow":
			a.food = "grass"
			a.locomotion = "walk"
			a.noise = "moo"
		case "bird":
			a.food = "worms"
			a.locomotion = "fly"
			a.noise = "poop"
		case "snake":
			a.food = "mice"
			a.locomotion = "slither"
			a.noise = "hsss"
		}

		switch s[1] {
		case "eat":
			a.Eat()
		case "speak":
			a.Speak()
		case "move":
			a.Move()
		}
	}
}
