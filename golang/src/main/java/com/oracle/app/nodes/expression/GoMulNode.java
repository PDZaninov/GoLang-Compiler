package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

/**
 * This class is similar to the extensively documented {@link GoAddNode}.
 */
@NodeInfo(shortName = "*")
public abstract class GoMulNode extends GoBinaryNode {
    
    @Specialization(rewriteOn = ArithmeticException.class)
    protected int mul(int left, int right) {
        return Math.multiplyExact(left, right);
    }
    
    @Specialization(rewriteOn = ArithmeticException.class)
    protected float mul(float left, float right) {
        return left * right;
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    protected double mul(double left, double right) {
        return left * right;
    }

}