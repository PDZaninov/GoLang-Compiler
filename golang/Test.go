package main

import "fmt"

type Vertex struct {
	X int
	Y int
}

func main() {
	v := Vertex{X:1, Y:2}
	v.X = 4
	Println(v.X)
}