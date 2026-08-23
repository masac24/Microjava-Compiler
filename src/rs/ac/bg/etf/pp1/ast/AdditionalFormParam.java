// generated with ast extension for cup
// version 0.8
// 7/2/2026 15:24:2


package rs.ac.bg.etf.pp1.ast;

public class AdditionalFormParam extends AdditionalParams {

    private FormParsItem FormParsItem;
    private AdditionalParams AdditionalParams;

    public AdditionalFormParam (FormParsItem FormParsItem, AdditionalParams AdditionalParams) {
        this.FormParsItem=FormParsItem;
        if(FormParsItem!=null) FormParsItem.setParent(this);
        this.AdditionalParams=AdditionalParams;
        if(AdditionalParams!=null) AdditionalParams.setParent(this);
    }

    public FormParsItem getFormParsItem() {
        return FormParsItem;
    }

    public void setFormParsItem(FormParsItem FormParsItem) {
        this.FormParsItem=FormParsItem;
    }

    public AdditionalParams getAdditionalParams() {
        return AdditionalParams;
    }

    public void setAdditionalParams(AdditionalParams AdditionalParams) {
        this.AdditionalParams=AdditionalParams;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsItem!=null) FormParsItem.accept(visitor);
        if(AdditionalParams!=null) AdditionalParams.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsItem!=null) FormParsItem.traverseTopDown(visitor);
        if(AdditionalParams!=null) AdditionalParams.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsItem!=null) FormParsItem.traverseBottomUp(visitor);
        if(AdditionalParams!=null) AdditionalParams.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AdditionalFormParam(\n");

        if(FormParsItem!=null)
            buffer.append(FormParsItem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AdditionalParams!=null)
            buffer.append(AdditionalParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AdditionalFormParam]");
        return buffer.toString();
    }
}
