package main

import "fmt"

/*
Global block comment
*/
//Single line comment
func main() {
    /*
    block comment
    */
    var k int = 20
    ptr := &k
    ptr2 := &ptr
    ptr3 := &ptr2
    fmt.Println(***ptr3)
}
