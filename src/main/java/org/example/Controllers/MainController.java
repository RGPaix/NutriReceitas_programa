package Controllers;

import Services.ApiService;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Receita;
import models.Usuario;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MainController {
    @FXML private Label bemVindoLabel;
    @FXML private Label totalReceitasLabel;
    @FXML private TextField pesquisaField;
    @FXML private TableView<Receita> receitasTable;
    @FXML private TableColumn<Receita, String> nomeColumn;
    @FXML private TableColumn<Receita, Integer> tempoColumn;
    @FXML private TableColumn<Receita, Integer> serveColumn;
    @FXML private Button novaReceitaButton;
    @FXML private Button editarButton;
    @FXML private Button excluirButton;
    @FXML private Button atualizarButton;
    @FXML private ProgressIndicator progressIndicator;

    private ApiService apiService;
    private Usuario usuarioLogado;
    private ObservableList<Receita> receitas;
    private FilteredList<Receita> receitasFiltradas;

    public void initialize() {
        apiService = new ApiService();
        receitas = FXCollections.observableArrayList();
        receitasFiltradas = new FilteredList<>(receitas);

        configurarTabela();
        configurarPesquisa();
        configurarBotoes();

        progressIndicator.setVisible(false);
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        bemVindoLabel.setText("Bem-vindo(a), " + usuario.getNome() + "!");
        carregarReceitas();
    }

    private void configurarTabela() {
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tempoColumn.setCellValueFactory(new PropertyValueFactory<>("tempoPreparo"));
        serveColumn.setCellValueFactory(new PropertyValueFactory<>("serve"));

        receitasTable.setItems(receitasFiltradas);

        // Adicionar listener para seleção
        receitasTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean itemSelecionado = newSelection != null;
                    editarButton.setDisable(!itemSelecionado);
                    excluirButton.setDisable(!itemSelecionado);
                }
        );
    }

    private void configurarPesquisa() {
        pesquisaField.textProperty().addListener((observable, oldValue, newValue) -> {
            receitasFiltradas.setPredicate(receita -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return receita.getNome().toLowerCase().contains(lowerCaseFilter) ||
                        receita.getIngredientes().toLowerCase().contains(lowerCaseFilter) ||
                        receita.getDescricao().toLowerCase().contains(lowerCaseFilter);
            });

            atualizarContador();
        });
    }

    private void configurarBotoes() {
        editarButton.setDisable(true);
        excluirButton.setDisable(true);
    }

    @FXML
    private void handleNovaReceita() {
        abrirTelaReceita(null);
    }

    @FXML
    private void handleEditar() {
        Receita receitaSelecionada = receitasTable.getSelectionModel().getSelectedItem();
        if (receitaSelecionada != null) {
            abrirTelaReceita(receitaSelecionada);
        }
    }

    @FXML
    private void handleExcluir() {
        Receita receitaSelecionada = receitasTable.getSelectionModel().getSelectedItem();
        if (receitaSelecionada == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir Receita");
        alert.setContentText("Tem certeza que deseja excluir a receita '" +
                receitaSelecionada.getNome() + "'?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            excluirReceita(receitaSelecionada);
        }
    }

    @FXML
    private void handleAtualizar() {
        carregarReceitas();
    }

    private void abrirTelaReceita(Receita receita) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/receita.fxml"));
            Parent root = loader.load();

            ReceitaController controller = loader.getController();
            controller.setUsuarioLogado(usuarioLogado);
            controller.setMainController(this);

            if (receita != null) {
                controller.setReceita(receita);
            }

            Stage stage = new Stage();
            stage.setTitle(receita == null ? "Nova Receita" : "Editar Receita");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(novaReceitaButton.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            mostrarErro("Erro ao abrir tela de receita: " + e.getMessage());
        }
    }

    private void carregarReceitas() {
        progressIndicator.setVisible(true);
        setControlsDisabled(true);

        Task<List<Receita>> task = new Task<List<Receita>>() {
            @Override
            protected List<Receita> call() throws Exception {
                return apiService.buscarReceitasPorUsuario(usuarioLogado.getId());
            }

            @Override
            protected void succeeded() {
                receitas.setAll(getValue());
                atualizarContador();
                progressIndicator.setVisible(false);
                setControlsDisabled(false);
            }

            @Override
            protected void failed() {
                mostrarErro("Erro ao carregar receitas: " + getException().getMessage());
                progressIndicator.setVisible(false);
                setControlsDisabled(false);
            }
        };

        new Thread(task).start();
    }

    private void excluirReceita(Receita receita) {
        progressIndicator.setVisible(true);
        setControlsDisabled(true);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                apiService.excluirReceita(receita.getId());
                return null;
            }

            @Override
            protected void succeeded() {
                receitas.remove(receita);
                atualizarContador();
                progressIndicator.setVisible(false);
                setControlsDisabled(false);

                mostrarInfo("Receita excluída com sucesso!");
            }

            @Override
            protected void failed() {
                mostrarErro("Erro ao excluir receita: " + getException().getMessage());
                progressIndicator.setVisible(false);
                setControlsDisabled(false);
            }
        };

        new Thread(task).start();
    }

    public void atualizarLista() {
        carregarReceitas();
    }

    private void atualizarContador() {
        Platform.runLater(() -> {
            int total = receitasFiltradas.size();
            totalReceitasLabel.setText("Total: " + total + " receita" + (total != 1 ? "s" : ""));
        });
    }

    private void setControlsDisabled(boolean disabled) {
        novaReceitaButton.setDisable(disabled);
        editarButton.setDisable(disabled);
        excluirButton.setDisable(disabled);
        atualizarButton.setDisable(disabled);
        pesquisaField.setDisable(disabled);
    }

    private void mostrarErro(String mensagem) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Ops! Algo deu errado");
            alert.setContentText(mensagem);
            alert.showAndWait();
        });
    }

    private void mostrarInfo(String mensagem) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Operação realizada");
            alert.setContentText(mensagem);
            alert.showAndWait();
        });
    }
}
