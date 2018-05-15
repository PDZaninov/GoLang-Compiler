package main

func vals() (int,int){
var b int = 1
var c int = 5

return b,c

}

func main() {
	var x,y = vals()
	for b := 1; b <= 5; b++{
		x *=b
	}
	println(x + y)
	var z = x + "lol"
	println(z)
}