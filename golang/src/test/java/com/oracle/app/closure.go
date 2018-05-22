package main

func intSeq() func() int {
    i := 0
    return func() int {
        i++
        return i
    }
}

func main() {
	nextInt := intSeq()
    x := nextInt()
    x = nextInt()
    return x
}