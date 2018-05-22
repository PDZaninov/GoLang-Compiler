package main

/*
* Adds two variables with the result of vals(), vals() declared later in file
*/
func add(x int, y int) int {
    var a,b int = vals(4)
    x = x + a
    y = y + b
    return x + y
}

// Multiple returns
func vals(a int) (int,int) {
    var c = something(a)
    return c,7
}


func something(a int) int {
    a = 4
    return a
}


func main() int {
    x := add(2, 3)
    return x
}