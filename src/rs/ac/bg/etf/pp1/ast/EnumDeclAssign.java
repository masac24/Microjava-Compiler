// generated with ast extension for cup
// version 0.8
// 7/2/2026 15:24:2


package rs.ac.bg.etf.pp1.ast;

public class EnumDeclAssign extends EnumDecl {

    private EnumName EnumName;
    private EnumAtrAssign EnumAtrAssign;
    private AdditionalAssignNum AdditionalAssignNum;

    public EnumDeclAssign (EnumName EnumName, EnumAtrAssign EnumAtrAssign, AdditionalAssignNum AdditionalAssignNum) {
        this.EnumName=EnumName;
        if(EnumName!=null) EnumName.setParent(this);
        this.EnumAtrAssign=EnumAtrAssign;
        if(EnumAtrAssign!=null) EnumAtrAssign.setParent(this);
        this.AdditionalAssignNum=AdditionalAssignNum;
        if(AdditionalAssignNum!=null) AdditionalAssignNum.setParent(this);
    }

    public EnumName getEnumName() {
        return EnumName;
    }

    public void setEnumName(EnumName EnumName) {
        this.EnumName=EnumName;
    }

    public EnumAtrAssign getEnumAtrAssign() {
        return EnumAtrAssign;
    }

    public void setEnumAtrAssign(EnumAtrAssign EnumAtrAssign) {
        this.EnumAtrAssign=EnumAtrAssign;
    }

    public AdditionalAssignNum getAdditionalAssignNum() {
        return AdditionalAssignNum;
    }

    public void setAdditionalAssignNum(AdditionalAssignNum AdditionalAssignNum) {
        this.AdditionalAssignNum=AdditionalAssignNum;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumName!=null) EnumName.accept(visitor);
        if(EnumAtrAssign!=null) EnumAtrAssign.accept(visitor);
        if(AdditionalAssignNum!=null) AdditionalAssignNum.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumName!=null) EnumName.traverseTopDown(visitor);
        if(EnumAtrAssign!=null) EnumAtrAssign.traverseTopDown(visitor);
        if(AdditionalAssignNum!=null) AdditionalAssignNum.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumName!=null) EnumName.traverseBottomUp(visitor);
        if(EnumAtrAssign!=null) EnumAtrAssign.traverseBottomUp(visitor);
        if(AdditionalAssignNum!=null) AdditionalAssignNum.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumDeclAssign(\n");

        if(EnumName!=null)
            buffer.append(EnumName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumAtrAssign!=null)
            buffer.append(EnumAtrAssign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AdditionalAssignNum!=null)
            buffer.append(AdditionalAssignNum.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumDeclAssign]");
        return buffer.toString();
    }
}
