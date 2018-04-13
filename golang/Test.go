package main

import "fmt"

func main() {

    var k int = 20
    ptr := &k
    ptr2 := &ptr
    ptr3 := &ptr2
    fmt.Println(***ptr3)
}
