// generated with ast extension for cup
// version 0.8
// 7/2/2026 15:24:2


package rs.ac.bg.etf.pp1.ast;

public class ConstDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private ConstTypeName ConstTypeName;
    private AdditionalAssign AdditionalAssign;

    public ConstDecl (Type Type, ConstTypeName ConstTypeName, AdditionalAssign AdditionalAssign) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ConstTypeName=ConstTypeName;
        if(ConstTypeName!=null) ConstTypeName.setParent(this);
        this.AdditionalAssign=AdditionalAssign;
        if(AdditionalAssign!=null) AdditionalAssign.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public ConstTypeName getConstTypeName() {
        return ConstTypeName;
    }

    public void setConstTypeName(ConstTypeName ConstTypeName) {
        this.ConstTypeName=ConstTypeName;
    }

    public AdditionalAssign getAdditionalAssign() {
        return AdditionalAssign;
    }

    public void setAdditionalAssign(AdditionalAssign AdditionalAssign) {
        this.AdditionalAssign=AdditionalAssign;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ConstTypeName!=null) ConstTypeName.accept(visitor);
        if(AdditionalAssign!=null) AdditionalAssign.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ConstTypeName!=null) ConstTypeName.traverseTopDown(visitor);
        if(AdditionalAssign!=null) AdditionalAssign.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ConstTypeName!=null) ConstTypeName.traverseBottomUp(visitor);
        if(AdditionalAssign!=null) AdditionalAssign.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstTypeName!=null)
            buffer.append(ConstTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AdditionalAssign!=null)
            buffer.append(AdditionalAssign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDecl]");
        return buffer.toString();
    }
}
