package main

import "fmt"

type Vertex struct{
    X int
    Y int
}

func main() {
	s := Vertex{1,2}
    fmt.Println(s.X)
}