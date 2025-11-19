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
        codigo.append ("ldc.srt " + "\"" + token.getLexeme() + "\"" + "\n");
    }
	
}
