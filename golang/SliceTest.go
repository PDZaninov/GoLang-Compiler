package main

func main() {
	s := make([]string, 3)
    s[0] = "a"
    s[1] = "b"
    s[2] = "c"

    s = append(s, "d")
    l := s[2:5]

    a := make([]int, 2, 5)
    a[0] = 0
    a[1] = 1

    println(s)
    println(l)
    println(a)
}