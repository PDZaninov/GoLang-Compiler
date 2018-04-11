package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRSwitchStmtNode extends GoBaseIRNode {

    GoIRStmtNode init;
    GoBaseIRNode tag; //Ident node in .ast file but in ast documentation is Expr Node
    GoIRBlockStmtNode body;
    String source;

    public GoIRSwitchStmtNode(GoIRStmtNode init, GoBaseIRNode tag, GoIRBlockStmtNode body,String source) {
        super("SwitchStmt");
        this.init = init;
        this.tag = tag;
        this.body = body;
        this.source = source;
    }
    
    public int getSourceLine(){
    	return Integer.parseInt(source.split(":")[1]);
    }

    public GoIRStmtNode getInit(){
        return init;
    }

    public GoBaseIRNode getTag(){
        return tag;
    }

    public GoIRBlockStmtNode getBody(){
        return body;
    }

    @Override
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitSwitchStmt(this);
    }

}