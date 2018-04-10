package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRSliceExprNode extends GoBaseIRNode {

	GoBaseIRNode expr;
	String lbrack;
	GoBaseIRNode low;
	GoBaseIRNode high;
	GoBaseIRNode max;
	boolean slice3;
	String rbrack;

	public GoIRSliceExprNode(GoBaseIRNode sliceexpr, String slbrack, GoBaseIRNode low, GoBaseIRNode high,
			GoBaseIRNode max, String slice3, String srbrack) {
		super("Slice Expr Node");
		lbrack = slbrack;
		rbrack = srbrack;
		this.slice3 = Boolean.getBoolean(slice3);
		expr = sliceexpr;
		this.low = low;
		this.high = high;
		this.max = max;
	}

	public GoBaseIRNode getExpr() {
		return expr;
	}

	public String getLbrack() {
		return lbrack;
	}

	public GoBaseIRNode getLow() {
		return low;
	}

	public GoBaseIRNode getHigh() {
		return high;
	}

	public GoBaseIRNode getMax() {
		return max;
	}

	public boolean isSlice3() {
		return slice3;
	}

	public String getRbrack() {
		return rbrack;
	}

	@Override
	public void setChildParent() {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitSliceExpr(this);
	}

}
