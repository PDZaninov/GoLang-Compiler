package com.oracle.app.parser.ir.nodes;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.app.nodes.controlflow.GoIfStmtNode;
import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.Node.Child;
import com.oracle.truffle.api.nodes.NodeInfo;

import java.util.ArrayList;

@NodeInfo(shortName = "if", description = "The node implementing a condional statement")

public class GoIRIfStmtNode extends GoBaseIRNode {

    @Child private String Init;
    @Child private GoIRExprNode Cond;
    @Child private GoIRBlockStmtNode Body;
    @Child private GoIRBlockStmtNode Else;

    public GoIRIfStmtNode(String Init, GoIRExprNode Cond, GoIRBlockStmtNode Body, GoIRBlockStmtNode Else) {
        super("If Node");
        this.Init=Init;
        this.Cond=Cond;
        this.Body=Body;
        this.Else=Else;
    }

    public String getInit() {
        return Init;
    }

    public  GoIRExprNode getCond() {
        return Cond;
    }

    public GoIRBlockStmtNode getBody() { return Body; }

    public GoIRBlockStmtNode getElse() { return Else; }

    @Override
    public void setChildParent() {
        // Do nothing :^)
        // Init.setParent(this);
//        Cond.setParent(this);
        Body.setParent(this);
        Else.setParent(this);
    }

    @Override
    public ArrayList<GoBaseIRNode> getChildren() {
        return null; //TODO: to be removed
    }

    @Override
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitIf(this);
    }

}
