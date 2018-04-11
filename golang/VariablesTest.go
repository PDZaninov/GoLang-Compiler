package main

func main() {
	a := 5
	y := &a
	x := &y
	println(**x)
}