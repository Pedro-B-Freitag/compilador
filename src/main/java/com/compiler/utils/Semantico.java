package com.compiler.utils;

import java.util.*;

public class Semantico implements Constants
{
    private Stack<String> pilha_tipos =  new Stack<>();
    private StringBuilder codigo = new StringBuilder();
    private Stack<String> pilha_rotulos = new Stack<>();
    private int contadorRotulos = 0;
    private String operador_relacional = "";
    private String tipo_declarado = "";
    private List<String> lista_identificadores = new ArrayList<>();
    private Map<String, String> tabela_simbolos = new HashMap<>();

    private String novoRotulo() {
        return "R" + (contadorRotulos++);
    }

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
                acao111(token);
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
                acao120(token);
                break;
            case 121:
                acao121(token);
                break;
            case 122:
                acao122();
                break;
            case 123:
                acao123(token);
                break;
            case 124:
                acao124(token);
                break;
            case 125:
                acao125(token);
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
                acao129(token);
                break;
            case 130:
                acao130(token);
                break;
        
            default:
                break;
        }
        System.out.println("Ação #"+action+", Token: "+token);
    }
    private void acao100() {
        codigo.append(".assembly extern mscorlib {}\n" +
                ".assembly _programa{}\n" +
                ".module _programa.exe\n\n" +
                ".class public _unica{\n" +
                ".method static public void _principal(){\n" +
                ".entrypoint\n");
    }

    private void acao101() {
        codigo.append("ret\n}\n}\n");
    }

    private void acao102() throws SemanticError {
        if (pilha_tipos.isEmpty()) throw new SemanticError("pilha de tipos vazia em print");
        String tipo = pilha_tipos.pop();
        if (tipo.equals("int64")) codigo.append("conv.i8\n");
        codigo.append("call void [mscorlib]System.Console::Write(").append(tipo).append(")\n");
    }

    private void acao103(Token token) {
        pilha_tipos.push("int64");
        codigo.append("ldc.i8 ").append(token.getLexeme()).append("\n");
        codigo.append("conv.r8\n");
    }

    private void acao104(Token token) {
        pilha_tipos.push("float64");
        codigo.append("ldc.r8 ").append(token.getLexeme()).append("\n");
    }

    private void acao105(Token token) {
        pilha_tipos.push("string");
        codigo.append("ldstr ").append(token.getLexeme()).append("\n");
    }

    private void acao106() throws SemanticError {
        if (pilha_tipos.size() < 2) throw new SemanticError("tipos insuficientes em operação +");
        String t2 = pilha_tipos.pop();
        String t1 = pilha_tipos.pop();
        if (t1.equals("int64") && t2.equals("int64")) pilha_tipos.push("int64");
        else pilha_tipos.push("float64");
        codigo.append("add\n");
    }

    private void acao107() throws SemanticError {
        if (pilha_tipos.size() < 2) throw new SemanticError("tipos insuficientes em operação -");
        String t2 = pilha_tipos.pop();
        String t1 = pilha_tipos.pop();
        if (t1.equals("int64") && t2.equals("int64")) pilha_tipos.push("int64");
        else pilha_tipos.push("float64");
        codigo.append("sub\n");
    }

    private void acao108() throws SemanticError {
        if (pilha_tipos.size() < 2) throw new SemanticError("tipos insuficientes em operação *");
        String t2 = pilha_tipos.pop();
        String t1 = pilha_tipos.pop();
        if (t1.equals("int64") && t2.equals("int64")) pilha_tipos.push("int64");
        else pilha_tipos.push("float64");
        codigo.append("mul\n");
    }

    private void acao109() throws SemanticError {
        if (pilha_tipos.size() < 2) throw new SemanticError("tipos insuficientes em operação /");
        pilha_tipos.pop();
        pilha_tipos.pop();
        pilha_tipos.push("float64");
        codigo.append("div\n");
    }

    private void acao110() throws SemanticError {
        if (pilha_tipos.isEmpty()) throw new SemanticError("operador - unário sem operando");
        String t = pilha_tipos.pop();
        pilha_tipos.push(t);
        codigo.append("neg\n");
    }

    private void acao111(Token token) {
        operador_relacional = token.getLexeme();
    }

    private void acao112() throws SemanticError {
        if (pilha_tipos.size() < 2) throw new SemanticError("tipos insuficientes em operação relacional");
        pilha_tipos.pop();
        pilha_tipos.pop();
        pilha_tipos.push("bool");

        switch (operador_relacional) {
            case "==": codigo.append("ceq\n"); break;
            case "~=":
                codigo.append("ceq\n");
                codigo.append("ldc.i4.0\n");
                codigo.append("ceq\n");
                break;
            case "<": codigo.append("clt\n"); break;
            case ">": codigo.append("cgt\n"); break;
            default: throw new SemanticError("operador relacional inválido: " + operador_relacional);
        }

        operador_relacional = "";
    }

    private void acao113() throws SemanticError {
        if (pilha_tipos.size() < 2) throw new SemanticError("tipos insuficientes em operação and");
        pilha_tipos.pop();
        pilha_tipos.pop();
        pilha_tipos.push("bool");
        codigo.append("and\n");
    }

    private void acao114() throws SemanticError {
        if (pilha_tipos.size() < 2) throw new SemanticError("tipos insuficientes em operação or");
        pilha_tipos.pop();
        pilha_tipos.pop();
        pilha_tipos.push("bool");
        codigo.append("or\n");
    }

    private void acao115() {
        pilha_tipos.push("bool");
        codigo.append("ldc.i4 1\n");
    }

    private void acao116() {
        pilha_tipos.push("bool");
        codigo.append("ldc.i4 0\n");
    }

    private void acao117() throws SemanticError {
        if (pilha_tipos.isEmpty()) throw new SemanticError("not sem operando");
        pilha_tipos.pop();
        pilha_tipos.push("bool");
        codigo.append("ldc.i4 0\n");
        codigo.append("ceq\n");
    }

    private void acao118() {
        codigo.append("ldstr \"\\n\"\n");
        codigo.append("call void [mscorlib]System.Console::Write(string)\n");
    }

    private void acao119() throws SemanticError {
        if (lista_identificadores.isEmpty()) throw new SemanticError("não há identificadores cadastrados");
        for (String id : lista_identificadores) {
            if (tabela_simbolos.containsKey(id)) throw new SemanticError("id já declarado: " + id);
            tabela_simbolos.put(id, tipo_declarado);
            codigo.append(".locals (").append(tipo_declarado).append(" ").append(id).append(")\n");
        }
        lista_identificadores.clear();
    }

    private void acao120(Token token) {
        String lex = token.getLexeme().toLowerCase();
        switch (lex) {
            case "bool": tipo_declarado = "bool"; break;
            case "int": tipo_declarado = "int64"; break;
            case "float": tipo_declarado = "float64"; break;
            case "string": tipo_declarado = "string"; break;
            default: tipo_declarado = "";
        }
    }

    private void acao121(Token token) {
        lista_identificadores.add(token.getLexeme());
    }

    private void acao122() throws SemanticError {
        if (lista_identificadores.isEmpty()) throw new SemanticError("atribuição sem identificador");
        String id = lista_identificadores.get(0);
        lista_identificadores.clear();
        if (!tabela_simbolos.containsKey(id)) throw new SemanticError("variável não declarada: " + id);
        String tipoExp = pilha_tipos.pop();
        if (tipoExp.equals("int64")) codigo.append("conv.i8\n");
        codigo.append("stloc ").append(id).append("\n");
    }

    private void acao123(Token token) throws SemanticError {
        String id = token.getLexeme();
        if (!tabela_simbolos.containsKey(id)) throw new SemanticError("variável não declarada: " + id);
        String tipo = tabela_simbolos.get(id);
        if (tipo.equals("bool")) throw new SemanticError("bool inválido para comando de entrada");

        codigo.append("call string [mscorlib]System.Console::ReadLine()\n");

        switch (tipo) {
            case "int64":
                codigo.append("call int64 [mscorlib]System.Int64::Parse(string)\n");
                break;
            case "float64":
                codigo.append("call float64 [mscorlib]System.Double::Parse(string)\n");
                break;
            case "string":
                break;
            default:
                throw new SemanticError("tipo inválido para leitura: " + tipo);
        }

        codigo.append("stloc ").append(id).append("\n");
    }

    private void acao124(Token token) {
        codigo.append("ldstr ").append(token.getLexeme()).append("\n");
        codigo.append("call void [mscorlib]System.Console::Write(string)\n");
    }

    private void acao125(Token token) throws SemanticError {
        String tipo = pilha_tipos.pop();
        if (!tipo.equals("bool")) throw new SemanticError("expressão incompatível em comando de seleção", token.getPosition());
        String rot = novoRotulo();
        pilha_rotulos.push(rot);
        codigo.append("brfalse ").append(rot).append("\n");
    }

    private void acao126() {
        String rot = pilha_rotulos.pop();
        codigo.append(rot).append(":\n");
    }

    private void acao127() {
        String r2 = novoRotulo();
        codigo.append("br ").append(r2).append("\n");
        String r1 = pilha_rotulos.pop();
        codigo.append(r1).append(":\n");
        pilha_rotulos.push(r2);
    }

    private void acao128() {
        String r = novoRotulo();
        codigo.append(r).append(":\n");
        pilha_rotulos.push(r);
    }

    private void acao129(Token token) throws SemanticError {
        String tipo = pilha_tipos.pop();
        if (!tipo.equals("bool")) throw new SemanticError("expressão incompatível em comando de repetição", token.getPosition());
        String r = pilha_rotulos.pop();
        codigo.append("brfalse ").append(r).append("\n");
    }

    private void acao130(Token token) throws SemanticError {
        String id = token.getLexeme();
        if (!tabela_simbolos.containsKey(id)) throw new SemanticError("variável não declarada: " + id);
        String tipo = tabela_simbolos.get(id);
        pilha_tipos.push(tipo);
        codigo.append("ldloc ").append(id).append("\n");
        if (tipo.equals("int64")) codigo.append("conv.r8\n");
    }

    public String getCodigoGerado() {
        return codigo.toString();
    }
	
}
