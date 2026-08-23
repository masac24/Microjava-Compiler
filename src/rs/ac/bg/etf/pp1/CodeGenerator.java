package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import rs.ac.bg.etf.pp1.CounterVisitor.VarArrayCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarIdentCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	
	private int mainPc;
	
	public int getMainPc() {
		return mainPc;
	}

	CodeGenerator(){
		Obj ord = Tab.find("ord");
		ord.setAdr(Code.pc);
		Obj chr = Tab.find("chr");
		chr.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Obj len = Tab.find("len");
		len.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.put(Code.exit);
		Code.put(Code.return_);
	
	}
	
	public List<Obj> classes = new ArrayList<Obj>();
	public HashMap<Struct, Integer> TVF = new HashMap<Struct, Integer>();
	public HashMap<Struct, List<Integer>> toPatch = new HashMap<Struct, List<Integer>>();
	Obj classArray;
	
	public void visit(ProgName prog) {
		classArray = new Obj(Obj.Var, "classArray", Tab.intType);
	}
	
	public void visit(ClassDecl decl) {
		Obj cls = decl.getClassName().obj;
		classes.add(cls);
		if (cls.getType().getElemType() != null) {
	        Collection<Obj> meth = cls.getType().getMembers();
	        for (Obj o : meth) {
	            if (o.getKind() == Obj.Meth && o.getAdr() == 0) {
	                for (Obj parentMethod : cls.getType().getElemType().getMembers()) {
	                    if (parentMethod.getKind() == Obj.Meth && 
	                        o.getName().equals(parentMethod.getName())) {
	                        o.setAdr(parentMethod.getAdr());
	                        break;
	                    }
	                }
	            }
	        }
		}
	}
	
	public void visit(AbstractClassDecl decl) {
		Obj cls = decl.getAbstractClassName().obj;
		classes.add(cls);
	}
	
	public void generateTVF() {
		for(Obj cls : classes) {
			TVF.put(cls.getType(), Code.dataSize);
			List<Integer> adrs = toPatch.get(cls.getType());
			if(adrs != null) {
				for(Integer a : adrs) {
					Code.put2(a, Code.dataSize);
				}
			}
			for(Obj meth : cls.getType().getMembers()) {
				if(meth.getKind() == Obj.Meth) {
					String methName = meth.getName();
					for(int i = 0; i < methName.length(); i++) {
						Code.loadConst(methName.charAt(i));
						Code.put(Code.putstatic);
						Code.put2(Code.dataSize++);
					}
					Code.loadConst(-1);
					Code.put(Code.putstatic);
					Code.put2(Code.dataSize++);
					Code.loadConst(meth.getAdr());
					Code.put(Code.putstatic);
					Code.put2(Code.dataSize++);
				}
			}
			Code.loadConst(-2);
			Code.put(Code.putstatic);
			Code.put2(Code.dataSize++);
		}
	}
	
	public void visit(Print print) {
		if(print.getNumAdd() instanceof WithoutNumber) 
			Code.loadConst(5);
		if(print.getExpr().struct == Tab.intType) {
			Code.put(Code.print);
		} else {
			Code.put(Code.bprint);
		}
	}
	
	public void visit(WithNumber num) {
		Code.loadConst(num.getN1());
	}
	
	public void visit(Read read) {
		if(read.getDesignator().obj.getType() == Tab.intType) {
			Code.put(Code.read);
		} else {
			Code.put(Code.bread);
		}
		Code.store(read.getDesignator().obj);
	}
	
	public void visit(NumFactor num) {
		Obj con = Tab.insert(Obj.Con, "$", num.struct);
		con.setLevel(0);
		con.setAdr(num.getValue());
		
		Code.load(con);
	}
	
	public void visit(CharFactor num) {
		Obj con = Tab.insert(Obj.Con, "$", num.struct);
		con.setLevel(0);
		con.setAdr(num.getValue());
		Code.load(con);
	}
	
	public void visit(BoolFactor num) {
		Obj con = Tab.insert(Obj.Con, "$", num.struct);
		con.setLevel(0);
		con.setAdr(num.getValue());
		
		Code.load(con);
	}
	
	Obj currentMethod = null;
	
	public void visit(MethodTypeName meth) {
		if(meth.getParent() instanceof AbstractMethodParams || meth.getParent() instanceof AbstractMethodNoParams)
			return;
		currentMethod = meth.obj;
		meth.obj.setAdr(Code.pc);
		SyntaxNode methnode = meth.getParent();
		
		if("main".equalsIgnoreCase(meth.getMethName())) {
			mainPc = Code.pc;
			generateTVF();
		}
		
		VarArrayCounter vararrcnt = new VarArrayCounter();
		methnode.traverseTopDown(vararrcnt);
		
		VarIdentCounter varcnt = new VarIdentCounter();
		methnode.traverseTopDown(varcnt);
		
//		FormParamCounter formcnt = new FormParamCounter();
//		methnode.traverseTopDown(formcnt);
//		
//		FormParamArrayCounter formarrcnt = new FormParamArrayCounter();
//		methnode.traverseTopDown(formarrcnt);
		
		Code.put(Code.enter);
		Code.put(meth.obj.getLevel());
		Code.put(meth.obj.getLevel()+vararrcnt.getCount()+varcnt.getCount());
	
	}
	
	
	public void visit(MethodWithParams meth) {
		currentMethod = null;
		 if(meth.obj.getType() == Tab.noType) {
	        Code.put(Code.exit);
	        Code.put(Code.return_);
	    }
	}
	public void visit(MethodNoParams meth) {
		currentMethod = null;
		 if(meth.obj.getType() == Tab.noType) {
	        Code.put(Code.exit);
	        Code.put(Code.return_);
	    }
	}
	
	
	public void visit(DesAssign assign) {
		Obj des = assign.getDesignator().obj;
		if(des.getKind() == Obj.Elem) {
			if(des.getType().getKind() == Struct.Class) {
				Code.put(Code.bastore);
			}else
				Code.put(Code.astore);
		} else if(des.getKind() == Obj.Fld) {
			Code.put(Code.putfield);
			Code.put2(des.getAdr());
		}
		else
			Code.store(des);
		
	}
	
	
	
	public void visit(ElemAtribute des) {
		SyntaxNode parent = des.getParent().getParent();
		if(DesAssign.class != parent.getClass() &&
				parent.getClass() != Inc.class && parent.getClass() != Dec.class && parent.getClass() != Read.class) {
			if(des.obj.getType().getKind() == Struct.Class)
				Code.put(Code.baload);
			else
				Code.put(Code.aload);
		}
		if(des.getParent() instanceof IdentAtribute) {
			IdentAtribute idatr = (IdentAtribute)des.getParent();
			if(idatr.obj.getKind() == Obj.Meth) {
				Code.put(Code.dup);
				
				Code.store(classArray);
			}
				
		}
		
	}
	
	public void visit(DesignatorName des) {
		SyntaxNode parent = des.getParent().getParent();
		if(des.obj.getKind() == Obj.Type) return;
		if(des.obj.getKind() == Obj.Fld) {
		    Obj thisObj = new Obj(Obj.Var, "this", Tab.noType);
		    thisObj.setAdr(0);
		    thisObj.setLevel(1);
		    Code.load(thisObj);
		    
		    if(DesAssign.class != parent.getClass() && 
		       parent.getClass() != Inc.class && 
		       parent.getClass() != Dec.class) {
		        Code.put(Code.getfield);
		        Code.put2(des.obj.getAdr());
		    }
		    return;
		}
		
		if((des.obj.getKind() == Obj.Meth || des.obj.getKind() == 7) && 
		   ((Designator)des.getParent()).getDesignatorList() instanceof NoDesignators) {
		    if(isClassParam((Designator)des.getParent())) {
		        Code.load(getBase((Designator)des.getParent()));
		    }
		    return;  
		}
		
		if((DesAssign.class == parent.getClass() || parent.getClass() == NoActPars.class || parent.getClass() == FuncCall.class
				|| parent.getClass() == WithActPars.class || parent.getClass() == FuncCallParams.class) 
				&& isClassParam((Designator)des.getParent())) {
			Code.load(des.obj);return;
		}
		Designator designator = (Designator)des.getParent();
		if(designator.obj.getKind() == Obj.Fld && parent.getClass() == Read.class) {Code.load(des.obj); return;}
		if(designator.obj.getKind() == Obj.Elem ) {Code.load(des.obj); return;}
		if(DesAssign.class != parent.getClass() && FuncCall.class != parent.getClass() 
				&& parent.getClass() != NoActPars.class && parent.getClass() != Read.class) {
			Code.load(des.obj);
		}
	}
	
	
	public void visit(IdentAtribute ident) {
		if (ident.obj.getKind() == Obj.Meth) {
	        return;
	    }
		if(ident.obj.getKind() == Obj.Con) {
			Code.load(ident.obj); return;
		}
	    if(ident.obj.getKind() == Obj.Fld) {
	    	SyntaxNode parent = ident.getParent().getParent();
	        if (parent instanceof DesAssign) {
	            return;
	        }
	        if ( parent instanceof Read) {
	            return;
	        }
	        if(parent instanceof Inc || parent instanceof Dec) {
	        	Code.put(Code.dup);
	        }
			
		    Code.put(Code.getfield);
		    Code.put2(ident.obj.getAdr());
	    }
		
		
	}

	
	public void visit(NewTypeWithExprFactor newt) {
		Code.put(Code.newarray);
		if(newt.struct == Tab.charType) {
	        Code.put(0);  
	    } else {
	        Code.put(1); 
	    }
	}
	

	public void visit(NewTypeFactor newt) {
		Struct type = newt.getType().struct;
		
		Code.put(Code.new_);
	    Code.put2((type.getNumberOfFields()+1)*4);
	    Code.put(Code.dup); 
	    Integer adr = TVF.get(type);
	    if(adr == null) {
	    	Code.loadConst(0);
	    	if(!toPatch.containsKey(type))
	    		toPatch.put(type, new ArrayList<Integer>());
	    	toPatch.get(type).add(Code.pc - 4); 
	    } else
	    	Code.loadConst(TVF.get(type)); 
	    Code.put(Code.putfield); 
	    Code.put2(0);
	}
	
	public void visit(LengthAtribute len) {
		Code.put(Code.arraylength);
	}
	
	public void visit(Inc inc) {
		if(inc.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
			Code.put(Code.aload);
		}
		Code.put(Code.const_1);
		Code.put(Code.add);
		Code.store(inc.getDesignator().obj);
	}
	
	public void visit(Dec dec) {
		if(dec.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
			Code.put(Code.aload);
		}
		Code.put(Code.const_1);
		Code.put(Code.sub);
		Code.store(dec.getDesignator().obj);
	}
	
	private boolean isClassParam(Designator des) {
		for (Obj local : des.obj.getLocalSymbols()) {
	        if ("this".equals(local.getName()) && local.getAdr() == 0) {
	            return true;  
	        }
	    }
		DesignatorList list = des.getDesignatorList();
        return list instanceof IdentAtribute;
	}
	
	public Obj getBase(Designator des) {
		DesignatorList designator = des.getDesignatorList();
		Obj base = des.getDesignatorName().obj;
		if(base.getKind() == Obj.Meth || base.getKind() == 7) {
			if(currentMethod != null) {
				Collection<Obj>fields = currentMethod.getLocalSymbols();
				for(Obj o : fields) {
					if(o.getName().equals("this")) return o;
				}
			}
			
		}
		if(!(designator instanceof IdentAtribute)) return base;
		IdentAtribute idatr = (IdentAtribute)designator;
		if(!(idatr.getDesignatorList() instanceof NoDesignators))
			base = idatr.getDesignatorList().obj;
		

		return base;

	}
	
	
	public void visit(FuncCall funccall) {
		Obj func = funccall.getDesignator().obj;
		if(isClassParam(funccall.getDesignator())) {
			Code.put(Code.dup);
			Code.put(Code.getfield);
			Code.put2(0);
			Code.put(Code.invokevirtual);
			String name= func.getName();
			for(int i = 0; i < name.length() ; i++) {
				Code.put4(name.charAt(i));
			}
			Code.put4(-1);
		} else {
			int offset = func.getAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
		}
		
	}
	
	public void visit(NoActPars funccall) {
		Obj func = funccall.getDesignator().obj;
		if(isClassParam(funccall.getDesignator())) {
			Code.put(Code.dup);
			Code.put(Code.getfield);
			Code.put2(0);
			Code.put(Code.invokevirtual);
			String name= func.getName();
			for(int i = 0; i < name.length() ; i++) {
				Code.put4(name.charAt(i));
			}
			Code.put4(-1);
		} else {
			int offset = func.getAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
		}
		if(funccall.getDesignator().obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
	}
	
	public void visit(FuncCallParams funccall) {
		Obj func = funccall.getDesignator().obj;
		
		if(isClassParam(funccall.getDesignator())) {
			Obj obj = getBase(funccall.getDesignator());
			if (obj.getKind() != Obj.Elem) {
			    Code.load(obj);
			}else
				Code.load(classArray);
			Code.put(Code.getfield);
			Code.put2(0);
			Code.put(Code.invokevirtual);
			String name= func.getName();
			
			for(int i = 0; i < name.length() ; i++) {
				Code.put4(name.charAt(i));
			}
			Code.put4(-1);
		} else {
			int offset = func.getAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
		}
		
	}
	
	public void visit(WithActPars funccall) {
		Obj func = funccall.getDesignator().obj;
		if(isClassParam(funccall.getDesignator())) {
			Obj obj = getBase(funccall.getDesignator());
			if (obj.getKind() != Obj.Elem) {
			    Code.load(obj);
			}else
				Code.load(classArray);
			Code.put(Code.getfield);
			Code.put2(0);
			Code.put(Code.invokevirtual);
			String name= func.getName();
			
			System.out.println();
			for(int i = 0; i < name.length() ; i++) {
				Code.put4(name.charAt(i));
			}
			Code.put4(-1);
		} else {
			int offset = func.getAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
		}
		if(funccall.getDesignator().obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
		
	}
	
	public void visit(ReturnExpr returnexpr) {
		if(!switchStartIndex.isEmpty()) {
			Code.put(Code.pop);
		}
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(MinusTerm min) {
		if(!switchStartIndex.isEmpty()) {
			Code.put(Code.pop);
		}
		Code.put(Code.neg);
	}
	
	public void visit(Return returnexpr) {
		if(!switchStartIndex.isEmpty()) {
			Code.put(Code.pop);
		}
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(MultipleTerms add) {
		if(add.getAddop() instanceof PlusOp)
			Code.put(Code.add);
		if(add.getAddop() instanceof MinusOp)
			Code.put(Code.sub);
	}
	public void visit(MultipleFactors add) {
		if(add.getMulop() instanceof MultiplyOp)
			Code.put(Code.mul);
		if(add.getMulop() instanceof DivideOp)
			Code.put(Code.div);
		if(add.getMulop() instanceof ModOp)
			Code.put(Code.rem);
	}
	

	
	public int getRel(Relop relop) {
		if(relop instanceof EqualOp)
			return Code.eq;
		if(relop instanceof NotEqualOp)
			return Code.ne;
		if(relop instanceof GreaterOp)
			return Code.gt;
		if(relop instanceof GreqOp)
			return Code.ge;
		if(relop instanceof LessOp)
			return Code.lt;
		if(relop instanceof LeqOp)
			return Code.le;
		return -1;
	}
	
	List<Integer> wrongCondFact = new ArrayList<Integer>();
	List<Integer> skipToThen = new ArrayList<Integer>();
	List<Integer> skipToElse = new ArrayList<Integer>();
	List<Integer> skipToAfter = new ArrayList<Integer>();
	
	public void visit(CondExpr cond) {
		Code.loadConst(0);
		Code.putFalseJump(Code.ne, 0); 
		wrongCondFact.addLast(Code.pc - 2);
	}
	
	public void visit(RelationCond cond) {
		Code.putFalseJump(getRel(cond.getRelop()), 0);
		wrongCondFact.addLast(Code.pc - 2);
	}
	
	public void visit(CondTerm cond) {
		Code.putJump(0);	
		skipToThen.addLast(Code.pc - 2);
		while(!wrongCondFact.isEmpty()) {
			Code.fixup(wrongCondFact.removeLast());
		}
	}
	
	public void visit(Condition cond) {
		Code.putJump(0); // netacno baci na else ili gde vec
		skipToElse.addLast(Code.pc - 2);
		while(!skipToThen.isEmpty()) {
			Code.fixup(skipToThen.removeLast());
		}
	}
	
	List<Integer> skipTernary = new ArrayList<Integer>();
	
	public void visit(TernaryColon colon) {
		Code.putJump(0);
		skipTernary.addLast(Code.pc - 2);
		Code.fixup(skipToElse.removeLast());
	}
	
	public void visit(TernaryDef def) {
		Code.fixup(skipTernary.removeLast());
	}
	
	public void visit(IfStatement stmt) {
		Code.fixup(skipToElse.removeLast());
		
	}
	
	public void visit(Else stmt) {
		Code.putJump(0);
		skipToAfter.addLast(Code.pc - 2);
		Code.fixup(skipToElse.removeLast());
		
	}
	
	public void visit(IfElseStatement stmt) {
		Code.fixup(skipToAfter.removeLast());
	}
	
	public void visit(EnterFor forStmt) {  
	    broken.add(new ArrayList<>());
	}
	
	public void visit(For stmt) {
		desAddr.removeLast();
		condAddr.removeLast();
		while(!broken.getLast().isEmpty()) 
	    	Code.fixup(broken.getLast().removeLast());
		broken.removeLast();
		if(!skipToElse.isEmpty())Code.fixup(skipToElse.removeLast()); // kada uslov nije ispunjen condition izbaci netacne i hvataju se ovde
	}
	
	//kada je uslov tacan condAdd treba da baci tacno na statement a onda iz statementa da se vrati na drugi des add a onda na cond add 
	List<Integer> skipToStatement = new ArrayList<Integer>();
	List<Integer> returnToCondition = new ArrayList<Integer>();
	List<Integer> returnToDes = new ArrayList<Integer>();
	
	public void visit(ForCond cond) {
		Code.putJump(0);
		skipToStatement.add(Code.pc - 2);
	}
	
	public void visit(ForNoCond cond) {
		Code.putJump(0);
		skipToStatement.add(Code.pc - 2);
	}
	
	List<Integer> condAddr = new ArrayList<Integer>();
	
	public void visit(ReturnToCondition cond) {
		if(condAddr.contains(Code.pc)) {
			Code.fixup(returnToCondition.removeLast());
		}else
			condAddr.addLast(Code.pc);
	}
	
	List<Integer> desAddr = new ArrayList<Integer>();
	
	public void visit(ReturnToDes ret) {
		if(desAddr.contains(Code.pc)) {
			Code.fixup(returnToDes.removeLast());
		}else
			desAddr.addLast(Code.pc);
	}
	
	public void visit(DesSec des) {
		Code.putJump(condAddr.getLast());
		returnToCondition.add(Code.pc - 2);
	}
	
	public void visit(NoDesSec des) {
		Code.putJump(condAddr.getLast());
		returnToCondition.add(Code.pc - 2);
	}
	
	public void visit(ForStatement stmt) {
		Code.putJump(desAddr.getLast());
		returnToDes.add(Code.pc - 2);
	}
	
	public void visit(GoToStatement stmt) {
		Code.fixup(skipToStatement.removeLast());
	}
	
	public void visit(Continue cont) {
		if(!switchStartIndex.isEmpty()) {
			Code.put(Code.pop);
		} //ovo je dodato
		Code.putJump(desAddr.getLast());
		returnToDes.add(Code.pc - 2);
	}
	
	public void visit(Switch sw) {
		switchStartIndex.removeLast();
	    Code.fixup(nextCase.removeLast());
	    Code.fixup(nextStmt.removeLast());
	    while(!broken.getLast().isEmpty()) 
	    	Code.fixup(broken.getLast().removeLast());
	    broken.removeLast();
		Code.put(Code.pop);
	}
	
	public void visit(EnterSwitch sw) {
		switchStartIndex.add(nextCase.size()); 
		broken.add(new ArrayList<Integer>());
	}
	

	List<Integer> nextCase = new ArrayList<Integer>();
	List<Integer> nextStmt = new ArrayList<Integer>();
	List<Integer> switchStartIndex = new ArrayList<>(); 
	
	public void visit(CaseValue val) {
		
		int startIdx = switchStartIndex.getLast();

		 if(nextCase.size() > startIdx) {
	        Code.fixup(nextCase.removeLast());
	    }
	    
	    Code.put(Code.dup);
	    Obj con = Tab.insert(Obj.Con, "$", Tab.intType);
	    con.setLevel(0);
	    con.setAdr(val.getN1());
	    Code.load(con);
	    
	    Code.putFalseJump(Code.eq, 0);
	    nextCase.add(Code.pc - 2);
	}
	
	public void visit(CaseStmt stmt) {
		if(!nextStmt.isEmpty()) {
			Code.fixup(nextStmt.removeLast());
		}
		
	}
	public void visit(Case cas) {
		Code.putJump(0);
		nextStmt.add(Code.pc - 2);
	}
	
	List<List<Integer>> broken = new ArrayList<List<Integer>>();

	public void visit(Break br) {
		Code.putJump(0);
		broken.getLast().add(Code.pc - 2);
	}
	
	
	
}
