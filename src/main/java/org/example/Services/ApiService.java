package Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Receita;
import models.Usuario;
import models.UsuarioLogin;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiService {
    private static final String BASE_URL = "http://localhost:8080";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // ============ MÉTODOS PARA USUÁRIO ============

    public Usuario login(String email, String senha) throws IOException, InterruptedException {
        UsuarioLogin loginDto = new UsuarioLogin(email, senha);
        String jsonBody = objectMapper.writeValueAsString(loginDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/usuario/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Usuario.class);
        } else {
            throw new RuntimeException("Login falhou: " + response.body());
        }
    }

    public String cadastrarUsuario(Usuario usuario) throws IOException, InterruptedException {
        String jsonBody = objectMapper.writeValueAsString(usuario);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/usuario/salvar"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // ============ MÉTODOS PARA RECEITA ============

    public List<Receita> buscarTodasReceitas() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/receita"))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Receita>>() {});
        } else {
            throw new RuntimeException("Erro ao buscar receitas: " + response.statusCode());
        }
    }

    public List<Receita> buscarReceitasPorUsuario(Integer usuarioId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/receita/usuario/" + usuarioId))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Receita>>() {});
        } else {
            throw new RuntimeException("Erro ao buscar receitas do usuário: " + response.statusCode());
        }
    }

    public Receita buscarReceitaPorId(Integer id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/receita/id/" + id))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Receita.class);
        } else {
            throw new RuntimeException("Receita não encontrada");
        }
    }

    public void salvarReceita(Receita receita) throws IOException, InterruptedException {
        String jsonBody = objectMapper.writeValueAsString(receita);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/receita/salvar"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Erro ao salvar receita: " + response.body());
        }
    }

    public void atualizarReceita(Integer id, Receita receita) throws IOException, InterruptedException {
        String jsonBody = objectMapper.writeValueAsString(receita);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/receita/id/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Erro ao atualizar receita: " + response.body());
        }
    }

    public void excluirReceita(Integer id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/receita/id/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Erro ao excluir receita: " + response.body());
        }
    }
}
