package main

import ( 
    "fmt"
    "time"
)

func main() {
    a := time.Now()
    x := 0
    for i := 0; i < 1000; i++{
        x *= i
    }
    fmt.Println(time.Now() - a)
}