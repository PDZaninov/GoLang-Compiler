package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

import java.util.ArrayList;

public class GoIRSelectorExprNode extends GoBaseIRNode {

    private GoIRIdentNode importName;

    private GoIRIdentNode importMethod;

    public GoIRSelectorExprNode(GoIRIdentNode importName, GoIRIdentNode importMethod) {
        super("SelectorExpr");
        this.importName = importName;
        this.importMethod = importMethod;
        setChildParent();
    }

    @Override
    public void setChildParent() {
        if(importName != null)
            importName.setParent(this);
        if(importMethod != null)
            importMethod.setParent(this);
    }

    @Override
    public ArrayList<GoBaseIRNode> getChildren() {
        return null;
    }

    public GoIRIdentNode getImportName() {
        return importName;
    }

    public GoIRIdentNode getImportMethod() {
        return importMethod;
    }

    @Override
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitSelectorExpr(this);
    }
}
