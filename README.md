# 🍽️ NutriReceitas - Sistema de Gerenciamento de Receitas

## 📋 Descrição

Sistema JavaFX para gerenciamento de receitas culinárias que consome uma API REST Spring Boot. O aplicativo permite realizar operações completas de CRUD (Create, Read, Update, Delete) sobre receitas de forma intuitiva e temática.

## ✨ Funcionalidades

### 🔐 Autenticação
- **Login**: Autenticação de usuários via email e senha
- **Cadastro**: Registro de novos usuários com validação de dados
- **Validações**: Verificação de email válido, senha mínima e confirmação

### 📚 Gerenciamento de Receitas
- **Listagem**: Visualização de todas as receitas do usuário logado
- **Pesquisa**: Filtro em tempo real por nome, ingredientes ou descrição
- **Criação**: Cadastro de novas receitas com informações completas
- **Edição**: Modificação de receitas existentes
- **Exclusão**: Remoção de receitas com confirmação
- **Contador**: Exibição do total de receitas filtradas

### 🎨 Interface Intuitiva
- **Design Temático**: Interface inspirada no universo culinário
- **Responsiva**: Adaptável a diferentes tamanhos de tela
- **Feedback Visual**: Indicadores de carregamento e status das operações
- **Emojis Temáticos**: Ícones relacionados à culinária para melhor UX

## 🏗️ Arquitetura

O projeto segue os princípios da programação orientada a objetos com separação clara de responsabilidades:

```
src/main/java/com/nutrireceitas/client/
├── model/          # Modelos de dados (Usuario, Receita, UsuarioLogin)
├── service/        # Camada de serviços (ApiService para comunicação HTTP)
├── controller/     # Controladores JavaFX (Login, Principal, Receita, Cadastro)
└── NutriReceitasApp.java  # Classe principal da aplicação

src/main/resources/
├── views/          # Arquivos FXML das interfaces
│   ├── login.fxml
│   ├── cadastro.fxml
│   ├── principal.fxml
│   └── receita.fxml
└── css/
    └── style.css   # Estilos visuais personalizados
```

## 🛠️ Tecnologias Utilizadas

### Backend API
- **Spring Boot** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Jackson** - Serialização JSON
- **H2 Database** - Banco de dados em memória (assumido)

### Cliente JavaFX
- **JavaFX 17** - Interface gráfica
- **Java HttpClient** - Requisições HTTP
- **Jackson** - Serialização/Deserialização JSON
- **Maven** - Gerenciamento de dependências

## 📦 Pré-requisitos

- **Java 11+** (com suporte a JavaFX)
- **Maven 3.6+**
- **API Spring Boot** rodando na porta 8080

## 🚀 Instalação e Execução

### 1. Configurar a API Backend

Primeiro, certifique-se de que a API Spring Boot está rodando:

```bash
# No diretório da API Spring Boot
mvn spring-boot:run
```

A API deve estar disponível em: `http://localhost:8080`

### 2. Clonar e Configurar o Cliente

```bash
# Clonar o repositório
git clone [URL_DO_REPOSITORIO]
cd nutrireceitas-client

# Estrutura de diretórios esperada:
nutrireceitas-client/
├── pom.xml
├── src/main/java/com/nutrireceitas/client/
│   ├── model/
│   ├── service/
│   ├── controller/
│   └── NutriReceitasApp.java
└── src/main/resources/
    ├── views/
    └── css/
```

### 3. Compilar e Executar

```bash
# Compilar o projeto
mvn clean compile

# Executar via Maven
mvn javafx:run

# OU executar via JAR (após package)
mvn clean package
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/nutrireceitas-client-1.0.0.jar
```

### 4. Configurar JavaFX (se necessário)

Se você não tiver o JavaFX instalado:

```bash
# Download do JavaFX SDK
# https://gluonhq.com/products/javafx/

# Executar com módulos explícitos
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp target/classes com.nutrireceitas.client.NutriReceitasApp
```

## 🖥️ Como Usar

### Login Inicial
1. Execute a aplicação
2. Use um usuário existente ou clique em "Criar Conta"
3. Para testar, você pode criar um novo usuário na tela de cadastro

### Gerenciamento de Receitas
1. **Visualizar**: As receitas são carregadas automaticamente após o login
2. **Pesquisar**: Digite no campo de pesquisa para filtrar receitas
3. **Criar Nova**: Clique em "➕ Nova Receita"
4. **Editar**: Selecione uma receita e clique em "✏️ Editar"
5. **Excluir**: Selecione uma receita e clique em "🗑️ Excluir"
6. **Atualizar**: Clique em "🔄 Atualizar" para recarregar a lista

### Campos da Receita
- **Nome**: Título da receita (obrigatório)
- **Ingredientes**: Lista de ingredientes necessários (obrigatório)
- **Tempo de Preparo**: Em minutos (obrigatório)
- **Porções**: Quantas pessoas serve (obrigatório)
- **Modo de Preparo**: Instruções detalhadas (obrigatório)

## 🔧 Configuração

### Alterar URL da API

Para alterar o endereço da API, edite o arquivo `ApiService.java`:

```java
private static final String BASE_URL = "http://localhost:8080";
```

### Personalizar Aparência

Os estilos podem ser modificados no arquivo `src/main/resources/css/style.css`.

## 📱 Endpoints da API Utilizados

### Usuários
- `POST /usuario/login` - Autenticação
- `POST /usuario/salvar` - Cadastro de usuário

### Receitas
- `GET /receita/usuario/{id}` - Listar receitas do usuário
- `GET /receita/id/{id}` - Buscar receita por ID
- `POST /receita/salvar` - Criar nova receita
- `PUT /receita/id/{id}` - Atualizar receita
- `DELETE /receita/id/{id}` - Excluir receita

## 🐛 Solução de Problemas

### API não conecta
- Verifique se a API Spring Boot está rodando na porta 8080
- Confirme se não há firewall bloqueando a conexão
- Teste a API diretamente: `curl http://localhost:8080/usuario`

### JavaFX não executa
- Verifique se está usando Java 11+
- Instale o JavaFX SDK separadamente se necessário
- Use os parâmetros `--module-path` e `--add-modules` na execução

### Erro de compilação
- Execute `mvn clean` antes de compilar novamente
- Verifique se todas as dependências foram baixadas: `mvn dependency:resolve`

### Interface não carrega
- Verifique se os arquivos FXML estão no caminho correto: `src/main/resources/views/`
- Confirme se o CSS está em: `src/main/resources/css/style.css`

## 🔄 Operações CRUD Implementadas

| Operação | Método HTTP | Endpoint | Funcionalidade |
|----------|-------------|----------|----------------|
| **Create** | POST | `/receita/salvar` | Criar nova receita |
| **Read** | GET | `/receita/usuario/{id}` | Listar receitas |
| **Read** | GET | `/receita/id/{id}` | Buscar receita específica |
| **Update** | PUT | `/receita/id/{id}` | Atualizar receita |
| **Delete** | DELETE | `/receita/id/{id}` | Excluir receita |

## 🎨 Recursos Visuais

### Paleta de Cores
- **Primária**: Azul (#3498db, #2980b9)
- **Secundária**: Cinza (#95a5a6, #7f8c8d)  
- **Sucesso**: Verde (#27ae60)
- **Aviso**: Laranja (#f39c12)
- **Erro**: Vermelho (#e74c3c)

### Ícones Temáticos
- 🍽️ Logo principal
- 📚 Seção de receitas
- 🔍 Busca
- ➕ Adicionar
- ✏️ Editar
- 🗑️ Excluir
- 🔄 Atualizar
- 📝 Formulário
- 🥕 Ingredientes
- 👨‍🍳 Modo de preparo

## 📝 Validações Implementadas

### Usuário
- Nome: obrigatório, não vazio
- Email: obrigatório, formato válido, único
- Senha: obrigatória, mínimo 6 caracteres
- Confirmação de senha: deve coincidir

### Receita
- Nome: obrigatório, não vazio
- Ingredientes: obrigatório, não vazio
- Tempo: obrigatório, maior que zero
- Porções: obrigatório, maior que zero
- Descrição: obrigatória, não vazia

## 🚀 Funcionalidades Complementares

### ✅ Implementadas
- **Filtro de pesquisa** em tempo real por nome, ingredientes e descrição
- **Contador de registros** exibindo total de receitas filtradas
- **Validações de entrada** com mensagens de erro específicas
- **Indicadores de carregamento** para melhor feedback visual
- **Confirmações de exclusão** para evitar perda acidental de dados
- **Interface responsiva** adaptável a diferentes resoluções
- **Tratamento de erros** robusto com mensagens informativas

### 🔮 Melhorias Futuras
- Upload de imagens para receitas
- Categorização por tipo de prato
- Sistema de favoritos
- Avaliações e comentários
- Exportação de receitas para PDF
- Modo offline com sincronização

## 📄 Licença

Este projeto é desenvolvido para fins educacionais e demonstração de integração JavaFX com APIs REST.

## 🤝 Contribuição

Para contribuir com o projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## 📞 Suporte

Em caso de dúvidas ou problemas:

1. Verifique se a API está rodando corretamente
2. Confirme se todas as dependências estão instaladas
3. Consulte a seção de solução de problemas
4. Verifique os logs da aplicação para erros específicos

---

**Desenvolvido com ❤️ e ☕ para a comunidade de desenvolvedores Java**
