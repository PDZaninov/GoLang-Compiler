package main

import (
	"go/ast"
	"go/parser"
	"go/token"
	"io/ioutil"
	"os"
)

func main() {
	// input := `
	// package main
	// func main() {
	// 	println("Hello, World!")
	// }
	// `
	file := os.Args[0]
	b, err := ioutil.ReadFile(file) //reads File
	// Create the AST by go src.
	fset := token.NewFileSet()                         // positions are relative to fset
	f, err := parser.ParseFile(fset, "", string(b), 0) //second input is file name
	if err != nil {
		panic(err)
	}

	ast.Print(fset, f)

}
