package main

import "fmt"

type Vertex struct {
	X int
	Y int
}

func main() {
	v := Vertex{Y:2}
	//v.X = 4
    fmt.Println(v.Y)
}
