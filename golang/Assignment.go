package main

func vals(lol int, rofl int) (int,int,int){
x:=0
return lol,rofl,x

}

func me(x int) int {
return x

}

func main() {
	var yoyo int = me(3)
	println(yoyo)
	var x,y,zzz  = vals(5,yoyo)
	z,wtf, wtfz := vals(3,5)
	println(wtf + wtfz)
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
	x = me(3)
	println(me(3))
	array := make([]string,5)
	array = make([]string, 10, 15)
	sucker := append(array,"a","b")
	println(len(array))
	println(cap(sucker))
	
}