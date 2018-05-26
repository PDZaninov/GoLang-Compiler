package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRCaseClauseNode extends GoBaseIRNode {

    GoIRArrayListExprNode list;
    GoIRStmtNode body;
    String casetok;
    String colon;

    public GoIRCaseClauseNode(GoIRArrayListExprNode list, GoIRStmtNode body,String casetok, String colon) {
        super("CaseClause");
        this.list = list;
        this.body = body;
        this.casetok = casetok;
        this.colon = colon;
    }

    public int getSourceLine(){
    	return Integer.parseInt(casetok.split(":")[1]);
    }
    
    public int getCaseStart(){
    	return Integer.parseInt(casetok.split(":")[2]);
    }
    
    public int getSourceLength(){
    	return Integer.parseInt(colon.split(":")[2]);
    }
    
    public GoIRStmtNode getBody() {
        return body;
    }

    public GoIRArrayListExprNode getList() {
        return list;
    }

    @Override
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitCaseClause(this);
    }
}