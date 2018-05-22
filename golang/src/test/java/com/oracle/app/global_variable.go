package main

var global int = 2

func set() {
    global = 7
}

func main() int{
    set()
    return global
}