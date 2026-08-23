// generated with ast extension for cup
// version 0.8
// 7/2/2026 15:24:2


package rs.ac.bg.etf.pp1.ast;

public class AbstractClassDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private AbstractClassName AbstractClassName;
    private AddExtend AddExtend;
    private VarDeclList VarDeclList;
    private AbstractClassMethod AbstractClassMethod;

    public AbstractClassDecl (AbstractClassName AbstractClassName, AddExtend AddExtend, VarDeclList VarDeclList, AbstractClassMethod AbstractClassMethod) {
        this.AbstractClassName=AbstractClassName;
        if(AbstractClassName!=null) AbstractClassName.setParent(this);
        this.AddExtend=AddExtend;
        if(AddExtend!=null) AddExtend.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.AbstractClassMethod=AbstractClassMethod;
        if(AbstractClassMethod!=null) AbstractClassMethod.setParent(this);
    }

    public AbstractClassName getAbstractClassName() {
        return AbstractClassName;
    }

    public void setAbstractClassName(AbstractClassName AbstractClassName) {
        this.AbstractClassName=AbstractClassName;
    }

    public AddExtend getAddExtend() {
        return AddExtend;
    }

    public void setAddExtend(AddExtend AddExtend) {
        this.AddExtend=AddExtend;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public AbstractClassMethod getAbstractClassMethod() {
        return AbstractClassMethod;
    }

    public void setAbstractClassMethod(AbstractClassMethod AbstractClassMethod) {
        this.AbstractClassMethod=AbstractClassMethod;
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
        if(AbstractClassName!=null) AbstractClassName.accept(visitor);
        if(AddExtend!=null) AddExtend.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(AbstractClassMethod!=null) AbstractClassMethod.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AbstractClassName!=null) AbstractClassName.traverseTopDown(visitor);
        if(AddExtend!=null) AddExtend.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(AbstractClassMethod!=null) AbstractClassMethod.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AbstractClassName!=null) AbstractClassName.traverseBottomUp(visitor);
        if(AddExtend!=null) AddExtend.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(AbstractClassMethod!=null) AbstractClassMethod.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AbstractClassDecl(\n");

        if(AbstractClassName!=null)
            buffer.append(AbstractClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AddExtend!=null)
            buffer.append(AddExtend.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AbstractClassMethod!=null)
            buffer.append(AbstractClassMethod.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AbstractClassDecl]");
        return buffer.toString();
    }
}
