package com.oracle.app.nodes;

public class GoImportSpec extends GoSpec{
	
	String Name; //// local package name (including "."); or nil
	int EndPos;  // end of spec (overrides Path.Pos if nonzero)
	String Path;  // import path
	String Doc;
	
	
	public GoImportSpec(String d, String p, String n, int e) {
		Name = n;
		EndPos = e;
		Path = p;
		Doc = d;
		EndPos = e;
	}
	
	public int End() {
		return EndPos;
	}
	
}


//An ImportSpec node represents a single package import.
//
//type ImportSpec struct {
//        Doc     *CommentGroup // associated documentation; or nil
//        Name    *Ident        // local package name (including "."); or nil
//        Path    *BasicLit     // import path
//        Comment *CommentGroup // line comments; or nil
//        EndPos  token.Pos     // end of spec (overrides Path.Pos if nonzero)
//}
//func (*ImportSpec) End
//func (s *ImportSpec) End() token.Pos
//func (*ImportSpec) Pos
//func (s *ImportSpec) Pos() token.Pos