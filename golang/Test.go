package main

import "fmt"

func main() {
m := make(map[string]int)
   m["k1"] = 7
   m["k2"] = 13
   fmt.Println("map:", m)
   delete(m,"k1")
   fmt.Println(m)

}