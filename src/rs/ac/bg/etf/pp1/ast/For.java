// generated with ast extension for cup
// version 0.8
// 7/2/2026 15:24:2


package rs.ac.bg.etf.pp1.ast;

public class For extends Statement {

    private EnterFor EnterFor;
    private DesAdd DesAdd;
    private ReturnToCondition ReturnToCondition;
    private ConAdd ConAdd;
    private ReturnToDes ReturnToDes;
    private DesAddSec DesAddSec;
    private GoToStatement GoToStatement;
    private ForStatement ForStatement;

    public For (EnterFor EnterFor, DesAdd DesAdd, ReturnToCondition ReturnToCondition, ConAdd ConAdd, ReturnToDes ReturnToDes, DesAddSec DesAddSec, GoToStatement GoToStatement, ForStatement ForStatement) {
        this.EnterFor=EnterFor;
        if(EnterFor!=null) EnterFor.setParent(this);
        this.DesAdd=DesAdd;
        if(DesAdd!=null) DesAdd.setParent(this);
        this.ReturnToCondition=ReturnToCondition;
        if(ReturnToCondition!=null) ReturnToCondition.setParent(this);
        this.ConAdd=ConAdd;
        if(ConAdd!=null) ConAdd.setParent(this);
        this.ReturnToDes=ReturnToDes;
        if(ReturnToDes!=null) ReturnToDes.setParent(this);
        this.DesAddSec=DesAddSec;
        if(DesAddSec!=null) DesAddSec.setParent(this);
        this.GoToStatement=GoToStatement;
        if(GoToStatement!=null) GoToStatement.setParent(this);
        this.ForStatement=ForStatement;
        if(ForStatement!=null) ForStatement.setParent(this);
    }

    public EnterFor getEnterFor() {
        return EnterFor;
    }

    public void setEnterFor(EnterFor EnterFor) {
        this.EnterFor=EnterFor;
    }

    public DesAdd getDesAdd() {
        return DesAdd;
    }

    public void setDesAdd(DesAdd DesAdd) {
        this.DesAdd=DesAdd;
    }

    public ReturnToCondition getReturnToCondition() {
        return ReturnToCondition;
    }

    public void setReturnToCondition(ReturnToCondition ReturnToCondition) {
        this.ReturnToCondition=ReturnToCondition;
    }

    public ConAdd getConAdd() {
        return ConAdd;
    }

    public void setConAdd(ConAdd ConAdd) {
        this.ConAdd=ConAdd;
    }

    public ReturnToDes getReturnToDes() {
        return ReturnToDes;
    }

    public void setReturnToDes(ReturnToDes ReturnToDes) {
        this.ReturnToDes=ReturnToDes;
    }

    public DesAddSec getDesAddSec() {
        return DesAddSec;
    }

    public void setDesAddSec(DesAddSec DesAddSec) {
        this.DesAddSec=DesAddSec;
    }

    public GoToStatement getGoToStatement() {
        return GoToStatement;
    }

    public void setGoToStatement(GoToStatement GoToStatement) {
        this.GoToStatement=GoToStatement;
    }

    public ForStatement getForStatement() {
        return ForStatement;
    }

    public void setForStatement(ForStatement ForStatement) {
        this.ForStatement=ForStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnterFor!=null) EnterFor.accept(visitor);
        if(DesAdd!=null) DesAdd.accept(visitor);
        if(ReturnToCondition!=null) ReturnToCondition.accept(visitor);
        if(ConAdd!=null) ConAdd.accept(visitor);
        if(ReturnToDes!=null) ReturnToDes.accept(visitor);
        if(DesAddSec!=null) DesAddSec.accept(visitor);
        if(GoToStatement!=null) GoToStatement.accept(visitor);
        if(ForStatement!=null) ForStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnterFor!=null) EnterFor.traverseTopDown(visitor);
        if(DesAdd!=null) DesAdd.traverseTopDown(visitor);
        if(ReturnToCondition!=null) ReturnToCondition.traverseTopDown(visitor);
        if(ConAdd!=null) ConAdd.traverseTopDown(visitor);
        if(ReturnToDes!=null) ReturnToDes.traverseTopDown(visitor);
        if(DesAddSec!=null) DesAddSec.traverseTopDown(visitor);
        if(GoToStatement!=null) GoToStatement.traverseTopDown(visitor);
        if(ForStatement!=null) ForStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnterFor!=null) EnterFor.traverseBottomUp(visitor);
        if(DesAdd!=null) DesAdd.traverseBottomUp(visitor);
        if(ReturnToCondition!=null) ReturnToCondition.traverseBottomUp(visitor);
        if(ConAdd!=null) ConAdd.traverseBottomUp(visitor);
        if(ReturnToDes!=null) ReturnToDes.traverseBottomUp(visitor);
        if(DesAddSec!=null) DesAddSec.traverseBottomUp(visitor);
        if(GoToStatement!=null) GoToStatement.traverseBottomUp(visitor);
        if(ForStatement!=null) ForStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("For(\n");

        if(EnterFor!=null)
            buffer.append(EnterFor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesAdd!=null)
            buffer.append(DesAdd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ReturnToCondition!=null)
            buffer.append(ReturnToCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConAdd!=null)
            buffer.append(ConAdd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ReturnToDes!=null)
            buffer.append(ReturnToDes.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesAddSec!=null)
            buffer.append(DesAddSec.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GoToStatement!=null)
            buffer.append(GoToStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForStatement!=null)
            buffer.append(ForStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [For]");
        return buffer.toString();
    }
}
