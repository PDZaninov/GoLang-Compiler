package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

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
        setChildParent();
    }

    @Override
    public void setChildParent() {
        if (list != null){
            list.setParent(this);
        }

        if (body != null) {
            body.setParent(this);
        }
    }

    @Override
    public ArrayList<GoBaseIRNode> getChildren() {
        return null;
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

/**
 * SWITCH STATMENTS IN GOLANG
 *
 * - You can use commas to separate multiple expressions in the same case statement. We use the optional default case in this example as well.
 * - switch without an expression is an alternate way to express if/else logic. Here we also show how the case expressions can be non-constants.
 * - A type switch compares types instead of values. You can use this to discover the type of an interface value. In this example, the variable t will have the type corresponding to its clause.
 *
 * TYPE CASECLAUSE IN GO/AST DOCUMENTATION:
 *
 * type CaseClause struct {
 *      Case  token.Pos // position of "case" or "default" keyword
 *      List  []Expr    // list of expressions or types; nil means default case
 *      Colon token.Pos // position of ":"
 *      Body  []Stmt    // statement list; or nil
 * }
 *
 **/