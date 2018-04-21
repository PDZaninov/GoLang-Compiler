package main

func main() {
	s := []int{2, 3, 5, 7, 11, 13}
    println(s)
	s = s[:0]
	println(s)

	s = s[:4]
	println(s)

	s = s[2:]
	println(s)
}