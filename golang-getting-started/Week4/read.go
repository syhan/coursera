package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

/*
name has two fields, fname for the first name, and lname for the last name.
Each field will be a string of size 20 (characters)
*/
type name struct {
	fname []byte
	lname []byte
}

func main() {
	fmt.Print("File? ")

	var filename string
	fmt.Scanln(&filename)

	file, _ := os.Open(filename)
	defer file.Close()

	names := make([]name, 0)

	s := bufio.NewScanner(file)
	for s.Scan() {
		s := strings.Split(s.Text(), " ")
		n := name{fname: []byte(s[0]), lname: []byte(s[1])}

		names = append(names, n)
	}

	for _, n := range names {
		fmt.Printf("first name: %s, last name: %s\n", n.fname, n.lname)
	}

}
