package main

import "fmt"

func main() {
    var a, n int = 1, 5
    for x := 1; x <= n; x++{
        a *= x
        }
    fmt.Println(a)
}