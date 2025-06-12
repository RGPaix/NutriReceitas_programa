package Controllers;

import Services.ApiService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Receita;
import models.Usuario;

public class ReceitaController {
    @FXML private TextField nomeField;
    @FXML private TextArea ingredientesArea;
    @FXML private Spinner<Integer> tempoSpinner;
    @FXML private Spinner<Integer> serveSpinner;
    @FXML private TextArea descricaoArea;
    @FXML private Button salvarButton;
    @FXML private Button cancelarButton;
    @FXML private Label statusLabel;
    @FXML private ProgressIndicator progressIndicator;

    private ApiService apiService;
    private Usuario usuarioLogado;
    private Receita receitaEditando;
    private MainController mainController;

    public void initialize() {
        apiService = new ApiService();
        progressIndicator.setVisible(false);

        // Configurar spinners
        tempoSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 30));
        serveSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 4));

        // Tornar os spinners editáveis
        tempoSpinner.setEditable(true);
        serveSpinner.setEditable(true);
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public void setMainController(MainController controller) {
        this.mainController = controller;
    }

    public void setReceita(Receita receita) {
        this.receitaEditando = receita;
        preencherCampos(receita);
    }

    private void preencherCampos(Receita receita) {
        nomeField.setText(receita.getNome());
        ingredientesArea.setText(receita.getIngredientes());
        tempoSpinner.getValueFactory().setValue(receita.getTempoPreparo());
        serveSpinner.getValueFactory().setValue(receita.getServe());
        descricaoArea.setText(receita.getDescricao());
    }

    @FXML
    private void handleSalvar() {
        if (!validarCampos()) {
            return;
        }

        Receita receita = criarReceitaFromForm();

        setControlsDisabled(true);
        progressIndicator.setVisible(true);

        Task<Void> task;

        if (receitaEditando == null) {
            // Nova receita
            task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    apiService.salvarReceita(receita);
                    return null;
                }
            };
        } else {
            // Editar receita
            task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    apiService.atualizarReceita(receitaEditando.getId(), receita);
                    return null;
                }
            };
        }

        task.setOnSucceeded(e -> {
            mostrarStatus("Receita salva com sucesso!", false);
            if (mainController != null) {
                mainController.atualizarLista();
            }

            // Fechar janela após pequeno delay
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    javafx.application.Platform.runLater(() -> {
                        Stage stage = (Stage) salvarButton.getScene().getWindow();
                        stage.close();
                    });
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        });

        task.setOnFailed(e -> {
            mostrarStatus("Erro ao salvar receita: " + task.getException().getMessage(), true);
            setControlsDisabled(false);
            progressIndicator.setVisible(false);
        });

        new Thread(task).start();
    }

    @FXML
    private void handleCancelar() {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    private boolean validarCampos() {
        if (nomeField.getText().trim().isEmpty()) {
            mostrarStatus("Nome da receita é obrigatório!", true);
            nomeField.requestFocus();
            return false;
        }

        if (ingredientesArea.getText().trim().isEmpty()) {
            mostrarStatus("Ingredientes são obrigatórios!", true);
            ingredientesArea.requestFocus();
            return false;
        }

        if (descricaoArea.getText().trim().isEmpty()) {
            mostrarStatus("Modo de preparo é obrigatório!", true);
            descricaoArea.requestFocus();
            return false;
        }

        if (tempoSpinner.getValue() <= 0) {
            mostrarStatus("Tempo de preparo deve ser maior que zero!", true);
            tempoSpinner.requestFocus();
            return false;
        }

        if (serveSpinner.getValue() <= 0) {
            mostrarStatus("Número de porções deve ser maior que zero!", true);
            serveSpinner.requestFocus();
            return false;
        }

        return true;
    }

    private Receita criarReceitaFromForm() {
        Receita receita = new Receita();
        receita.setNome(nomeField.getText().trim());
        receita.setIngredientes(ingredientesArea.getText().trim());
        receita.setTempoPreparo(tempoSpinner.getValue());
        receita.setServe(serveSpinner.getValue());
        receita.setDescricao(descricaoArea.getText().trim());
        receita.setUsuarioId(usuarioLogado.getId());
        return receita;
    }

    private void setControlsDisabled(boolean disabled) {
        nomeField.setDisable(disabled);
        ingredientesArea.setDisable(disabled);
        tempoSpinner.setDisable(disabled);
        serveSpinner.setDisable(disabled);
        descricaoArea.setDisable(disabled);
        salvarButton.setDisable(disabled);
    }

    private void mostrarStatus(String mensagem, boolean erro) {
        statusLabel.setText(mensagem);
        statusLabel.setStyle(erro ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}
