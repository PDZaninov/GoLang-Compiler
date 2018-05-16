package main

func me() int {
	return add(10,11)
}
func add() int {
	return me()
}

func main() {
	println(me())
}