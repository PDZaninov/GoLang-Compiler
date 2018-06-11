package com.oracle.app.nodes.expression;
import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;


@NodeInfo(shortName = "%")
public abstract class GoModNode extends GoBinaryNode {


    @Specialization(rewriteOn = ArithmeticException.class)
    protected int mod(int left, int right) {
        return left % right;
    }
    
    @Specialization(rewriteOn = ArithmeticException.class)
    protected float mod(float left, float right) {
    	return left % right;
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    protected double mod(double left, double right) {
        return left % right;
    }
}