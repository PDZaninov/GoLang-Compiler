package main

import (
	"fmt"
)

func add(x int, y int) int {
    fmt.Println("in the function")
	return x + y
}

func main() {
	fmt.Println(add(42, 13))
}