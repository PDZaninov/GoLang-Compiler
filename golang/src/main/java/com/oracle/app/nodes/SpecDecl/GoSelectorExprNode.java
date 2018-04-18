package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoSelectorExprNode extends GoExpressionNode {

    @Child private GoExpressionNode expr;
    @Child private GoIdentNode name;


    public GoSelectorExprNode(GoExpressionNode expr, GoIdentNode name) {
        this.expr = expr;
        this.name = name;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return null;
    }



}
