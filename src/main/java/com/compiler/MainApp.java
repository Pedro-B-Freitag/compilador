package com.compiler;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MainApp extends Application {

    private TextArea editor;
    private TextArea numerosLinha;
    private TextArea mensagens;
    private Label statusBar;
    private File arquivoAtual;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Compilador - Interface");
        stage.setWidth(1500);
        stage.setHeight(800);
        stage.setResizable(false);

        ToolBar toolbar = new ToolBar();
        toolbar.setPrefHeight(70);

        Button novo = criarBotao("Novo [Ctrl-N]", "novo.png");
        Button abrir = criarBotao("Abrir [Ctrl-O]", "abrir.png");
        Button salvar = criarBotao("Salvar [Ctrl-S]", "salvar.png");
        Button copiar = criarBotao("Copiar [Ctrl-C]", "copiar.png");
        Button colar = criarBotao("Colar [Ctrl-V]", "colar.png");
        Button recortar = criarBotao("Recortar [Ctrl-X]", "recortar.png");
        Button compilar = criarBotao("Compilar [F7]", "compilar.png");
        Button equipe = criarBotao("Equipe [F1]", "equipe.png");

        toolbar.getItems().addAll(novo, abrir, salvar, copiar, colar, recortar, compilar, equipe);

        editor = new TextArea();
        editor.setWrapText(false);
        editor.setPromptText("");
        editor.setFocusTraversable(true);

        numerosLinha = new TextArea("1");
        numerosLinha.setEditable(false);
        numerosLinha.setMouseTransparent(true);
        numerosLinha.setFocusTraversable(false);
        numerosLinha.setPrefWidth(50);
        numerosLinha.setMaxWidth(60);
        numerosLinha.setWrapText(false);
        numerosLinha.getStyleClass().add("line-numbers");
        numerosLinha.setStyle("-fx-control-inner-background: #f0f0f0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-hbar-policy: never; -fx-vbar-policy: never;");
        numerosLinha.setScrollTop(Double.MIN_VALUE);
        numerosLinha.setScrollLeft(Double.MIN_VALUE);

        editor.textProperty().addListener((obs, oldText, newText) -> atualizarNumeracao());

        bindScrollBars(numerosLinha, editor);

        HBox editorComLinhas = new HBox(numerosLinha, editor);
        HBox.setHgrow(editor, Priority.ALWAYS);

        mensagens = new TextArea();
        mensagens.setEditable(false);

        SplitPane split = new SplitPane();
        split.setOrientation(Orientation.VERTICAL);
        split.getItems().addAll(editorComLinhas, mensagens);
        split.setDividerPositions(0.75);

        statusBar = new Label("");
        HBox status = new HBox(statusBar);
        status.setPadding(new Insets(2, 8, 2, 8));
        status.setPrefHeight(25);

        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(split);
        root.setBottom(status);

        Scene scene = new Scene(root);

        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN), () -> novo.fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN), () -> abrir.fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), () -> salvar.fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN), () -> copiar.fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN), () -> colar.fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN), () -> recortar.fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F7), () -> compilar.fire());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F1), () -> equipe.fire());

        novo.setOnAction(e -> {
            editor.clear();
            mensagens.clear();
            statusBar.setText("");
            arquivoAtual = null;
            atualizarNumeracao();
        });

        abrir.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos de Texto (*.txt)", "*.txt"));
            File f = fc.showOpenDialog(stage);
            if (f != null) {
                carregarArquivo(f);
            }
        });

        salvar.setOnAction(e -> salvarArquivo(stage));

        copiar.setOnAction(e -> editor.copy());
        colar.setOnAction(e -> editor.paste());
        recortar.setOnAction(e -> editor.cut());

        compilar.setOnAction(e -> {
            mensagens.clear();
            mensagens.setText("Compilação de programas ainda não foi implementada.");
        });

        equipe.setOnAction(e -> {
            mensagens.clear();
            mensagens.setText("Equipe de Desenvolvimento:\nPedro Bosini Freitag");
        });

        stage.setScene(scene);
        stage.show();

        atualizarNumeracao();
    }

    private void carregarArquivo(File f) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line).append("\n");
            }
            editor.setText(sb.toString());
            mensagens.clear();
            statusBar.setText(f.getAbsolutePath());
            arquivoAtual = f;
            atualizarNumeracao();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void salvarArquivo(Stage stage) {
        try {
            if (arquivoAtual == null) {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos de Texto (*.txt)", "*.txt"));
                File f = fc.showSaveDialog(stage);
                if (f == null) return;
                arquivoAtual = f;
            }
            try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivoAtual), StandardCharsets.UTF_8))) {
                w.write(editor.getText());
            }
            mensagens.clear();
            statusBar.setText(arquivoAtual.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Button criarBotao(String texto, String arquivoIcone) {
        ImageView icone = null;
        try {
            InputStream is = getClass().getResourceAsStream("/icons/" + arquivoIcone);
            if (is != null) {
                Image img = new Image(is);
                icone = new ImageView(img);
                icone.setFitWidth(32);
                icone.setFitHeight(32);
                icone.setPreserveRatio(true);
            }
        } catch (Exception ignored) { }
        Button b = (icone == null) ? new Button(texto) : new Button(texto, icone);
        b.setContentDisplay(ContentDisplay.TOP);
        b.setPrefWidth(120);
        b.setPrefHeight(70);
        return b;
    }

    private void atualizarNumeracao() {
        String text = editor.getText();
        int linhas = 1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') linhas++;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= linhas; i++) {
            sb.append(i).append("\n");
        }
        numerosLinha.setText(sb.toString());
        numerosLinha.setScrollTop(editor.getScrollTop());
    }

    private void bindScrollBars(TextArea left, TextArea right) {
        left.scrollTopProperty().bindBidirectional(right.scrollTopProperty());
    }

    public static void main(String[] args) {
        launch();
    }
}
