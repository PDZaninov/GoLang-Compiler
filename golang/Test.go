package main
import "fmt"

type Rectangle struct {
	width int
	height int
}
func (r Rectangle) area() int {
	return r.width * r.height
}

func sum(x int, y int) int {
	return x + y
}
func threeSum(x int, y int, z int) int {
	return x + y + z
}

func vals() (int, int) {
	return 4, 2
}

func fib(x int) int {
	if x == 0 {
		return 0
	}

	if x == 1 {
		return 1
	}
	return fib(x-1) + fib(x-2)
}

func main() {
	//Struct and Struct Methods
	r := Rectangle{width:4, height:6}
	fmt.Println(r.area())

	//Multiple Returns
	a, b := vals()
	fmt.Println(a, b)

	//Recursion
	fmt.Println(fib(7))

	//Arrays
	var arr [7]int
	fmt.Println(arr)
	arr[0] = 10
	arr[4] = 70
	fmt.Println("Length: ", len(arr))
	x := [10]int{10,9,8,7,6,5,4,3,2,1}
	fmt.Println(x)

	//Slices
	s := make([]string, 3)
	s[0] = "a"
	s[1] = "b"
	s[2] = "c"
	s = append(s, "d")
	fmt.Println(s)
	x := s[1:3]
	fmt.Println(x)

	//Maps
	m := make(map[string]int)
	m["k1"] = 7
	m["k2"] = 13
	fmt.Println("map:", m)
	delete(m,"k1")
	fmt.Println(m)
	a := map[string]int{"foo": 1, "bar": 2}
	a["foo"] = 10
	a["bar"] = 3
	fmt.Println(a)

	//Functions
	a := sum(2,4)
	b := threeSum(2,4, a)
	fmt.Println(a)
	fmt.Println(b)
}