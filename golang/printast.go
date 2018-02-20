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
	println("False")
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