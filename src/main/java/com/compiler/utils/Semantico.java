package com.compiler.utils;

import java.util.Stack;

public class Semantico implements Constants
{
    private Stack<String> pilha_tipos =  new Stack<>();
    private StringBuilder codigo = new StringBuilder();
    // O codigo é o que vai rodar

    public void executeAction(int action, Token token)	throws SemanticError
    {
        switch (action) {
            case 100:
                acao100();
                break;
            case 101:
                acao101();
                break;
            case 102:
                acao102();
                break;
            case 103:
                acao103(token);
                break;
            case 104:
                acao104(token);
                break;
            case 105:
                acao105(token);
                break;
            case 106:
                acao106();
                break;
            case 107:
                acao107();
                break;
            case 108:
                acao108();
                break;
            case 109:
                acao109();
                break;
            case 110:
                acao110();
                break;
            case 111:
                acao111();
                break;
            case 112:
                acao112();
                break;
            case 113:
                acao113();
                break;
            case 114:
                acao114();
                break;
            case 115:
                acao115();
                break;
            case 116:
                acao116();
                break;
            case 117:
                acao117();
                break;
            case 118:
                acao118();
                break;
            case 119:
                acao119();
                break;
            case 120:
                acao120();
                break;
            case 121:
                acao121();
                break;
            case 122:
                acao122();
                break;
            case 123:
                acao123();
                break;
            case 124:
                acao124();
                break;
            case 125:
                acao125();
                break;
            case 126:
                acao126();
                break;
            case 127:
                acao127();
                break;
            case 128:
                acao128();
                break;
            case 129:
                acao129();
                break;
            case 130:
                acao130();
                break;
        
            default:
                break;
        }
        System.out.println("Ação #"+action+", Token: "+token);
    }
    private void acao100 () {
      codigo.append (".assembly extern mscorlib {}\n" +
                       ".assembly _programa{}\n" +
                       ".module _programa.exe\n" +
                       "\n" +
                       ".class public _unica{\n" +
                       ".method static public void _principal(){\n" +
                       ".entrypoint\n");
    }

    private void acao101 () {
      codigo.append ("ret\n"+
                       "}\n" +
                       "}\n");
    }

    private void acao102 () {
      codigo.append ("call void [mscorlib]System.Console::Write(" + pilha_tipos.pop() + ")\n");
    }

    private void acao103 (Token token) {
        pilha_tipos.push("int64");
        codigo.append ("ldc.i8 " + token.getLexeme() + "\n");
        codigo.append ("conv.r8");
    }
    
    private void acao104 (Token token) {
      pilha_tipos.push("float64");
        codigo.append ("ldc.r8 " + token.getLexeme() + "\n");
    }

    private void acao105 (Token token) {
      pilha_tipos.push("string");
        codigo.append ("ldstr " +  token.getLexeme()  + "\n");
    }

    private void acao106 () {
      
    }

    private void acao107 () {
      
    }

    private void acao108 () {
      
    }

    private void acao109 () {
      
    }

    private void acao110 () {
      
    }

    private void acao111 () {
      
    }

    private void acao112 () {
      
    }

    private void acao113 () {
      
    }

    private void acao114 () {
      
    }

    private void acao115 () {
      
    }

    private void acao116 () {
      
    }

    private void acao117 () {
      
    }

    private void acao118() {
        pilha_tipos.push("string");

        codigo.append("ldstr \"\\n\"\n");
        codigo.append("call void [mscorlib]System.Console::Write(string)\n");
    }

    private void acao119 () {
      
    }

    private void acao120 () {
      
    }

    private void acao121 () {
      
    }

    private void acao122 () {
      
    }

    private void acao123 () {
      
    }

    private void acao124 () {
      
    }

    private void acao125 () {
      
    }

    private void acao126 () {
      
    }

    private void acao127 () {
      
    }

    private void acao128 () {
      
    }

    private void acao129 () {
      
    }

    private void acao130 () {
      
    }

    public String getCodigoGerado() {
        return codigo.toString();
    }
	
}
