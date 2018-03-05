package com.oracle.app.nodes.controlflow;

import com.oracle.app.nodes.GoExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;


@NodeInfo(shortName = "switch", description = "A node implementing a switch statement")
public final class GoSwitchNode extends GoStatementNode {

    @Child private GoStatementNode init;
    @Child private GoExpressionNode tag;
    @Child private GoBlockNode body;

    public GoSwitchNode(GoStatementNode init, GoExpressionNode tag, GoBlockNode body){
        this.init = init;
        this.tag = tag;
        this.body = body;
    }

    @Override
    public void executeVoid(VirtualFrame frame){
    	if(init != null){
    		init.executeVoid(frame);
    	}
        Object value = tag.executeGeneric(frame);
        body.switchExecute(frame, value);
    }
}