package main

func vals() (int,int){
return 5,7

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
	var a,b = 1,2
	var c = 3
	println(a)
	println(b)
	println(c)
	d,f := 1,2
	g := 3
	println(d)
	println(f)
	println(g)
	r,t := vals()
	y := me()
	println(me())
	array := make([]string,5)
	array = make([]string, 10, 15)
	sucker := append(array,"a","b")
	println(len(array))
	println(cap(sucker))
	
}