// generated with ast extension for cup
// version 0.8
// 7/2/2026 15:24:3


package rs.ac.bg.etf.pp1.ast;

public class CondTerm implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private AddCondFact AddCondFact;

    public CondTerm (AddCondFact AddCondFact) {
        this.AddCondFact=AddCondFact;
        if(AddCondFact!=null) AddCondFact.setParent(this);
    }

    public AddCondFact getAddCondFact() {
        return AddCondFact;
    }

    public void setAddCondFact(AddCondFact AddCondFact) {
        this.AddCondFact=AddCondFact;
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
        if(AddCondFact!=null) AddCondFact.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AddCondFact!=null) AddCondFact.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AddCondFact!=null) AddCondFact.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondTerm(\n");

        if(AddCondFact!=null)
            buffer.append(AddCondFact.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondTerm]");
        return buffer.toString();
    }
}
