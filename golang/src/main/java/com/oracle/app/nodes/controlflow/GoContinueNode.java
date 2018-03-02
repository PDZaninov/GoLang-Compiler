package com.oracle.app.nodes.controlflow;

import com.oracle.app.nodes.GoStatementNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "continue", description = "The node implementing a continue statement")
public final class GoContinueNode extends GoStatementNode {

    @Override
    public void executeVoid(VirtualFrame frame) { throw GoContinueException.SINGLETON; }
}