// generated with ast extension for cup
// version 0.8
// 7/2/2026 15:24:2


package rs.ac.bg.etf.pp1.ast;

public class Case implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private CaseValue CaseValue;
    private CaseStmt CaseStmt;
    private StatementList StatementList;

    public Case (CaseValue CaseValue, CaseStmt CaseStmt, StatementList StatementList) {
        this.CaseValue=CaseValue;
        if(CaseValue!=null) CaseValue.setParent(this);
        this.CaseStmt=CaseStmt;
        if(CaseStmt!=null) CaseStmt.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public CaseValue getCaseValue() {
        return CaseValue;
    }

    public void setCaseValue(CaseValue CaseValue) {
        this.CaseValue=CaseValue;
    }

    public CaseStmt getCaseStmt() {
        return CaseStmt;
    }

    public void setCaseStmt(CaseStmt CaseStmt) {
        this.CaseStmt=CaseStmt;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
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
        if(CaseValue!=null) CaseValue.accept(visitor);
        if(CaseStmt!=null) CaseStmt.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CaseValue!=null) CaseValue.traverseTopDown(visitor);
        if(CaseStmt!=null) CaseStmt.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CaseValue!=null) CaseValue.traverseBottomUp(visitor);
        if(CaseStmt!=null) CaseStmt.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Case(\n");

        if(CaseValue!=null)
            buffer.append(CaseValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CaseStmt!=null)
            buffer.append(CaseStmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Case]");
        return buffer.toString();
    }
}
