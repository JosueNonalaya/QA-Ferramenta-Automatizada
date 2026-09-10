package br.com.autoaudit.model;

public class ClassificacaoNC {

    private Long id;
    private String nome;
    private String descricao;

    public ClassificacaoNC() {
    }

    public ClassificacaoNC(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    //GETTER's e SETTER's
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}