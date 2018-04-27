package main

import "fmt"

type Vertex struct {
	X int
	Y int
}

func main() {
    a := Vertex{1,2}
    Println(a.X, a.Y, a)

}