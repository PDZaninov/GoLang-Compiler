package com.oracle.app.nodes.expression;
import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

/**
 * Go does not concatenate strings with other objects so there is no default type
 * as that allows for the wrong result. 
 * @author Trevor
 *
 */
@NodeInfo(shortName = "+")
public abstract class GoAddNode extends GoBinaryNode {

    @Specialization(rewriteOn = ArithmeticException.class)
    protected int add(int left, int right) {
        return Math.addExact(left, right);
    }
    
    @Specialization(rewriteOn = ArithmeticException.class)
    protected float add(float left, float right) {
        return left + right;
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    protected double add(double left, double right) {
        return left + right;
    }	

    @Specialization(guards = "isString(left, right)")
    @TruffleBoundary
    protected String add(String left, String right) {
        return left + right;
    }

    protected boolean isString(Object a, Object b) {
        return a instanceof String || b instanceof String;
    }

	@Override
	public String toString() {
		return "Add Node";
	}

    
}