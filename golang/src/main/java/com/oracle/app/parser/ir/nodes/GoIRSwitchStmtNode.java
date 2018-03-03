package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.app.parser.ir.GoIRStmtNode;
import com.oracle.app.parser.ir.GoIRBlockStmtNode;

public class GoIRSwitchStmtNode extends GoBaseIRNode {

    GoIRStmtNode init;
    GoBaseIRNode tag; //Ident node in .ast file but in ast documentation is Expr Node
    GoIRBlockStmtNode body;


    public GoIRSwitchStmtNode(GoIRStmtNode init, GoBaseIRNode tag, GoIRBlockStmtNode body) {
        super("SwitchStmt");
        this.init = init;
        this.tag = tag;
        this.body = body;
        setChildParent();
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
    public void setChildParent() {
        if(init != null){
            init.setParent(this);
        }
        if(tag != null){
            tag.setParent(this);
        }
        if(body != null){
            body.setParent(this);
        }

    }

    @Override
    public ArrayList<GoBaseIRNode> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitSwitchStmt(this);
    }

}