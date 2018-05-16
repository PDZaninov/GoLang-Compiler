package main

import "fmt"

var global int = 2

func add() {
    global = 7
}

func main() {
    add()
	fmt.Println(global)
}