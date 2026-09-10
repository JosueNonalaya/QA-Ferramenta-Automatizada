package br.com.autoaudit.model;

import br.com.autoaudit.enums.TipoDocumento;

public class Documento {

    private Long id;
    private String nome;
    private String caminho;
    private TipoDocumento tipo;

    public Documento() {
    }

    public Documento(String nome, String caminho, TipoDocumento tipo) {
        this.nome = nome;
        this.caminho = caminho;
        this.tipo = tipo;
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

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public TipoDocumento getTipo() {
        return tipo;
    }

    public void setTipo(TipoDocumento tipo) {
        this.tipo = tipo;
    }
}