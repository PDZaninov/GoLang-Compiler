package main

func me() int {
	var a [5] int 
	result := 0 
	for i := 0; i < len(a); i++{ 
		result += a[i] 
	}
	b := [5]int{1 , 2, 3, 4, 5} 
	b[4] = 10 
	for j := 0; j < len(b); j++ { 
		result += b[j] 
	} 
	return result 
}

func main(){ 
	println(me())
}