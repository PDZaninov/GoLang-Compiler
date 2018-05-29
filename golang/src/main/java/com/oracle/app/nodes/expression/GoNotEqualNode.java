package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.app.runtime.GoFunction;
import com.oracle.app.runtime.GoNull;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "!=")
public abstract class GoNotEqualNode extends GoBinaryNode {

    @Specialization
    protected boolean  notEqual(int left, int right) {
        return left != right;
    }

    @Specialization
    protected boolean  notEqual(boolean left, boolean right) {
        return left != right;
    }

    @Specialization
    protected boolean  notEqual(String left, String right) {
        return !left.equals(right);
    }

    @Specialization
    protected boolean  notEqual(GoFunction left, GoFunction right) {
        return left != right;
    }

    @Specialization
    protected boolean  notEqual(GoNull left, GoNull right) {
        return left != right;
    }

}
