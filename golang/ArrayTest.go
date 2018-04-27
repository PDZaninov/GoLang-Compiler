package main

func main() {
	s := make([]string, 3)
    s[0] = "a"
    s[1] = "b"
    s[2] = "c"

    b := []string{"d","e","f"}
	s = append(s[:1], b[1:]...)
	println(s)
}