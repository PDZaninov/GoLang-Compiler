package  main

func add() (int,string) {
	return 6, "lol"
}
func main(){
	var m string = "S"
	var l int = 7
	l,m = add()
	println(l)
	println(m)
}