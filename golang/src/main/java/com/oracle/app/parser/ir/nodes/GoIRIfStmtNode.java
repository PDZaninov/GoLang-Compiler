package com.oracle.app.parser.ir.nodes;

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
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitIfStmt(this);
    }

}
