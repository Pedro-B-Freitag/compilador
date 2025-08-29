package com.compiler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class MainApp extends JFrame {

    private JTextArea editor;
    private JTextArea mensagens;
    private JLabel statusBar;
    private File arquivoAtual;

    public MainApp() {
        setTitle("Compilador - Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);            // item 1
        setResizable(false);           // item 1
        setLocationRelativeTo(null);

        // === Barra de ferramentas (70px) === item 2 e 9
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setPreferredSize(new Dimension(1500, 70));

        JButton novo     = criarBotao("Novo [Ctrl-N]",   "novo.png");
        JButton abrir    = criarBotao("Abrir [Ctrl-O]",  "abrir.png");
        JButton salvar   = criarBotao("Salvar [Ctrl-S]", "salvar.png");
        JButton copiar   = criarBotao("Copiar [Ctrl-C]", "copiar.png");
        JButton colar    = criarBotao("Colar [Ctrl-V]",  "colar.png");
        JButton recortar = criarBotao("Recortar [Ctrl-X]","recortar.png");
        JButton compilar = criarBotao("Compilar [F7]",   "compilar.png");
        JButton equipe   = criarBotao("Equipe [F1]",     "equipe.png");

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

        // === Editor com NumberedBorder (itens 4 e 5) ===
        editor = new JTextArea();
        editor.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        editor.setLineWrap(false); // sem quebra automática para aparecer barra horizontal
        editor.setBorder(new NumberedBorder()); // <— usa sua classe
        JScrollPane spEditor = new JScrollPane(
                editor,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS // barras SEMPRE visíveis
        );

        // === Área de mensagens (itens 6 e 7) ===
        mensagens = new JTextArea();
        mensagens.setEditable(false); // item 6
        mensagens.setLineWrap(false);
        JScrollPane spMsgs = new JScrollPane(
                mensagens,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS // barras SEMPRE visíveis
        );

        // === Split vertical ajustável (item 3) ===
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spEditor, spMsgs);
        split.setResizeWeight(0.75); // 75% editor / 25% mensagens
        split.setDividerSize(8);

        // === Barra de status (25px) — item 2 e 8 ===
        statusBar = new JLabel(" ");
        JPanel status = new JPanel(new BorderLayout());
        status.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        status.setPreferredSize(new Dimension(1500, 25));
        status.add(statusBar, BorderLayout.WEST);

        // === Layout raiz ===
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolbar, BorderLayout.NORTH);
        getContentPane().add(split, BorderLayout.CENTER);
        getContentPane().add(status, BorderLayout.SOUTH);

        // === Ações dos botões (itens 10–15) ===
        novo.addActionListener(e -> acaoNovo());
        abrir.addActionListener(e -> acaoAbrir());
        salvar.addActionListener(e -> acaoSalvar());
        copiar.addActionListener(e -> editor.copy());    // item 13
        colar.addActionListener(e -> editor.paste());    // item 13
        recortar.addActionListener(e -> editor.cut());   // item 13
        compilar.addActionListener(e -> mensagens.setText(
                "Compilação de programas ainda não foi implementada.")); // item 14
        equipe.addActionListener(e -> mensagens.setText(
                "Equipe de Desenvolvimento:\nPedro Bosini Freitag, Samuel Jose Candido e Vitor da Silva")); // item 15

        // === Atalhos de teclado (item 9/13/14/15) ===
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
        am.put("compilar", new AbstractAction() { public void actionPerformed(ActionEvent e) {
            mensagens.setText("Compilação de programas ainda não foi implementada."); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "equipe");
        am.put("equipe", new AbstractAction() { public void actionPerformed(ActionEvent e) {
            mensagens.setText("Equipe de Desenvolvimento:\nPedro Bosini Freitag, Samuel Jose Candido e Vitor da Silva"); }});
    }

    private void acaoNovo() { // item 10
        editor.setText("");
        mensagens.setText("");
        statusBar.setText(" ");
        arquivoAtual = null;
    }

    private void acaoAbrir() { // item 11
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
                statusBar.setText(f.getAbsolutePath()); // atualiza barra de status
                arquivoAtual = f;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        // se cancelar, mantém tudo como estava (conforme enunciado)
    }

    private void acaoSalvar() { // item 12
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
                statusBar.setText(f.getAbsolutePath()); // atualiza (caso novo)
                arquivoAtual = f;
            } else {
                try (BufferedWriter w = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(arquivoAtual), StandardCharsets.UTF_8))) {
                    w.write(editor.getText());
                }
                mensagens.setText("");
                // mantém status bar como está (mesmo caminho) — "manter a barra de status"
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}
