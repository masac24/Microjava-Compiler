// generated with ast extension for cup
// version 0.8
// 7/2/2026 15:24:2


package rs.ac.bg.etf.pp1.ast;

public class Extends extends AddExtend {

    private WasExtend WasExtend;
    private Type Type;

    public Extends (WasExtend WasExtend, Type Type) {
        this.WasExtend=WasExtend;
        if(WasExtend!=null) WasExtend.setParent(this);
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
    }

    public WasExtend getWasExtend() {
        return WasExtend;
    }

    public void setWasExtend(WasExtend WasExtend) {
        this.WasExtend=WasExtend;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(WasExtend!=null) WasExtend.accept(visitor);
        if(Type!=null) Type.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(WasExtend!=null) WasExtend.traverseTopDown(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(WasExtend!=null) WasExtend.traverseBottomUp(visitor);
        if(Type!=null) Type.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Extends(\n");

        if(WasExtend!=null)
            buffer.append(WasExtend.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Extends]");
        return buffer.toString();
    }
}
