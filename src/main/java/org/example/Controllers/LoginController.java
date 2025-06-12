package Controllers;

import Services.ApiService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Usuario;

import java.io.IOException;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;
    @FXML private Button loginButton;
    @FXML private Button cadastroButton;
    @FXML private Label statusLabel;
    @FXML private ProgressIndicator progressIndicator;

    private ApiService apiService;

    public void initialize() {
        apiService = new ApiService();
        progressIndicator.setVisible(false);
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String senha = senhaField.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            mostrarStatus("Por favor, preencha todos os campos!", true);
            return;
        }

        // Desabilita controles durante o login
        setControlsDisabled(true);
        progressIndicator.setVisible(true);

        Task<Usuario> loginTask = new Task<Usuario>() {
            @Override
            protected Usuario call() throws Exception {
                return apiService.login(email, senha);
            }

            @Override
            protected void succeeded() {
                try {
                    Usuario usuario = getValue();
                    abrirTelaPrincipal(usuario);
                } catch (Exception e) {
                    mostrarStatus("Erro ao abrir tela principal: " + e.getMessage(), true);
                    setControlsDisabled(false);
                    progressIndicator.setVisible(false);
                }
            }

            @Override
            protected void failed() {
                mostrarStatus("Login falhou! Verifique suas credenciais.", true);
                setControlsDisabled(false);
                progressIndicator.setVisible(false);
            }
        };

        new Thread(loginTask).start();
    }

    @FXML
    private void handleCadastro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cadastro.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastro de Usuário - NutriReceitas");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarStatus("Erro ao abrir tela de cadastro: " + e.getMessage(), true);
        }
    }

    private void abrirTelaPrincipal(Usuario usuario) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/principal.fxml"));
        Parent root = loader.load();

        MainController controller = loader.getController();
        controller.setUsuarioLogado(usuario);

        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setTitle("NutriReceitas - Bem-vindo, " + usuario.getNome() + "!");
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
    }

    private void setControlsDisabled(boolean disabled) {
        emailField.setDisable(disabled);
        senhaField.setDisable(disabled);
        loginButton.setDisable(disabled);
        cadastroButton.setDisable(disabled);
    }

    private void mostrarStatus(String mensagem, boolean erro) {
        statusLabel.setText(mensagem);
        statusLabel.setStyle(erro ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}
