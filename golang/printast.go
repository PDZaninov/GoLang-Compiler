package main

import (
	"go/ast"
	"go/parser"
	"go/token"
	"os"
)

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func main() {
	//TODO: add a way to write the files to a text
	files := os.Args[1:]
	// Create the AST by go src.
	fset := token.NewFileSet()
	for i := 0; i < len(files); i++ {
		f, err := parser.ParseFile(fset, string(files[i]), nil, parser.ParseComments)
		check(err)
		ast.Print(fset, f)
		// d1 := []byte(asts)
		// err := ioutil.WriteFile("/tmp/"+string(files[i]), d1, 0644)
		// check(err)
	}

}
