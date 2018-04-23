package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRFileNode extends GoBaseIRNode {

	GoIRIdentNode name;
	GoBaseIRNode decls;
	GoBaseIRNode imports;
	
	public GoIRFileNode(GoIRIdentNode name, GoBaseIRNode decls, GoBaseIRNode imports) {
		super("IR File Node");
		this.name = name;
		this.decls = decls;
		this.imports = imports;
	}

	public GoIRIdentNode getName() {
		return name;
	}

	public GoBaseIRNode getDecls() {
		return decls;
	}

	public GoBaseIRNode getImports() {
		return imports;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitFile(this);
	}

}
