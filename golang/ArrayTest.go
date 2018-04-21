package main

func main() {
	s := [6]int{2, 3, 5, 7, 11, 13}
    ptr := &s[3]
    *ptr = 4
    println(s)
}