// generated with ast extension for cup
// version 0.8
// 7/2/2026 15:24:2


package rs.ac.bg.etf.pp1.ast;

public class VarNameIdent extends VarItem {

    private String varname;

    public VarNameIdent (String varname) {
        this.varname=varname;
    }

    public String getVarname() {
        return varname;
    }

    public void setVarname(String varname) {
        this.varname=varname;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarNameIdent(\n");

        buffer.append(" "+tab+varname);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarNameIdent]");
        return buffer.toString();
    }
}
