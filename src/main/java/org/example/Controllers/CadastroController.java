package org.example.Controllers;

import org.example.Services.ApiService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.models.Usuario;

public class CadastroController {
    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;
    @FXML private PasswordField confirmarSenhaField;
    @FXML private Button salvarButton;
    @FXML private Button cancelarButton;
    @FXML private Label statusLabel;
    @FXML private ProgressIndicator progressIndicator;

    private ApiService apiService;

    public void initialize() {
        apiService = new ApiService();
        progressIndicator.setVisible(false);
    }

    @FXML
    private void handleSalvar() {
        if (!validarCampos()) {
            return;
        }

        Usuario usuario = new Usuario(
                nomeField.getText().trim(),
                emailField.getText().trim(),
                senhaField.getText()
        );

        setControlsDisabled(true);
        progressIndicator.setVisible(true);

        Task<String> cadastroTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return apiService.cadastrarUsuario(usuario);
            }

            @Override
            protected void succeeded() {
                mostrarStatus("Usuário cadastrado com sucesso!", false);
                // Limpar campos após sucesso
                limparCampos();
                setControlsDisabled(false);
                progressIndicator.setVisible(false);
            }

            @Override
            protected void failed() {
                mostrarStatus("Erro ao cadastrar usuário: " + getException().getMessage(), true);
                setControlsDisabled(false);
                progressIndicator.setVisible(false);
            }
        };

        new Thread(cadastroTask).start();
    }

    @FXML
    private void handleCancelar() {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    private boolean validarCampos() {
        if (nomeField.getText().trim().isEmpty()) {
            mostrarStatus("Nome é obrigatório!", true);
            return false;
        }

        if (emailField.getText().trim().isEmpty()) {
            mostrarStatus("Email é obrigatório!", true);
            return false;
        }

        if (!emailField.getText().contains("@")) {
            mostrarStatus("Email inválido!", true);
            return false;
        }

        if (senhaField.getText().isEmpty()) {
            mostrarStatus("Senha é obrigatória!", true);
            return false;
        }

        if (senhaField.getText().length() < 6) {
            mostrarStatus("Senha deve ter pelo menos 6 caracteres!", true);
            return false;
        }

        if (!senhaField.getText().equals(confirmarSenhaField.getText())) {
            mostrarStatus("Senhas não coincidem!", true);
            return false;
        }

        return true;
    }

    private void limparCampos() {
        nomeField.clear();
        emailField.clear();
        senhaField.clear();
        confirmarSenhaField.clear();
    }

    private void setControlsDisabled(boolean disabled) {
        nomeField.setDisable(disabled);
        emailField.setDisable(disabled);
        senhaField.setDisable(disabled);
        confirmarSenhaField.setDisable(disabled);
        salvarButton.setDisable(disabled);
    }

    private void mostrarStatus(String mensagem, boolean erro) {
        statusLabel.setText(mensagem);
        statusLabel.setStyle(erro ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}
