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

    @Child private GoBaseIRNode Init;
    @Child private GoBaseIRNode Cond;
    @Child private GoBaseIRNode Body;
    @Child private GoBaseIRNode Else;

    public GoIRIfStmtNode(GoBaseIRNode Init, GoBaseIRNode Cond, GoBaseIRNode Body, GoBaseIRNode Else) {
        super("If Node");
        this.Init=Init;
        this.Cond=Cond;
        this.Body=Body;
        this.Else=Else;
        setChildParent();
    }

    public GoBaseIRNode getInit() {
        return Init;
    }

    public  GoBaseIRNode getCond() {
        return Cond;
    }

    public GoBaseIRNode getBody() { 
    	return Body; 
    }

    public GoBaseIRNode getElse() { 
    	return Else; 
    }

    @Override
    public void setChildParent() {
        if (Init!=null) {
            Init.setParent(this);
        }
        Cond.setParent(this);
        Body.setParent(this);
        if(Else != null){
        	Else.setParent(this);
        }
    }

    @Override
    public ArrayList<GoBaseIRNode> getChildren() {
        return null; //TODO: to be removed
    }

    @Override
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitIfStmt(this);
    }

}
