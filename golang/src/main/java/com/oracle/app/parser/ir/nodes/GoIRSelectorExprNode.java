package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRSelectorExprNode extends GoBaseIRNode {

    private GoIRIdentNode importName;

    private GoIRIdentNode importMethod;

    public GoIRSelectorExprNode(GoIRIdentNode importName, GoIRIdentNode importMethod) {
        super("SelectorExpr");
        this.importName = importName;
        this.importMethod = importMethod;
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
