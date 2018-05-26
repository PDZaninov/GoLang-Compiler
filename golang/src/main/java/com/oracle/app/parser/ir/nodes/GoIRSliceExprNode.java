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

	public int getRbrackLineNum() {
		String[] split = rbrack.split(":");
		return Integer.parseInt(split[1]);
	}
	
	public int getRbrackStartColumn(){
		String[] split = rbrack.split(":");
		return Integer.parseInt(split[2]);
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
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitSliceExpr(this);
	}

}
