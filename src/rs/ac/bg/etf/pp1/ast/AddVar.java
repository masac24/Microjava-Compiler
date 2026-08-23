// generated with ast extension for cup
// version 0.8
// 7/2/2026 15:24:2


package rs.ac.bg.etf.pp1.ast;

public class AddVar extends AdditionalVarDecl {

    private VarItem VarItem;
    private AdditionalVarDecl AdditionalVarDecl;

    public AddVar (VarItem VarItem, AdditionalVarDecl AdditionalVarDecl) {
        this.VarItem=VarItem;
        if(VarItem!=null) VarItem.setParent(this);
        this.AdditionalVarDecl=AdditionalVarDecl;
        if(AdditionalVarDecl!=null) AdditionalVarDecl.setParent(this);
    }

    public VarItem getVarItem() {
        return VarItem;
    }

    public void setVarItem(VarItem VarItem) {
        this.VarItem=VarItem;
    }

    public AdditionalVarDecl getAdditionalVarDecl() {
        return AdditionalVarDecl;
    }

    public void setAdditionalVarDecl(AdditionalVarDecl AdditionalVarDecl) {
        this.AdditionalVarDecl=AdditionalVarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarItem!=null) VarItem.accept(visitor);
        if(AdditionalVarDecl!=null) AdditionalVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarItem!=null) VarItem.traverseTopDown(visitor);
        if(AdditionalVarDecl!=null) AdditionalVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarItem!=null) VarItem.traverseBottomUp(visitor);
        if(AdditionalVarDecl!=null) AdditionalVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AddVar(\n");

        if(VarItem!=null)
            buffer.append(VarItem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AdditionalVarDecl!=null)
            buffer.append(AdditionalVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AddVar]");
        return buffer.toString();
    }
}
