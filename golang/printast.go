package main

import (
	"go/ast"
	"go/parser"
	"go/token"
	"os"
	"reflect"
)

func main() {
	// src is the input for which we want to print the AST.
	src := `
package main



func main() {
	println("True")
	println(true)
	println("1<2 ")
	println(1<2)
	println("1<=2 ")
	println(1<=2)
	println("2<=2 ")
	println(2<=2)
	println("3>2 ")
	println(3>2)
	println("3>=2 ")
	println(3>=3)
	println("1==1 ")
	println(1==1)
	println("1!=2 ")
	println(1!=2)
	println("true&&true : 4>0&&5>0")
	println(4>0&&5>0)
	println("true||false : 3>1||5<2")
	println(3>1||5<2)
	println("False")
	println(false)
	println("2<2 ")
	println(2<2)
	println("10<=2 ")
	println(10<=2)
	println("5<=2 ")
	println(5<=2)
	println("1>2 ")
	println(1>2)
	println("1>=3 ")
	println(1>=3)
	println("1==2 ")
	println(1==2)
	println("1!=1 ")
	println(1!=1)
	println("true&&false : 2>1&&3>4")
	println(2>1&&3>4)
	println("false||false : 2<1||1<0")
	println(2<1||1<0)
	println("Done")

}
`

	// Create the AST by parsing src.
	fset := token.NewFileSet() // positions are relative to fset
	f, err := parser.ParseFile(fset, "", src, 0)
	if err != nil {
		panic(err)
	}

	// Print the AST.
	// ast.Print(fset, f)
	f2, err2 := os.Create("comparison.ast")
	if err2 != nil {
		panic(err2)
	}
	ast.Fprint(f2, fset, f, func(name string, value reflect.Value) bool {
		if ast.NotNilFilter(name, value) {
			return value.Type().String() != "*ast.Object"
		}
		return false
	})
}