package main

func vals() func() int{
	i := 0
	return func() int{
		i++
		return i
	}

}

func main() {
	x := intSeq()
	println(x())
	println(x())
}