
package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   { return new_symbol(sym.PROG, yytext());}
"break"		{ return new_symbol(sym.BREAK, yytext());}
"enum"		{ return new_symbol(sym.ENUM, yytext());}
"class"		{ return new_symbol(sym.CLASS, yytext());}
"abstract"  { return new_symbol(sym.ABSTRACT, yytext());}
"else"		{ return new_symbol(sym.ELSE, yytext());}
"const"		{ return new_symbol(sym.CONST, yytext());}
"if"		{ return new_symbol(sym.IF, yytext());}
"new"		{ return new_symbol(sym.NEW, yytext());}
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"read"		{ return new_symbol(sym.READ, yytext());}
"return" 	{ return new_symbol(sym.RETURN, yytext()); }
"void" 		{ return new_symbol(sym.VOID, yytext()); }
"extends"	{ return new_symbol(sym.EXTENDS, yytext());}
"continue"	{ return new_symbol(sym.CONTINUE, yytext());}
"for"		{ return new_symbol(sym.FOR, yytext());}
"length"	{ return new_symbol(sym.LENGTH, yytext());}
"switch"	{ return new_symbol(sym.SWITCH, yytext());}
"case"		{ return new_symbol(sym.CASE, yytext());}
"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-"			{ return new_symbol(sym.MINUS, yytext());}
"++"		{ return new_symbol(sym.INC, yytext());}
"--"		{ return new_symbol(sym.DEC, yytext());}
"*"			{ return new_symbol(sym.MULTIPLY, yytext());}
"/"			{ return new_symbol(sym.DIVIDE, yytext());}
"%"			{ return new_symbol(sym.MODUL, yytext());}
"=" 		{ return new_symbol(sym.ASSIGN, yytext());}
"=="		{ return new_symbol(sym.EQUAL, yytext());}
"!="		{ return new_symbol(sym.NOTEQUAL, yytext());}
">"			{ return new_symbol(sym.GREATER, yytext());}
"<"			{ return new_symbol(sym.LESS, yytext());}
">="		{ return new_symbol(sym.GREQ, yytext());}
"<="		{ return new_symbol(sym.LEQ, yytext());}
"&&"		{ return new_symbol(sym.AND, yytext());}
"||"		{ return new_symbol(sym.OR, yytext());}
":"			{ return new_symbol(sym.COLON, yytext());}
"."			{ return new_symbol(sym.DOT, yytext());}
"["			{ return new_symbol(sym.LSQUARE, yytext());}
"]"			{ return new_symbol(sym.RSQUARE, yytext());}
"?"			{ return new_symbol(sym.QUESTIONMARK, yytext());}
";" 		{ return new_symbol(sym.SEMI, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"(" 		{ return new_symbol(sym.LPAREN, yytext()); }
")" 		{ return new_symbol(sym.RPAREN, yytext()); }
"{" 		{ return new_symbol(sym.LBRACE, yytext()); }
"}"			{ return new_symbol(sym.RBRACE, yytext()); }

"//" {yybegin(COMMENT);}
<COMMENT> . {yybegin(COMMENT);}
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

[0-9]+  { return new_symbol(sym.NUMBER, Integer.parseInt(yytext())); }
("true"|"false") {return new_symbol(sym.BOOL, yytext().equals("true")? 1 : 0);}
"'" . "'" { 
    return new_symbol(sym.CHAR, Character.valueOf( yytext().charAt(1)));
}
[a-zA-Z][a-zA-Z0-9_]*	{return new_symbol (sym.IDENT, yytext()); }
. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }










