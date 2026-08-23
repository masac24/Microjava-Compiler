// generated with ast extension for cup
// version 0.8
// 7/2/2026 15:24:2


package rs.ac.bg.etf.pp1.ast;

public class MultipleAssign extends AdditionalAssign {

    private AdditionalAssign AdditionalAssign;
    private ConstTypeName ConstTypeName;

    public MultipleAssign (AdditionalAssign AdditionalAssign, ConstTypeName ConstTypeName) {
        this.AdditionalAssign=AdditionalAssign;
        if(AdditionalAssign!=null) AdditionalAssign.setParent(this);
        this.ConstTypeName=ConstTypeName;
        if(ConstTypeName!=null) ConstTypeName.setParent(this);
    }

    public AdditionalAssign getAdditionalAssign() {
        return AdditionalAssign;
    }

    public void setAdditionalAssign(AdditionalAssign AdditionalAssign) {
        this.AdditionalAssign=AdditionalAssign;
    }

    public ConstTypeName getConstTypeName() {
        return ConstTypeName;
    }

    public void setConstTypeName(ConstTypeName ConstTypeName) {
        this.ConstTypeName=ConstTypeName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AdditionalAssign!=null) AdditionalAssign.accept(visitor);
        if(ConstTypeName!=null) ConstTypeName.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AdditionalAssign!=null) AdditionalAssign.traverseTopDown(visitor);
        if(ConstTypeName!=null) ConstTypeName.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AdditionalAssign!=null) AdditionalAssign.traverseBottomUp(visitor);
        if(ConstTypeName!=null) ConstTypeName.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultipleAssign(\n");

        if(AdditionalAssign!=null)
            buffer.append(AdditionalAssign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstTypeName!=null)
            buffer.append(ConstTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultipleAssign]");
        return buffer.toString();
    }
}
