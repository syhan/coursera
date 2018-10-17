package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func main() {
	reader := bufio.NewReader(os.Stdin)
	i, _ := reader.ReadString('\n')

	i = strings.TrimSpace(strings.ToLower(i))

	if strings.HasPrefix(i, "i") && strings.HasSuffix(i, "n") && strings.Contains(i, "a") {
		fmt.Println("Found!")
	} else {
		fmt.Println("Not Found!")
	}

}
