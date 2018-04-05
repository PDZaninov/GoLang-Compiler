package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.runtime.GoContext;
import com.oracle.app.runtime.GoFunction;
import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoSelectorExprNode extends GoExpressionNode {

    String name;
    String packageName;
    boolean installed = false;
    @Child private GoIdentNode importPackage;

    @Child private GoIdentNode importMethod;

    private final ContextReference<GoContext> reference;

    public GoSelectorExprNode(GoLanguage language, GoIdentNode importPackage, GoIdentNode importMethod) {
        this.name = importMethod.getName();
        this.packageName = importPackage.getName();
        this.importPackage = importPackage;
        this.importMethod = importMethod;
        reference = language.getContextReference();
    }

    @Override
    public String toString() {
        return name;
    }

    public GoFunction getFunction(){
        return reference.get().getFunctionRegistry().lookup(name, true);
    }

    public String getName(){
        return name;
    }

    public String getPackageName() { return packageName; }

    public void loadBuiltIn() {
        if(installed) {
            return;
        }
        switch(packageName) {
            case "fmt":
                reference.get().installFmt();
                break;
            default:
                System.out.println("Package not yet done");
        }
        installed = true;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return null;
    }



}
