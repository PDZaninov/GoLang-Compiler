package main

import ( 
    "fmt"
    "time"
)

// Get all prime factors of a given number n
func PrimeFactors(n int) ([]int) {
    var pfs []int
	// Get the number of 2s that divide n
	for n%2 == 0 {
		pfs = append(pfs, 2)
		n = n / 2
	}

	// n must be odd at this point. so we can skip one element
	// (note i = i + 2)
	for i := 3; i*i <= n; i = i + 2 {
		// while i divides n, append i and divide n
		for n%i == 0 {
			pfs = append(pfs, i)
			n = n / i
		}
	}

	// This condition is to handle the case when n is a prime number
	// greater than 2
	if n > 2 {
		pfs = append(pfs, n)
	}

	return pfs
}

func forloopfib(x int) {
    a := 0
    b := 1
    c := 0
    for i:=0; i < x; i++ {
        c = a + b
        a = b
        b = c
    }
}

func fib(a int) int{
    if a == 0 {
        return 0
    }
    if a == 1 {
        return 1
    }
    return fib(a-1) + fib(a-2)
}

func main() {
    //UnixNano returns an int64 which cannot be implicitly converted to an int
    x := time.Now().UnixNano()
    for i := 0; i < 200000; i++{
        if(i == 10000){
            x = time.Now().UnixNano()
        }
        forloopfib(5)
        //fib(5)
        //PrimeFactors(13)
    }
    b := time.Now().UnixNano()
    fmt.Println(b-x)
    fmt.Println((b-x)/1000000)
}