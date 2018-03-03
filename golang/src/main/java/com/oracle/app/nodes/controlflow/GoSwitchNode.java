package com.oracle.app.nodes.controlflow;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoExprNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.app.nodes.controlflow.GoBlockNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node.Child;
import come.oracle.truffle.api.nodes.NodeInfo;


@NodeInfo(shortName = "switch", description = "A node implementing a switch statement")
public final class GoSwitchNode extends GoStatementNode {

    @Child private final GoStatementNode init;
    @Child private final GoExpressionNode tag;
    @Child private final GoBlockNode body;

    public GoSwitchNode(GoStatementNode init, GoExprNode tag, GoBlockNode body){
        this.init = init;
        this.tag = tag;
        this.body = body;
    }

    @Override
    public void executeVoid(Virtualframe frame){
        init.executeVoid(frame);
        Object value = tag.executeGeneric(frame);
        body.executeVoid(frame, value);
    }
}