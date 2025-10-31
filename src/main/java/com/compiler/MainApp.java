package com.compiler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import com.compiler.utils.*;

public class MainApp extends JFrame {

    private JTextArea editor;
    private JTextArea mensagens;
    private JLabel statusBar;
    private File arquivoAtual;
    private String equipeNomes = "Equipe de Desenvolvimento:\nPedro Bosini Freitag, Samuel Jose Candido e Vitor da Silva";

    //____________________________________________________________________________________________________________
    public MainApp() {
        setTitle("Compilador - Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setResizable(false);
        setLocationRelativeTo(null);

//____________________________________________________________________________________________________________        
        JButton novo     = criarBotao("Novo [Ctrl-N]",   "novo.png");
        JButton abrir    = criarBotao("Abrir [Ctrl-O]",  "abrir.png");
        JButton salvar   = criarBotao("Salvar [Ctrl-S]", "salvar.png");
        JButton copiar   = criarBotao("Copiar [Ctrl-C]", "copiar.png");
        JButton colar    = criarBotao("Colar [Ctrl-V]",  "colar.png");
        JButton recortar = criarBotao("Recortar [Ctrl-X]","recortar.png");
        JButton compilar = criarBotao("Compilar [F7]",   "compilar.png");
        JButton equipe   = criarBotao("Equipe [F1]",     "equipe.png");

//____________________________________________________________________________________________________________        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setPreferredSize(new Dimension(1500, 70));
        toolbar.add(novo);
        toolbar.addSeparator(new Dimension(10, 0));
        toolbar.add(abrir);
        toolbar.addSeparator(new Dimension(10, 0));
        toolbar.add(salvar);
        toolbar.addSeparator(new Dimension(10, 0));
        toolbar.add(copiar);
        toolbar.addSeparator(new Dimension(10, 0));
        toolbar.add(colar);
        toolbar.addSeparator(new Dimension(10, 0));
        toolbar.add(recortar);
        toolbar.addSeparator(new Dimension(10, 0));
        toolbar.add(compilar);
        toolbar.addSeparator(new Dimension(10, 0));
        toolbar.add(equipe);

//____________________________________________________________________________________________________________        
        editor = new JTextArea();
        editor.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        editor.setLineWrap(false);
        editor.setBorder(new NumberedBorder());
        JScrollPane spEditor = new JScrollPane(
                editor,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );

//____________________________________________________________________________________________________________        
        mensagens = new JTextArea();
        mensagens.setEditable(false);
        mensagens.setLineWrap(false);
        JScrollPane spMsgs = new JScrollPane(
                mensagens,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );

//____________________________________________________________________________________________________________        
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spEditor, spMsgs);
        split.setResizeWeight(0.75);
        split.setDividerSize(8);

//____________________________________________________________________________________________________________        
        statusBar = new JLabel(" ");
        JPanel status = new JPanel(new BorderLayout());
        status.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        status.setPreferredSize(new Dimension(1500, 25));
        status.add(statusBar, BorderLayout.WEST);

//____________________________________________________________________________________________________________        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolbar, BorderLayout.NORTH);
        getContentPane().add(split, BorderLayout.CENTER);
        getContentPane().add(status, BorderLayout.SOUTH);

//____________________________________________________________________________________________________________        
        novo.addActionListener(e -> acaoNovo());
        abrir.addActionListener(e -> acaoAbrir());
        salvar.addActionListener(e -> acaoSalvar());
        copiar.addActionListener(e -> editor.copy());
        colar.addActionListener(e -> editor.paste());
        recortar.addActionListener(e -> editor.cut());
        compilar.addActionListener(e -> analiseLexica());
        equipe.addActionListener(e -> mensagens.setText(equipeNomes));

//____________________________________________________________________________________________________________        
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "novo");
        am.put("novo", new AbstractAction() { public void actionPerformed(ActionEvent e) { acaoNovo(); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), "abrir");
        am.put("abrir", new AbstractAction() { public void actionPerformed(ActionEvent e) { acaoAbrir(); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "salvar");
        am.put("salvar", new AbstractAction() { public void actionPerformed(ActionEvent e) { acaoSalvar(); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "copiar");
        am.put("copiar", new AbstractAction() { public void actionPerformed(ActionEvent e) { editor.copy(); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), "colar");
        am.put("colar", new AbstractAction() { public void actionPerformed(ActionEvent e) { editor.paste(); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK), "recortar");
        am.put("recortar", new AbstractAction() { public void actionPerformed(ActionEvent e) { editor.cut(); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "compilar");
        am.put("compilar", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                analiseLexica();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "equipe");
        am.put("equipe", new AbstractAction() { public void actionPerformed(ActionEvent e) {
            mensagens.setText("Equipe de Desenvolvimento:\nPedro Bosini Freitag, Samuel Jose Candido e Vitor da Silva"); }});
    }

    //____________________________________________________________________________________________________________
    private void acaoNovo() {
        editor.setText("");
        mensagens.setText("");
        statusBar.setText(" ");
        arquivoAtual = null;
    }

    //____________________________________________________________________________________________________________
    private void acaoAbrir() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Arquivos de Texto (*.txt)", "txt"));
        int res = fc.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) sb.append(line).append(System.lineSeparator());
                editor.setText(sb.toString());
                mensagens.setText("");
                statusBar.setText(f.getAbsolutePath());
                arquivoAtual = f;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //____________________________________________________________________________________________________________
    private void acaoSalvar() {
        try {
            if (arquivoAtual == null) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter("Arquivos de Texto (*.txt)", "txt"));
                int res = fc.showSaveDialog(this);
                if (res != JFileChooser.APPROVE_OPTION) return;

                File f = fc.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".txt")) {
                    f = new File(f.getParentFile(), f.getName() + ".txt");
                }
                try (BufferedWriter w = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
                    w.write(editor.getText());
                }
                mensagens.setText("");
                statusBar.setText(f.getAbsolutePath());
                arquivoAtual = f;
            } else {
                try (BufferedWriter w = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(arquivoAtual), StandardCharsets.UTF_8))) {
                    w.write(editor.getText());
                }
                mensagens.setText("");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    //____________________________________________________________________________________________________________
    private JButton criarBotao(String texto, String iconeArquivo) {
        JButton b = new JButton(texto);
        b.setFocusable(false);
        b.setPreferredSize(new Dimension(120, 70));
        b.setHorizontalTextPosition(SwingConstants.CENTER);
        b.setVerticalTextPosition(SwingConstants.BOTTOM);

        var url = getClass().getResource("/icons/" + iconeArquivo);
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            b.setIcon(new ImageIcon(img));
        }
        return b;
    }

    //____________________________________________________________________________________________________________
    public void analiseLexica() {
        Lexico lexico = new Lexico();
        Sintatico sintatico = new Sintatico();
        Semantico semantico = new Semantico();
        lexico.setInput(editor.getText());

        // SUCESSO
        try {
            mensagens.setText("");
            sintatico.parse(lexico, semantico);
            mensagens.setText("programa compilado com sucesso");
            /* Token t = null;
            //revisar aqui
            while ( (t = lexico.nextToken()) != null ) {

                int linha = getLinha(editor.getText(), t.getPosition());

                mensagens.append(t.getLexeme() +
                                " - id:" + classificaToken(t.getId()) +
                                " - pos:" + linha + "\n");

                // só escreve o lexema, necessário escrever t.getId, t.getPosition()

                // t.getId () - retorna o identificador da classe (ver Constants.java)
                // necessário adaptar, pois deve ser apresentada a classe por extenso
                // MUDAR ID PELO NOME EXTENSO USANDO SWITCH CASE

                // t.getPosition () - retorna a posição inicial do lexema no editor
                // necessário adaptar para mostrar a linha


                // esse código apresenta os tokens enquanto não ocorrer erro
                // no entanto, os tokens devem ser apresentados SÓ se não ocorrer erro,
                // necessário adaptar para atender o que foi solicitado
            } */
        }


        // RETORNO ERROS

        // Exibe mensagem de erro léxico com linha e descrição
        catch (LexicalError f) {
            String mensagemErro = f.getMessage();
            
            // Se for erro de símbolo inválido, adiciona o símbolo na mensagem
            if (mensagemErro.equals("símbolo inválido")) {
                String texto = editor.getText();
                int pos = f.getPosition();
                
                // Obtém o caractere na posição do erro
                char simboloInvalido = pos < texto.length() ? texto.charAt(pos) : ' ';
                
                mensagemErro = String.format("%c símbolo inválido", simboloInvalido);
            }
            
            mensagens.setText("linha " + getLinha(editor.getText(), f.getPosition()) + 
                            ": " + mensagemErro);
        }


        // Exibe mensagem de erro sintático com linha e descrição
        catch (SyntaticError e) {
            Token token = null;
            
            lexico.setInput(editor.getText());

            while (true) {
                try {
                    token = lexico.nextToken();
                    if(!(token != null && token.getPosition() < e.getPosition())){
                        break;
                    }
                } catch (LexicalError ex) {
                    throw new RuntimeException(ex);
                }
            }
            String encontradoStr;
            if (token == null) {
                encontradoStr = "EOF"; 
            } else {
                if (token.getId() == Constants.t_cstring) {
                    encontradoStr = "constante_string";
                }
                else if ("$".equals(token.getLexeme())) {
                    encontradoStr = "EOF";
                }
                else {
                    encontradoStr = token.getLexeme();
                }
            }
            String mensagemErro = "linha " + getLinha(editor.getText(), e.getPosition())
                    + ": encontrado " + encontradoStr + " " + e.getMessage();

            mensagens.setText(mensagemErro);
        }
        catch ( SemanticError e ) {
            // trata erros semânticos na parte 4
        }
    }

    //____________________________________________________________________________________________________________
    public String classificaToken(int token) {

        switch (token) {

            // Constantes
            case Constants.t_identificador:
                return "identificador";

            case Constants.t_cint:
                return "constante_int";

            case Constants.t_cfloat:
                return "constante_float";

            case Constants.t_cstring:
                return "constante_string";

            //Palavras reservadas
            case Constants.t_pr_add:
            case Constants.t_pr_and:
            case Constants.t_pr_begin:
            case Constants.t_pr_bool:
            case Constants.t_pr_count:
            case Constants.t_pr_delete:
            case Constants.t_pr_do:
            case Constants.t_pr_elementof:
            case Constants.t_pr_else:
            case Constants.t_pr_end:
            case Constants.t_pr_false:
            case Constants.t_pr_float:
            case Constants.t_pr_if:
            case Constants.t_pr_int:
            case Constants.t_pr_list:
            case Constants.t_pr_not:
            case Constants.t_pr_or:
            case Constants.t_pr_print:
            case Constants.t_pr_read:
            case Constants.t_pr_size:
            case Constants.t_pr_string:
            case Constants.t_pr_true:
            case Constants.t_pr_until:
                return "palavra_reservada";

            //Simbolos Especiais
            case Constants.t_TOKEN_29: // (
            case Constants.t_TOKEN_30: // )
            case Constants.t_TOKEN_31: // +
            case Constants.t_TOKEN_32: // -
            case Constants.t_TOKEN_33: // *
            case Constants.t_TOKEN_34: // /
            case Constants.t_TOKEN_35: // ==
            case Constants.t_TOKEN_36: // ~=
            case Constants.t_TOKEN_37: // <
            case Constants.t_TOKEN_38: // >
            case Constants.t_TOKEN_39: // =
            case Constants.t_TOKEN_40: // <-
            case Constants.t_TOKEN_41: // ;
            case Constants.t_TOKEN_42: // ,
                return "simbolo_especial";

            default:
                return "token_desconhecido";
        }
    }

    //____________________________________________________________________________________________________________
    private int getLinha(String texto, int posicao) {
        int linha = 1; // inicia sempre na linha 1

        for (int i = 0; i < posicao && i < texto.length(); i++) {

            if (texto.charAt(i) == '\n') {
                linha++;
            }
        }
        return linha;
    }

    // Retorna o nome legível do token (sem a palavra "esperado")
    private String tokenName(int t) {
        switch (t) {
            case Constants.DOLLAR:
                return "EOF";
            case Constants.t_identificador:
                return "identificador";
            case Constants.t_cint:
                return "constante_int";
            case Constants.t_cfloat:
                return "constante_float";
            case Constants.t_cstring:
                return "constante_string";
            case Constants.t_pr_add:
                return "add";
            case Constants.t_pr_and:
                return "and";
            case Constants.t_pr_begin:
                return "begin";
            case Constants.t_pr_bool:
                return "bool";
            case Constants.t_pr_count:
                return "count";
            case Constants.t_pr_delete:
                return "delete";
            case Constants.t_pr_do:
                return "do";
            case Constants.t_pr_elementof:
                return "elementoOf";
            case Constants.t_pr_else:
                return "else";
            case Constants.t_pr_end:
                return "end";
            case Constants.t_pr_false:
                return "false";
            case Constants.t_pr_float:
                return "float";
            case Constants.t_pr_if:
                return "if";
            case Constants.t_pr_int:
                return "int";
            case Constants.t_pr_list:
                return "list";
            case Constants.t_pr_not:
                return "not";
            case Constants.t_pr_or:
                return "or";
            case Constants.t_pr_print:
                return "print";
            case Constants.t_pr_read:
                return "read";
            case Constants.t_pr_size:
                return "size";
            case Constants.t_pr_string:
                return "string";
            case Constants.t_pr_true:
                return "true";
            case Constants.t_pr_until:
                return "util";
            case Constants.t_TOKEN_29:
                return "+";
            case Constants.t_TOKEN_30:
                return "-";
            case Constants.t_TOKEN_31:
                return "*";
            case Constants.t_TOKEN_32:
                return "/";
            case Constants.t_TOKEN_33:
                return "==";
            case Constants.t_TOKEN_34:
                return "~=";
            case Constants.t_TOKEN_35:
                return "<";
            case Constants.t_TOKEN_36:
                return ">";
            case Constants.t_TOKEN_37:
                return "=";
            case Constants.t_TOKEN_38:
                return "<-";
            case Constants.t_TOKEN_39:
                return "(";
            case Constants.t_TOKEN_40:
                return ")";
            case Constants.t_TOKEN_41:
                return ";";
            case Constants.t_TOKEN_42:
                return ",";
            default:
                return null;
        }
    }

    //____________________________________________________________________________________________________________
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}
