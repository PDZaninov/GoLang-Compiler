package main

var global int = 2

func set() {
    global = 7
    println(global)
}

func main() {
    set()
    println(global)
}