package main

import "fmt"

type Vertex struct {
	X int
	Y int
}

type Test struct {
    X int
    Y string
}

func main() {
	v := make([]int,10,20)
    a := Vertex{1,2}
	Println(v)
    Println(a)
    
    v[9] = 10
    a.X = 45
    Println(v)
    Println(a)
    b := Test{X: 10, Y: "Hello!"}
    Println(b)
}