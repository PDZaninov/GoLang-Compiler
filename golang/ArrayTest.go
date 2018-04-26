package main

func main() {
	s := []string{"a","b","c","d","e","f"}

	s = s[1:4]
	println(s)

	s = s[:2]
	println(s)

	s = s[1:]
	println(s)
}