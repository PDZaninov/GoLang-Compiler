package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRSelectorExprNode extends GoBaseIRNode {

    private GoBaseIRNode expr;
	private GoIRIdentNode name;

    public GoIRSelectorExprNode(GoBaseIRNode expr, GoIRIdentNode name) {
        super("SelectorExpr");
        this.expr = expr;
        this.name = name;
    }

    public GoBaseIRNode getExpr() {
		return expr;
	}

	public GoIRIdentNode getName() {
		return name;
	}
    
    @Override
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitSelectorExpr(this);
    }
}
