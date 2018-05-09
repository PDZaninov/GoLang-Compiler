package main

import "fmt"

func main() {
	s := make([]string, 3)
    	s[0] = "a"
   	s[1] = "b"
   	s[2] = "c"

    	b := []string{"d","e","f"}
	s = append(s[:1], b[1:]...)
	fmt.Println(s)
   	 m := map[string]int{"a":1,"b":2,"c":3}
   	 println(m["a"])
}
