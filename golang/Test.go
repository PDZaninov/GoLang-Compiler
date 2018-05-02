package main

import "fmt"

func main() {
    n := map[int]string{1:"foo", 2:"bar"}
    n[2] = "no"
    fmt.Println(n)
}