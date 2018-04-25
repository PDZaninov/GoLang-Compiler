package main

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
   
    v[9] = 10
    a.X = 45
    println(v[9])
    println(a.X)
    b := Test{X: 10, Y: "Hello!"}
    println(b.Y)
}