package br.com.autoaudit.model;

import br.com.autoaudit.enums.TipoResponsavel;

public class Responsavel {

    private Long id;
    private Usuario usuario;
    private TipoResponsavel tipo;

    public Responsavel() {
    }

    public Responsavel(Usuario usuario, TipoResponsavel tipo) {
        this.usuario = usuario;
        this.tipo = tipo;
    }

    //GETTER's e SETTER's
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoResponsavel getTipo() {
        return tipo;
    }

    public void setTipo(TipoResponsavel tipo) {
        this.tipo = tipo;
    }
}