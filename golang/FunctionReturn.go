package main

func vals() (int,int){
	return 5,me()
}

func me() int {
	return 1
}

func main() {
	var x,y  = vals()
	z := me()
	println(x)
	println(y)
	println(z)
}