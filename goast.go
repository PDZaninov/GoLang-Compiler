package main

import (
    "go/ast"
    "go/parser"
    "go/token"
    "reflect"
    "os"
)

func NotNilFilter(_ string, v reflect.Value) bool {
  	switch v.Kind() {
  	case reflect.Chan, reflect.Func, reflect.Interface, reflect.Map, reflect.Ptr, reflect.Slice:
  		return !v.IsNil()
  	}
  	return true
}

func main() {
    fset := new(token.FileSet)
    f, _ := parser.ParseFile(fset, os.Args[1], nil, 0)
    a, _ := os.Create("ast.txt")
    defer a.Close()
    ast.Fprint(a,fset,f,NotNilFilter)
    a.Sync()
}