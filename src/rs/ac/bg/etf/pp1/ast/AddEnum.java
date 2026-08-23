// generated with ast extension for cup
// version 0.8
// 7/2/2026 15:24:2


package rs.ac.bg.etf.pp1.ast;

public class AddEnum extends AdditionalAssignNum {

    private AdditionalAssignNum AdditionalAssignNum;
    private EnumAtrDec EnumAtrDec;

    public AddEnum (AdditionalAssignNum AdditionalAssignNum, EnumAtrDec EnumAtrDec) {
        this.AdditionalAssignNum=AdditionalAssignNum;
        if(AdditionalAssignNum!=null) AdditionalAssignNum.setParent(this);
        this.EnumAtrDec=EnumAtrDec;
        if(EnumAtrDec!=null) EnumAtrDec.setParent(this);
    }

    public AdditionalAssignNum getAdditionalAssignNum() {
        return AdditionalAssignNum;
    }

    public void setAdditionalAssignNum(AdditionalAssignNum AdditionalAssignNum) {
        this.AdditionalAssignNum=AdditionalAssignNum;
    }

    public EnumAtrDec getEnumAtrDec() {
        return EnumAtrDec;
    }

    public void setEnumAtrDec(EnumAtrDec EnumAtrDec) {
        this.EnumAtrDec=EnumAtrDec;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AdditionalAssignNum!=null) AdditionalAssignNum.accept(visitor);
        if(EnumAtrDec!=null) EnumAtrDec.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AdditionalAssignNum!=null) AdditionalAssignNum.traverseTopDown(visitor);
        if(EnumAtrDec!=null) EnumAtrDec.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AdditionalAssignNum!=null) AdditionalAssignNum.traverseBottomUp(visitor);
        if(EnumAtrDec!=null) EnumAtrDec.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AddEnum(\n");

        if(AdditionalAssignNum!=null)
            buffer.append(AdditionalAssignNum.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumAtrDec!=null)
            buffer.append(EnumAtrDec.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AddEnum]");
        return buffer.toString();
    }
}
