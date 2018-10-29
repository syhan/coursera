package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

// Animal defines the behaviors
type Animal interface {
	Eat()
	Move()
	Speak()
}

// Cow is one specified animal
type Cow struct {
}

// Eat is how a cow eat
func (a Cow) Eat() {
	fmt.Println("grass")
}

// Move is how a cow move
func (a Cow) Move() {
	fmt.Println("walk")
}

// Speak is how a cow speak
func (a Cow) Speak() {
	fmt.Println("moo")
}

// Bird is one specified animal
type Bird struct {
}

// Eat is how a bird eat
func (a Bird) Eat() {
	fmt.Println("worms")
}

// Move is how a bird move
func (a Bird) Move() {
	fmt.Println("fly")
}

// Speak is how a bird speak
func (a Bird) Speak() {
	fmt.Println("peep")
}

// Snake is one specified animal
type Snake struct {
}

// Eat is how a snake eat
func (a Snake) Eat() {
	fmt.Println("worms")
}

// Move is how a snake move
func (a Snake) Move() {
	fmt.Println("slither")
}

// Speak is how a snake speak
func (a Snake) Speak() {
	fmt.Println("hsss")
}

func main() {
	reader := bufio.NewReader(os.Stdin)
	animals := make(map[string]Animal)

	for {
		fmt.Print("> ")
		input, _ := reader.ReadString('\n')
		s := strings.Split(strings.TrimSpace(input), " ")

		switch s[0] {
		case "newanimal":
			switch s[2] {
			case "cow":
				animals[s[1]] = new(Cow)
			case "bird":
				animals[s[1]] = new(Bird)
			case "snake":
				animals[s[1]] = new(Snake)
			}

			fmt.Println("Created it!")
		case "query":
			a, ok := animals[s[1]]

			if ok {
				switch s[2] {
				case "eat":
					a.Eat()
				case "move":
					a.Move()
				case "speak":
					a.Speak()
				}
			} else {
				fmt.Println("Not found!")
			}
		}

	}
}
