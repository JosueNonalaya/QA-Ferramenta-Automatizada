package br.com.autoaudit.model;

public class ClassificacaoNC {

    private Long id;
    private String nome;
    private String descricao;
    private int prazoHoras;

    public ClassificacaoNC() {
    }

    public ClassificacaoNC(
            String nome,
            String descricao,
            int prazoHoras
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.prazoHoras = prazoHoras;
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

    public int getPrazoHoras() {
        return prazoHoras;
    }

    public void setPrazoHoras(int prazoHoras) {
        this.prazoHoras = prazoHoras;
    }
}