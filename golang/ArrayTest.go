package main

func main() {
	var array [10]int

	for i := 0; i < len(array); i++ {
        array[i] = 2
    }

    println(array[2])
}