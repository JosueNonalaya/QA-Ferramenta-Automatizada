package br.com.autoaudit.model;

import br.com.autoaudit.enums.StatusNotificacao;
import java.time.LocalDateTime;

public class Notificacao {

    private Long id;
    private String mensagem;
    private LocalDateTime dataHora;
    private StatusNotificacao status;
    private Usuario usuario;

    public Notificacao() {
    }

    public Notificacao(String mensagem, LocalDateTime dataHora, Usuario usuario) {
        this.mensagem = mensagem;
        this.dataHora = dataHora;
        this.usuario = usuario;
        this.status = StatusNotificacao.PENDENTE;
    }

    //GETTER's e SETTER'
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusNotificacao getStatus() {
        return status;
    }

    public void setStatus(StatusNotificacao status) {
        this.status = status;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}