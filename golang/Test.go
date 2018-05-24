package main

import "fmt"

func main(){

    array := [5]int{1,2,3,4,5}
    slice := array[:4]
    s := append(slice,6,7,8,9)
    x := append(s[2:5],slice[3:]...)
    y := append(x,x[1:4]...)
    fmt.Println("array = ", array)
    fmt.Println("slice = ", slice)
    fmt.Println("s = ", s)
    fmt.Println("x = ", x)
    fmt.Println("y = ", y)

}