// generated with ast extension for cup
// version 0.8
// 7/2/2026 15:24:2


package rs.ac.bg.etf.pp1.ast;

public class AddEnumAssifn extends AdditionalAssignNum {

    private AdditionalAssignNum AdditionalAssignNum;
    private EnumAtrAssign EnumAtrAssign;

    public AddEnumAssifn (AdditionalAssignNum AdditionalAssignNum, EnumAtrAssign EnumAtrAssign) {
        this.AdditionalAssignNum=AdditionalAssignNum;
        if(AdditionalAssignNum!=null) AdditionalAssignNum.setParent(this);
        this.EnumAtrAssign=EnumAtrAssign;
        if(EnumAtrAssign!=null) EnumAtrAssign.setParent(this);
    }

    public AdditionalAssignNum getAdditionalAssignNum() {
        return AdditionalAssignNum;
    }

    public void setAdditionalAssignNum(AdditionalAssignNum AdditionalAssignNum) {
        this.AdditionalAssignNum=AdditionalAssignNum;
    }

    public EnumAtrAssign getEnumAtrAssign() {
        return EnumAtrAssign;
    }

    public void setEnumAtrAssign(EnumAtrAssign EnumAtrAssign) {
        this.EnumAtrAssign=EnumAtrAssign;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AdditionalAssignNum!=null) AdditionalAssignNum.accept(visitor);
        if(EnumAtrAssign!=null) EnumAtrAssign.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AdditionalAssignNum!=null) AdditionalAssignNum.traverseTopDown(visitor);
        if(EnumAtrAssign!=null) EnumAtrAssign.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AdditionalAssignNum!=null) AdditionalAssignNum.traverseBottomUp(visitor);
        if(EnumAtrAssign!=null) EnumAtrAssign.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AddEnumAssifn(\n");

        if(AdditionalAssignNum!=null)
            buffer.append(AdditionalAssignNum.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumAtrAssign!=null)
            buffer.append(EnumAtrAssign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AddEnumAssifn]");
        return buffer.toString();
    }
}
