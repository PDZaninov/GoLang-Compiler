package main

func main() {

    var k int = 20
    var a string = "Hello, World!"

    var array [10]int

	for j := 5; j <= 9; j++ {
        println(j)
    }

    for {
        println("loop")
        break
    }

    for n := 0; n <= 5; n++ {
        if n%2 == 0 {
            n++
            continue
        }
        println(n)
    }

    i := 1
    for i <= 3 {
        println(i)
        i = i + 1
    }

    switch k {
    case 1:
        println("one")
    case 20:
        println("twenty")
    }

    for f := 0; f < len(array); f++ {
        array[f] = f;
    }

    for z := len(array) - 1; z >= 0; z-- {
        println(array[z])
    }



    println(a + i)
    println(k + i)
}
