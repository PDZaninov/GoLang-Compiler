package main

func me() int {
	return add(10,11)
}
func add(x int, y int) int {
	return x + y
}

func main() {
	println(me())
}