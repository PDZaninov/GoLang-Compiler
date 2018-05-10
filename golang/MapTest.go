package main

import "fmt"

func main() {
    m := make(map[string]int)
   m["k1"] = 7
   m["k2"] = 13
   fmt.Println("map:", m)
   delete(m,"k1")
   fmt.Println(m)

   
   a := map[string]int{"foo": 1, "bar": 2}
   a["stuff"] = 10
   a["foo"] = 3
   fmt.Println(a)
}