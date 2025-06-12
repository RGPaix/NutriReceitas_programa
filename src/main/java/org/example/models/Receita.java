package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Receita {
    private Integer id;
    private Integer usuarioId;
    private String nome;
    private String ingredientes;
    private Integer tempoPreparo;
    private Integer serve;
    private String descricao;

    // Construtores
    public Receita() {}

    public Receita(String nome, String ingredientes, Integer tempoPreparo, Integer serve, String descricao, Integer usuarioId) {
        this.nome = nome;
        this.ingredientes = ingredientes;
        this.tempoPreparo = tempoPreparo;
        this.serve = serve;
        this.descricao = descricao;
        this.usuarioId = usuarioId;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getIngredientes() { return ingredientes; }
    public void setIngredientes(String ingredientes) { this.ingredientes = ingredientes; }

    public Integer getTempoPreparo() { return tempoPreparo; }
    public void setTempoPreparo(Integer tempoPreparo) { this.tempoPreparo = tempoPreparo; }

    public Integer getServe() { return serve; }
    public void setServe(Integer serve) { this.serve = serve; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() {
        return nome + " (Serve " + serve + " pessoas - " + tempoPreparo + " min)";
    }
}
