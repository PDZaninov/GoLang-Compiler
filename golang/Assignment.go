package main

func vals() (int,int,int){
return 5,7,8

}

func me() int {
return 1
}

func main() {
	var yoyo int = me()
	println(yoyo)
	var x,y,zzz  = vals()
	z := me()
	println(x)
	println(y)
	println(z)
	println(zzz)
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
	x = me()
	println(me())
	array := make([]string,5)
	array = make([]string, 10, 15)
	sucker := append(array,"a","b")
	println(len(array))
	println(cap(sucker))
	
}