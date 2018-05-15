package main

import "fmt"

func fib(x int) int {
    if x <= 1 {
        return 1
    } else {
        return fib(x - 1) + fib(x - 2)
      }
}
func main() int{
    a := fib(5)
    fmt.Println(a)
}