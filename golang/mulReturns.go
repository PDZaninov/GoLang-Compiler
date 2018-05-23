package main

func vals() (int, int){
return 5,7+3
}

func me() int{
	var b,c = vals()
	return b+c
}
func main(){

	println(me())
}