package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;

public class CounterVisitor extends VisitorAdaptor {
	
	
	protected int count;
	
	public int getCount() {
		return count;
	}
	
	public static class VarIdentCounter extends CounterVisitor {
		
		public void visit(VarNameIdent vardecl) {
			count++;
		}
	}
	
	public static class VarArrayCounter extends CounterVisitor {
		
		public void visit(VarNameArray vardecl) {
			count++;
		}
	}
	
	public static class FormParamArrayCounter extends CounterVisitor {
		
		public void visit(FormParsArray vardecl) {
			count++;
		}
	}
	
	public static class FormParamCounter extends CounterVisitor {
		
		public void visit(FormParsIdent vardecl) {
			count++;
		}
	}
	
}
