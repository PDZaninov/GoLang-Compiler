package main

import "fmt"

func main() {
	var x = [5]int{1,2,3,4,5}
	var y = [5]int{1,2,3,4,2}
	
	fmt.Println(x == y)
}