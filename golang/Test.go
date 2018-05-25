package main

import ( 
    "fmt"
    "time"
)

func forloopfib(x int) {
    a := 0
    b := 1
    c := 0
    for i:=1; i < x; i++ {
        c = a + b
        a = b
        b = c
    }
}

func fib(a int) int{
    if a == 0 {
        return 0
    }
    if a == 1 {
        return 1
    }
    return fib(a-1) + fib(a-2)
}

func main() {
    //UnixNano returns an int64 which cannot be implicitly converted to an int
    x := time.Now().UnixNano()
    for i := 0; i < 200000; i++{
        if(i == 10000){
            x = time.Now().UnixNano()
        }
        y := 13
        a := 0
        b := 1
        c := 0
        for j:=1; j < y; j++ {
            c = a + b
            a = b
            b = c
        }
    }
    
    b := time.Now().UnixNano()
    fmt.Println(b-x, "nanoseconds")
    fmt.Println((b-x)/1000000, "milliseconds")
}