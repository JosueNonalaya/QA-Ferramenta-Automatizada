package br.com.autoaudit.model;

import br.com.autoaudit.enums.StatusNC;

public class NaoConformidade {

    private Long id;
    private String descricao;
    private StatusNC status;
    private ClassificacaoNC classificacao;
    private Auditoria auditoria;

    public NaoConformidade() {
    }

    public NaoConformidade(String descricao, ClassificacaoNC classificacao, Auditoria auditoria) {
        this.descricao = descricao;
        this.classificacao = classificacao;
        this.auditoria = auditoria;
        this.status = StatusNC.ABERTA;
    }

    //GETTER's e SETTER's
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusNC getStatus() {
        return status;
    }

    public void setStatus(StatusNC status) {
        this.status = status;
    }

    public ClassificacaoNC getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(ClassificacaoNC classificacao) {
        this.classificacao = classificacao;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }
}