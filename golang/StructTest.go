package main

type Vertex struct {
    X int
    Y int
}

func me() int {

    v := Vertex{X:1, Y:2}
    v.X = 4
    return v.X 
}

func main() {
	println(me())
}