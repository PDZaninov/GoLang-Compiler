package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRIfStmtNode extends GoBaseIRNode {

    GoBaseIRNode init;
    GoBaseIRNode cond;
    GoBaseIRNode body;
    GoBaseIRNode elsenode;
    String iftok;

    public GoIRIfStmtNode(GoBaseIRNode init, GoBaseIRNode cond, GoBaseIRNode body, GoBaseIRNode elsenode,String iftok) {
        super("If Node");
        this.init=init;
        this.cond=cond;
        this.body=body;
        this.elsenode=elsenode;
        this.iftok = iftok;
        setChildParent();
    }

    public GoBaseIRNode getInit() {
        return init;
    }

    public  GoBaseIRNode getCond() {
        return cond;
    }

    public GoBaseIRNode getBody() { 
    	return body; 
    }

    public GoBaseIRNode getElse() { 
    	return elsenode; 
    }

    @Override
    public void setChildParent() {
        if (init!=null) {
            init.setParent(this);
        }
        cond.setParent(this);
        body.setParent(this);
        if(elsenode != null){
        	elsenode.setParent(this);
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
