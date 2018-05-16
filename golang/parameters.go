package main

func me() int {
	return 1
}
func add(x int, y int) int {
    println("in the function")
	z := x + y
	return z
}

func main() {
	println(add(42, 13))
	println(me())
}