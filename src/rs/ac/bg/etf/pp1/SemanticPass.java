package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.structure.HashTableDataStructure;

public class SemanticPass extends VisitorAdaptor {

	int printCallCount = 0;
	int varDeclCount = 0;
	Obj currentMethod = null;
	int constant;
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	Struct currentType = null;
	Struct constantType = null;
	Struct returnType = null;
	int scope = 0;

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	// ----------------- PROGRAM -------------------------------

	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
	}

	public void visit(Program program) {
		if (mainMeth == null || mainMeth.getLevel() > 0) {
			report_error("Program ne poseduje adekvatnu main metodu", null);
			return;
		}
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	// ------------------ VAR DECLARATION ------------------------------------
	
	int field = 1;

	public void visit(VarNameIdent vardecl) {
		Obj node = null;
		if (currentMethod != null || currentClass != null)
			node = Tab.currentScope().findSymbol(vardecl.getVarname());
		else
			node = Tab.find(vardecl.getVarname());
		if (node == null || node == Tab.noObj) {
			report_info("Deklarisana promenljiva " + vardecl.getVarname(), vardecl);
			if (currentMethod != null) {
				Tab.insert(Obj.Var, vardecl.getVarname(), currentType);
			} else if (currentClass != null) {
				Obj var = Tab.insert(Obj.Fld, vardecl.getVarname(), currentType);
				var.setAdr(field++);
				var.setLevel(scope);
			} else {
				Tab.insert(Obj.Var, vardecl.getVarname(), currentType);
			}
		} else {
			report_error("Vec postoji deklaracija simbola: " + vardecl.getVarname(), null);

		}
	}

	public void visit(VarNameArray vardecl) {
		Obj node = null;
		if (currentMethod != null || currentClass != null)
			node = Tab.currentScope().findSymbol(vardecl.getVarname());
		else
			node = Tab.find(vardecl.getVarname());
		if (node == null || node == Tab.noObj) {
			report_info("Deklarisana promenljiva " + vardecl.getVarname(), vardecl);
			Struct str = new Struct(3, currentType);
			if (currentMethod != null) {
				Tab.insert(Obj.Var, vardecl.getVarname(), str);
			} else if (currentClass != null) {
				Obj var = Tab.insert(Obj.Fld, vardecl.getVarname(), str);
				var.setAdr(field++);
				var.setLevel(scope);
			} else {
				Tab.insert(Obj.Var, vardecl.getVarname(), str);
			}
		} else {
			report_error("Vec postoji deklaracija simbola: " + vardecl.getVarname(), null);
		}
	}

	// ----------------- CONSTS -----------------------------------

	public void visit(NumConst num) {
		constantType = Tab.intType;
		constant = num.getValue();
	}

	public void visit(CharConst num) {
		constantType = Tab.charType;
		constant = num.getValue();

	}

	public void visit(BoolConst num) {
		constantType = Tab.find("bool").getType();
		constant = num.getValue();
	}

	// ------------------- CONST DECLARATION -----------------------------

	public void visit(ConstTypeName constdecl) {
		Obj node = Tab.currentScope().findSymbol(constdecl.getConstname());
		if (node != null) {
			report_error("Vec postoji deklaracija simbola: " + constdecl.getConstname(), null);
		} else {
			if (!constantType.assignableTo(currentType)) {
				report_error("Konstanta nije odgovarajuceg tipa ", null);
			} else {
				report_info("Deklarisana konstanta " + constdecl.getConstname(), constdecl);
				Obj varNode = Tab.insert(Obj.Con, constdecl.getConstname(), currentType);
				varNode.setAdr(constant);
				varNode.setLevel(scope);
			}
		}
	}

	// ------------------------- ENUM DECLARATION ----------------------------

	int lastEnumConst = -1;
	Struct Enum = null;
	String enumName = "";
	HashTableDataStructure currentHash = null;

	public void visit(EnumName enumdecl) {
		report_info("Deklarisanje enum " + enumdecl.getEnumName(), enumdecl);
		Struct currEnum = new Struct(6);
		enumName = enumdecl.getEnumName();
		Tab.insert(Obj.Type, enumdecl.getEnumName(), currEnum);
		HashTableDataStructure hash = new HashTableDataStructure();
		Enum = currEnum;
		currentHash = hash;
	}

	public void visit(EnumAtrAssign enumatr) {
		report_info("Deklarisana konstanta nabrajanja " + enumatr.getEnumAtrName(), enumatr);
		Obj enumConst = new Obj(Obj.Con, enumatr.getEnumAtrName(), Tab.intType);
		enumConst.setAdr(enumatr.getNum());
		lastEnumConst = enumatr.getNum();
		currentHash.insertKey(enumConst);
	}

	public void visit(EnumAtrDec enumatr) {
		report_info("Deklarisana konstanta nabrajanja " + enumatr.getEnumAtrName(), enumatr);
		Obj enumConst = new Obj(Obj.Con, enumatr.getEnumAtrName(), Tab.intType);
		if (lastEnumConst != -1)
			enumConst.setAdr(lastEnumConst + 1);
		else
			enumConst.setAdr(0);
		lastEnumConst = enumConst.getAdr();
		currentHash.insertKey(enumConst);
	}

	public void visit(EnumDeclaration enumdec) {
		Enum.setMembers(currentHash);
		lastEnumConst = -1;
		Enum = null;
		enumName = "";
		currentHash = null;
	}

	// -------------------- CLASS DECLARATION ---------------------------

	Obj currentClass = null;
	Obj extendClass = null;
	HashMap<String, Obj> methods = new HashMap<String, Obj>();
	List<Obj> abstractMethods = new ArrayList<Obj>();

	public void visit(ClassName classname) {
		report_info("Deklarisanje klase " + classname.getI1(), classname);
		Struct cl = new Struct(Struct.Class);
		currentClass = Tab.insert(Obj.Type, classname.getI1(), cl);
		classname.obj = currentClass;
		Tab.openScope();
		scope++;
	}

	public void visit(AbstractClassName classname) {
		report_info("Deklarisanje apstraktne klase " + classname.getI1(), classname);
		Struct cl = new Struct(8);
		currentClass = Tab.insert(Obj.Type, classname.getI1(), cl);
		classname.obj = currentClass;
		Tab.openScope();
		scope++;
	}

	boolean wasExtend = false;

	public void visit(WasExtend was) {
		wasExtend = true;
	}

	public void visit(Extends extd) {
		if (extendClass.getLevel() > 0) {
			report_error("Moguce je izvodjenje samo iz unutrasnje klase glavnog programa", extd);
		}
		currentClass.getType().setElementType(extendClass.getType());
		for (Obj member : extendClass.getType().getMembers()) {
			int kind = member.getKind();
			if (kind == Obj.Fld) {
	            Obj copiedField = Tab.insert(Obj.Fld, member.getName(), member.getType());
	            copiedField.setAdr(member.getAdr());  
	            field = Math.max(field, member.getAdr() + 1); 
	        }
			
			if (kind != Obj.Meth && kind != 7) {
				Tab.insert(kind, member.getName(), member.getType());
			}
			if (kind == Obj.Meth)
				methods.put(member.getName(), member);
			if (kind == 7)
				abstractMethods.add(member);
		}
	}

	public void visit(ClassDecl classdec) {
		for (Obj m : methods.values()) {
			Tab.insert(m.getKind(), m.getName(), m.getType());
			Tab.openScope();
			scope++;
			Collection<Obj> objs = m.getLocalSymbols();
			for (Obj o : objs) {
				Obj ob = Tab.insert(o.getKind(), o.getName(), o.getType());
				o.setAdr(ob.getAdr());
			}
			Tab.chainLocalSymbols(m);
			Tab.closeScope();
			scope--;
		}

		Tab.chainLocalSymbols(currentClass.getType());
		Tab.closeScope();
		scope--;
		currentClass = null;
		field = 1;
		extendClass = null;
		wasExtend = false;
		methods = new HashMap<String, Obj>();
		abstractMethods = new ArrayList<Obj>();
	}

	public void visit(AbstractClassDecl classdec) {
		Tab.chainLocalSymbols(currentClass.getType());
		Tab.closeScope();
		scope--;
		currentClass = null;
	}

	public void visit(ClassMethods met) {
		if (!abstractMethods.isEmpty()) {
			report_error("Neophodno je implementirati sve apstraktne funkcije natklase", met);
		}
	}

	// ------------------------- TYPES ----------------------

	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (currentClass != null && wasExtend && extendClass == null) {
			extendClass = typeNode;
		}
		if (typeNode == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! ", null);
			type.struct = Tab.noType;
		} else {
			if (Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();
				if (typeNode.getType().getKind() == Struct.Enum)
					type.struct = Tab.intType;
			} else {
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
				type.struct = Tab.noType;
			}
			currentType = type.struct;
		}
	}

	public void visit(ReturnVoid rtype) {
		returnType = Tab.noType;
	}

	public void visit(ReturnT ttype) {
		returnType = currentType;
	}

	// ----------------------- FUNCTION DECLARATION ---------------------------

	public void visit(FormParsIdent formident) {
		Obj node = null;
		node = Tab.currentScope().findSymbol(formident.getParamName());
		if (node == null || node == Tab.noObj) {
			report_info("Parametar funkcije " + formident.getParamName(), formident);
			Obj param = Tab.insert(Obj.Var, formident.getParamName(), formident.getType().struct);
			param.setFpPos(1);
			param.setLevel(scope);
			currentMethod.setLevel(currentMethod.getLevel() + 1);
		} else {
			report_error("Vec postoji parametar: " + formident.getParamName(), null);
		}

	}

	public void visit(FormParsArray formident) {
		Obj node = null;
		node = Tab.currentScope().findSymbol(formident.getParamName());
		if (node == null || node == Tab.noObj) {
			report_info("Parametar funkcije " + formident.getParamName(), formident);
			Struct str = new Struct(3);
			str.setElementType(formident.getType().struct);
			Obj param = Tab.insert(Obj.Var, formident.getParamName(), str);
			param.setFpPos(1);
			param.setLevel(scope);
			currentMethod.setLevel(currentMethod.getLevel() + 1);
		} else {
			report_error("Vec postoji parametar: " + formident.getParamName(), null);
		}
	}

	Obj mainMeth = null;
	
	public void visit(MethodNoParams meth) {
		meth.obj = meth.getMethodTypeName().obj;
	}
	
	public void visit(MethodWithParams meth) {
		meth.obj = meth.getMethodTypeName().obj;
	}

	public void visit(MethodTypeName methodTypeName) {
		Obj meth = null;
		if (currentClass == null) {
			meth = Tab.find(methodTypeName.getMethName());
			if (meth != Tab.noObj && meth != null && meth.getType().equals(returnType)) {
				report_error("Vec postoji funkcija", null);
				return;
			}
		} else {
			meth = Tab.currentScope().findSymbol(methodTypeName.getMethName());
			if (meth != Tab.noObj && meth != null && methods.containsKey(methodTypeName.getMethName())) {
				report_error("Vec postoji funkcija", null);
				return;
			}
		}
		if (currentClass != null && methods.containsKey(methodTypeName.getMethName())) {
			methods.remove(methodTypeName.getMethName());
		}
		if (methodTypeName.getParent() instanceof AbstractMethodNoParams
				|| methodTypeName.getParent() instanceof AbstractMethodParams) {
			currentMethod = Tab.insert(7, methodTypeName.getMethName(), returnType);
		} else
			currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethName(), returnType);
		if (methodTypeName.getMethName().equals("main"))
			mainMeth = currentMethod;

		methodTypeName.obj = currentMethod;
		Tab.openScope();
		scope++;
		report_info("Obradjuje se funkcija " + methodTypeName.getMethName(), methodTypeName);

		if (currentClass != null) {
			Tab.insert(Obj.Var, "this", currentClass.getType());
			currentMethod.setLevel(1);
		}
	}

	public void visit(MultipleMethodDecl methodDecl) {
		if (currentClass != null) {
			for (Obj o : abstractMethods) {
				if (o.getName().equals(currentMethod.getName())
						&& o.getType().compatibleWith(currentMethod.getType())) {
					abstractMethods.remove(o);
					break;
				}
			}
		}
		if (!returnFound && (currentMethod.getType() != Tab.noType)) {
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName()
					+ " nema return iskaz!", null);
		}
		report_info("Zavrsena obrada funkcije ", null);
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		scope--;
		returnFound = false;
		returnType = null;
		currentMethod = null;
	}

	public void visit(MultipleMethods meth) {
		if (!returnFound && currentMethod.getKind() != 7 && currentMethod.getType() != Tab.noType) {
			report_error("Semanticka greska na liniji " + meth.getLine() + ": funkcija " + currentMethod.getName()
					+ " nema return iskaz!", meth);
		}
		report_info("Zavrsena obrada funkcije ", meth);
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		scope--;
		returnFound = false;
		returnType = null;
		currentMethod = null;
	}

	// ----------------------- DESIGNATOR i DESIGNATOR STATEMENT-----------------------------

	private HashMap<String, Struct> polimorfni = new HashMap<String, Struct>();

	public void visit(DesAssign assignment) {
		int type = assignment.getDesignator().obj.getKind();
		if (type != 1 && type != 4 && type != 5) {
			report_error("Greska na liniji " + assignment.getLine() + " : "
					+ "designator mora biti promenljiva, element niza ili polje klase! ", null);
			return;
		}
		AssignR assignr = assignment.getAssignR();
		if(!(assignr instanceof AssignExpr)) {
			return;
		}
		Struct expr = ((AssignExpr)assignr).getExpr().struct;
		Obj des = assignment.getDesignator().obj;
		if ((des.getType().getKind() == Struct.Class || des.getType().getKind() == 8) && expr.getElemType() != null
				&& expr.getElemType().equals(des.getType())) {
			polimorfni.put(des.getName(), expr);
		}
		if (expr == null)
			return;
		if (!expr.assignableTo(des.getType()) && !des.getType().equals(expr.getElemType())) {
			report_error(
					"Greska na liniji " + assignment.getLine() + " : " + "nekompatibilni tipovi u dodeli vrednosti! ",
					null);
		} else {
			report_info("Dodeljena vrednost promenljivoj " + des.getName(), assignment);
		}
	}

	List<Obj> desArray = new ArrayList<Obj>();
	Obj currentDesignator = null;

	public void visit(DesignatorName designator) {
		Obj obj = Tab.find(designator.getName());
		if (obj == Tab.noObj || obj == null) {
			if (currentClass != null && extendClass != null) {
				Collection<Obj> objs = currentClass.getType().getElemType().getMembers();
				for (Obj o : objs) {
					if (o.getName().equals(designator.getName())) {
						obj = o;
						break;
					}
				}

			}
			if (obj == Tab.noObj || obj == null) {
				report_error("Greska na liniji " + designator.getLine() + " : ime " + designator.getName()
						+ " nije deklarisano! ", null);
				return;
			}
		}
		designator.obj = obj;
		currentDesignator = obj;
	}

	
	public void visit(IdentAtribute idatr) {
		Obj des = currentDesignator;
		int kind = des.getType().getKind();
		if (kind != Struct.Class && kind != Struct.Enum && kind != 8) {
			report_error("Moze se pristupati samo poljima klasa ili nabrajanja", idatr);
			return;
		}
		
		Collection<Obj> fields = des.getType().getMembers();
		if (currentClass != null && Tab.find(currentClass.getName()).getType() == des.getType())
			fields = Tab.currentScope().getOuter().getLocals().symbols();
		for (Obj c : fields) {
			if (c.getName().equals(idatr.getI2())) {
				idatr.obj = c;
				currentDesignator = idatr.obj;
				report_info("Dohvatanje polja", idatr);
				return;
			}
		}
		if (polimorfni.containsKey(des.getName())) {
			for (Obj c : polimorfni.get(des.getName()).getMembers()) {
				if (c.getName().equals(idatr.getI2())) {
					idatr.obj = c;
					currentDesignator = idatr.obj;
					report_info("Dohvatanje polja", idatr);
					return;
				}
			}
		}
		report_error("Ne postoji dato polje", idatr);
	}


	public void visit(LengthAtribute idatr) {
		Obj des = currentDesignator;
		int kind = des.getType().getKind();
		if (kind != Struct.Array) {
			report_error("Length se moze pozvati samo nad nizom!", idatr);
			return;
		}
		idatr.obj = new Obj(Obj.Con, "length", Tab.intType);
		currentDesignator = idatr.obj;
		report_info("Dohvatanje duzine niza", idatr);
	}

	public void visit(Designator des) {
		if (des.getDesignatorList().obj != Tab.noObj && des.getDesignatorList().obj != null)
			des.obj = des.getDesignatorList().obj;
		else {
			des.obj = des.getDesignatorName().obj;
		}
		currentDesignator = des.obj;
		if (des.obj.getKind() == Obj.Meth) {
			currentFuncCall.add(des.obj);
//			String name = des.obj.getName();
//			if(name == "chr" || name == "ord" || name == "len") {
//				paramCounter.add(1);
//				currentParamNumber.add(0);
//				return;
//			}
			Collection<Obj> members = des.obj.getLocalSymbols();
			currentParamNumber.add(des.obj.getLevel() - 1);
			for (Obj o : members) {
				if(o.getName().equals("this")) {
					paramCounter.add(2);
					return;
				}
			}
			paramCounter.add(1);
			
		}
	}
	
	public void visit(ArrayElem array) {
		desArray.add(currentDesignator);
	}

	public void visit(ElemAtribute arr) {
		Obj obj = desArray.removeLast();
		if (obj.getType().getElemType() == Tab.noType) {
			report_error("Neophodno je inicijalizovati niz", arr);
			return;
		}
		if (arr.getExpr().struct.getKind() != Struct.Int) {
			report_error("U [] mora biti int vrednost", arr);
			return;
		}
		Obj desObj = new Obj(Obj.Elem, obj.getName() + "$", obj.getType().getElemType());
		arr.obj = desObj;
		currentDesignator = desObj;
		report_info("Dohvatanje elementa niza", arr);
	}


	public void visit(NoActPars funcCall) {
		Obj func = funcCall.getDesignator().obj;
		if (Obj.Meth == func.getKind() && func.getKind() != 7) {
			report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine(), null);
			funcCall.struct = func.getType();
		} else {
			report_error("Greska na liniji " + funcCall.getLine() + " : ime " + func.getName() + " nije funkcija!",
					null);
			funcCall.struct = Tab.noType;
		}
	}

	public void visit(WithActPars funcCall) {
		Obj func = funcCall.getDesignator().obj;
		currentFuncCall.removeLast();
		int size = paramCounter.removeLast();
		int real = currentParamNumber.removeLast();
		if(size - 1 != real) {
			report_error("Uneli ste premalo parametara", funcCall);
			return;
		}
		if (Obj.Meth != func.getKind() && func.getKind() != 7) {
			report_error("Greska na liniji " + funcCall.getLine() + " : ime " + func.getName() + " nije funkcija!",
					null);
			funcCall.struct = Tab.noType;
			return;
		}
		report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine(), null);
		funcCall.struct = func.getType();
	}

	public void visit(Inc inc) {
		if (inc.getDesignator().obj.getType() != Tab.intType) {
			report_error("Greska na liniji " + inc.getLine() + " : " + "designator mora biti int! ", null);
			return;
		}
		int kind = inc.getDesignator().obj.getKind();
		if (kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld) {
			report_error("Greska na liniji " + inc.getLine() + " : "
					+ "designator mora biti promenljiva, element niza ili polje klase! ", null);
			return;
		}
		report_info("Incrementiranje", inc);
	}

	public void visit(Dec inc) {
		if (inc.getDesignator().obj.getType() != Tab.intType) {
			report_error("Greska na liniji " + inc.getLine() + " : " + "designator mora biti int! ", null);
			return;
		}
		int kind = inc.getDesignator().obj.getKind();
		if (kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld) {
			report_error("Greska na liniji " + inc.getLine() + " : "
					+ "designator mora biti promenljiva, element niza ili polje klase! ", null);
			return;
		}
		report_info("Dekrementiranje", inc);

	}

	// --------------------- STATEMENT -------------------------------

	public void visit(IfStatement ifs) {
		IfCondition ifc = ifs.getIfCondition();
		if(ifc instanceof ErrorCondition) {
			return;
		}
		RealCondition real = (RealCondition)ifc;
		if (real.getCondition().struct != boolType) {
			report_error("Tip uslova mora biti bool", ifs);
		}
	}

	public void visit(IfElseStatement ifs) {
		IfCondition ifc = ifs.getIfCondition();
		if(ifc instanceof ErrorCondition) {
			return;
		}
		RealCondition real = (RealCondition)ifc;
		if (real.getCondition().struct != boolType) {
			report_error("Tip uslova mora biti bool", ifs);
		}
	}

	public void visit(Return returnExpr) {
		returnFound = true;
		if (currentMethod == null)
			report_error(
					"Greska na liniji " + returnExpr.getLine() + " : " + "return se moze pozvati samo iz tela funkcije",
					null);
		Struct currMethType = currentMethod.getType();
		if (currMethType != Tab.noType) {
			report_error("Greska na liniji " + returnExpr.getLine() + " : "
					+ "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije "
					+ currentMethod.getName(), null);
		}
	}

	public void visit(For forstmt) {
		report_info("Gotova for petlja", forstmt);
		enteredFor--;
	}

	public void visit(ForCond conAdd) {
		Struct cond = conAdd.getCondition().struct;
		if (cond != boolType) {
			report_error("Tip uslova mora biti bool", conAdd);
		}
	}

	int enteredFor = 0;
	boolean enteredSwitch = false;

	public void visit(EnterFor enterfor) {
		enteredFor++;
		report_info("Obrada for petlje", enterfor);
	}

	public void visit(EnterSwitch sw) {
		enteredSwitch = true;
		switchcase.add(new ArrayList<Integer>());
		report_info("Obrada switch", sw);
	}

	List<List<Integer>> switchcase = new ArrayList<List<Integer>>();

	public void visit(Switch sw) {
		if (sw.getExpr().struct != Tab.intType) {
			report_error("Expression mora biti tipa int", sw);
		}
		switchcase.removeLast();
		enteredSwitch = !switchcase.isEmpty();
	}

	public void visit(Case cas) {
		int value = cas.getCaseValue().getN1();
		if (switchcase.getLast().contains(value)) {
			report_error("Vec postoji dati case", cas);
			return;
		}
		switchcase.getLast().add(value);
		report_info("Case " + value, cas);
	}

	public void visit(Continue cont) {
		if (enteredFor == 0) {
			report_error("Continue moze biti pozvan samo iz for petlje", cont);
			return;
		}
		report_info("Continue for petlje", cont);
	}

	public void visit(Break br) {
		if (enteredFor == 0 && !enteredSwitch) {
			report_error("Break moze biti pozvan samo iz for ili switch petlje", br);
			return;
		}
		report_info("Break", br);
	}

	public void visit(ReturnExpr returnExpr) {
		returnFound = true;
		if (currentMethod == null)
			report_error(
					"Greska na liniji " + returnExpr.getLine() + " : " + "return se moze pozvati samo iz tela funkcije",
					null);
		Struct currMethType = currentMethod.getType();
		if (!currMethType.compatibleWith(returnExpr.getExpr().struct))
			report_error("Greska na liniji " + returnExpr.getLine() + " : "
					+ "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije "
					+ currentMethod.getName(), null);
	}

	public void visit(Print print) {
		if ((print.getExpr().struct.getKind() != Struct.Int) && (print.getExpr().struct.getKind() != Struct.Char)
				&& (print.getExpr().struct != boolType))
			report_error("Tip koji se printuje mora biti int, char ili bool ", null);
		else
			report_info("Print promenljive", print);
	}

	public void visit(Read print) {
		Struct type = print.getDesignator().obj.getType();
		int kind = print.getDesignator().obj.getKind();
		if ((type != Tab.intType) && (type != Tab.charType) && (type != boolType)) {
			report_error("Tip koji se cita mora biti int, char ili bool ", null);
			return;
		}
		if (kind != Obj.Var && kind != Obj.Fld && kind != Obj.Elem) {
			report_error("Greska na liniji " + print.getLine() + " : "
					+ "designator mora biti promenljiva, element niza ili polje klase! ", null);
			return;
		}
		report_info("Citanje promenljive", print);
	}

	// -------------------------FACTOR -------------------------------

	public void visit(NumFactor cnst) {
		cnst.struct = Tab.intType;
	}

	public void visit(CharFactor cnst) {
		cnst.struct = Tab.charType;
	}

	public void visit(BoolFactor cnst) {
		cnst.struct = Tab.find("bool").getType();
	}

	public void visit(DesFactor var) {
		if (var.getDesignator().obj.getType().getKind() == Struct.Enum)
			var.struct = Tab.intType;
		else
			var.struct = var.getDesignator().obj.getType();
	}

	public void visit(NewTypeWithExprFactor newt) {
		if (newt.getExpr().struct.getKind() != Struct.Int) {
			report_error("Greska na liniji " + newt.getLine() + " : " + "tip expr mora biti int! ", null);
		}
		newt.struct = new Struct(Struct.Array, newt.getType().struct);
		currentDesignator.getType().setElementType(newt.getType().struct);
	}

	public void visit(NewTypeFactor newt) {
		int kind = newt.getType().struct.getKind();
		if (kind != Struct.Class && kind != 8) {
			report_error("Greska na liniji " + newt.getLine() + " : " + "expr mora biti klasa! ", null);
		}
		newt.struct = newt.getType().struct;
	}

	public void visit(ExprFactor fact) {
		fact.struct = fact.getExpr().struct;
	}

	public void visit(FuncCall funcCall) {
		Obj func = funcCall.getDesignator().obj;
		if (Obj.Meth == func.getKind() || func.getKind() == 7) {
			if (Tab.noType == func.getType()) {
				report_error("Greska na liniji " + funcCall.getLine()
						+ "Ne moze se koristiti u izrazima jer nema povratnu vrednost!", funcCall);
			} else {
				report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine(), null);
				funcCall.struct = func.getType();
			}
		} else {
			report_error("Greska na liniji " + funcCall.getLine() + " : ime " + func.getName() + " nije funkcija!",
					funcCall);
			funcCall.struct = Tab.noType;
		}
	}

	List<Obj> currentFuncCall = new ArrayList<Obj>();
	List<Integer> paramCounter = new ArrayList<Integer>();
	List<Integer> currentParamNumber = new ArrayList<Integer>();

	public void visit(FuncCallParams funcCall) {
		Obj func = funcCall.getDesignator().obj;
		currentFuncCall.removeLast();
		int size = paramCounter.removeLast();
		int real = currentParamNumber.removeLast();
		if(size - 1 != real ) {
			report_error("Uneli ste premalo parametara", funcCall);
			return;
		}
		if (Obj.Meth != func.getKind()) {
			report_error("Greska na liniji " + funcCall.getLine() + " : ime " + func.getName() + " nije funkcija!",
					null);
			funcCall.struct = Tab.noType;
			return;
		}
		report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine(), null);
		funcCall.struct = func.getType();
	}

	public void visit(MultipleExpr expr) {
		Struct param = expr.getExpr().struct;
		Obj curr = currentFuncCall.getLast();
		int paramc = paramCounter.removeLast();
		paramCounter.add(paramc + 1);
		Collection<Obj> params = curr.getLocalSymbols();
		for (Obj o : params) {
			if (o.getAdr() == paramc && o.getType().getKind() != param.getKind()) {
				report_error("Greska na liniji " + expr.getLine() + " : parametar " + o.getName()
						+ " nije odgovarajuceg tipa", null);
				return;
			}
			if (o.getAdr() == paramc && o.getType().getKind() == param.getKind()) {
				report_info("Validan " + paramc + " parametar " + o.getName() + " funkcije " + curr.getName()
						+ " na liniji " + expr.getLine(), null);
				return;
			}
		}
		report_error("Greska na liniji " + expr.getLine() + " : uneli ste vise parametara nego sto ih funkcija ima ",
				null);
	}
	
	public void visit(OneExpr expr) {
		Struct param = expr.getExpr().struct;
		Obj func = currentFuncCall.getLast();
		Collection<Obj> params = func.getLocalSymbols();
		boolean f = false;
		for (Obj o : params) {
			if (o.getAdr() == 0 && "this".equals(o.getName()))
				f = true;
			if (o.getAdr() == 0 && !f && o.getType().getKind() != param.getKind()) {
				report_error("Greska na liniji " + expr.getLine() + " : parametar nije odgovarajuceg tipa", null);
				return;
			}
			if ((o.getAdr() == 0 && !f && o.getType().getKind() == param.getKind())
					|| (o.getAdr() == 1 && f && o.getType().getKind() == param.getKind())) {
				report_info("Validan " + (f ? 1 : 0) + " parametar " + o.getName() + " funkcije " + func.getName()
						+ " na liniji " + expr.getLine(), null);
			}
		}
	}

	// -------------------------TERM -------------------------------

	public void visit(MultipleFactors fact) {
		fact.struct = fact.getFactor().struct;
		Struct factor = fact.getFactor().struct;
		Struct factorlist = fact.getFactorList().struct;
		if ((factorlist != null && factorlist.getKind() != Struct.Int) || factor.getKind() != Struct.Int) {
			report_error("Greska na liniji " + fact.getLine() + " : " + "tipovi moraju biti int! ", null);
			return;
		}
		report_info("Mul operation", fact);

	}

	public void visit(Term term) {
		term.struct = term.getFactor().struct;
		Struct factor = term.getFactor().struct;
		if (term.getFactorList().struct != null && factor.getKind() != Struct.Int) {
			report_error("Greska na liniji " + term.getLine() + " : " + "tipovi moraju biti int! ", null);
			return;
		}
	}

	// ------------------------- EXPR -------------------------------

	public void visit(Simple termExpr) {
		termExpr.struct = termExpr.getSimpleExpr().struct;
	}

	public void visit(Ternary termExpr) {
		termExpr.struct = termExpr.getTernaryExpr().struct;
	}

	public void visit(TernaryDef termExpr) {
		if (termExpr.getExpr().struct.getKind() != termExpr.getExpr1().struct.getKind()) {
			report_error("Izrazi moraju biti istog tipa", termExpr);
			return;
		}
		termExpr.struct = termExpr.getExpr().struct;
	}

	public void visit(SimpleDef multerms) {
		multerms.struct = multerms.getTerm().struct;
		if (multerms.getTermList().struct == null)
			return;
		if (multerms.getTerm().struct.getKind() != multerms.getTermList().struct.getKind())
			report_error("Greska na liniji " + multerms.getLine() + " : " + "tipovi nisu jednaki! ", null);
	}

	public void visit(SimpleDefWithMinus expr) {
		expr.struct = expr.getMinusTerm().struct;
		if (expr.getTermList().struct != null
				&& expr.getMinusTerm().struct.getKind() != (expr.getTermList().struct.getKind()))
			report_error("Greska na liniji " + expr.getLine() + " : " + "tipovi nisu jednaki! ", null);

	}

	public void visit(MinusTerm expr) {
		expr.struct = expr.getTerm().struct;
		if (expr.getTerm().struct.getKind() != Struct.Int) {
			report_error("Greska na liniji " + expr.getLine() + " : " + "term mora biti int! ", null);
			return;
		}
	}

	public void visit(MultipleTerms multerms) {
		report_info("Add operation", multerms);
		Struct termlist = multerms.getTermList().struct;
		multerms.struct = multerms.getTerm().struct;
		if (termlist == null)
			return;
		if (termlist.getKind() != Struct.Int || multerms.getTerm().struct.getKind() != Struct.Int)
			report_error("Greska na liniji " + multerms.getLine() + " : " + "tipovi nisu jednaki! ", null);
	}

	// ----------------- CONDITIONS -----------------------

	Struct boolType = Tab.find("bool").getType();

	public void visit(RelationCond rel) {
		int kind = rel.getSimpleExpr().struct.getKind();
		int kind1 = rel.getSimpleExpr1().struct.getKind();
		if (kind != kind1) {
			report_error("Tipovi izraza moraju biti kompatabilni", rel);
		}
		Relop relop = rel.getRelop();
		if ((kind == Struct.Class || kind == Struct.Array)
				&& !(relop instanceof EqualOp || relop instanceof NotEqualOp)) {
			report_error("Za klase i nizove mogu se koristiti samo != i ==", rel);
		}
		rel.struct = boolType;
	}

	public void visit(CondExpr cond) {
		if (cond.getSimpleExpr().struct != boolType) {
			report_error("Tipovi izraza mora biti bool", cond);
		}
		cond.struct = boolType;
	}

	public void visit(CondTerm cond) {
		if (cond.getAddCondFact().struct != boolType) {
			report_error("Tipovi izraza mora biti bool", cond);
		}
		cond.struct = boolType;
	}

	public void visit(OneCondFact cond) {
		if (cond.getCondFact().struct != boolType)
			report_error("Tipovi izraza mora biti bool", cond);
		cond.struct = boolType;
	}

	public void visit(AndConditions cond) {
		if (cond.getCondFact().struct != boolType
				|| (cond.getAddCondFact().struct != null && cond.getAddCondFact().struct != boolType)) {
			report_error("Tipovi izraza mora biti bool", cond);
		}
		cond.struct = boolType;
	}

	public void visit(Condition cond) {
		if (cond.getCondTermList().struct != boolType) {
			report_error("Tipovi izraza mora biti bool", cond);
		}
		cond.struct = boolType;
	}

	public void visit(OneCondTerm cond) {
		if (cond.getCondTerm().struct != boolType) {
			report_error("Tipovi izraza mora biti bool", cond);
		}
		cond.struct = boolType;
	}

	public void visit(MultipleCondTerm cond) {
		if (cond.getCondTerm().struct != boolType
				|| (cond.getCondTermList().struct != null && cond.getCondTermList().struct != boolType)) {
			report_error("Tipovi izraza mora biti bool", cond);
		}
		cond.struct = boolType;
	}

	public boolean passed() {
		return !errorDetected;
	}

}
