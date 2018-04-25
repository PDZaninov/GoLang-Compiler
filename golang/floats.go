package main

func vals() (float64,float32){
var b float64 = 2
var c float32 = 3

return b,c

}

func main() {
	var x,y = vals()
	println(x)
	println(y)
}