package main

func scoping(x int) int {
	x = 3
	return x
}

func main() int{
	x := 2
	y := scoping(x)	
	return x
}