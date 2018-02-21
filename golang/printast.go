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

	println("1>0||2>1")
	println(1>0||2>1)
	println("0>0||2>1")
	println(0>0||2>1)
	println("0>0||0>1")
	println(0>0||0>1)

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
	f2, err2 := os.Create("LogicalOr.ast")
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