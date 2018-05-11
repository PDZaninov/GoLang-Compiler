package main

import "fmt"

type Vertex struct{
    X int
    Y int
}

func main() {
	s := Vertex{X:1,Y:2}
    s.X = 4
    fmt.Println(s.X)
}