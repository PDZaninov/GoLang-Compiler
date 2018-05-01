package main

func scoping(x int) int {
	x = 3
	return x
}

func main() {
	x := 2
	y := scoping(x)	
	return x
}