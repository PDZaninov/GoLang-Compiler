package com.oracle.app.nodes.controlflow;

import com.oracle.app.nodes.GoStatementNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "break", description = "The node implementing a break statement")
public final class GoBreakNode extends GoStatementNode {

    @Override
    public void executeVoid(VirtualFrame frame) { throw GoBreakException.SINGLETON; }
}