package br.com.autoaudit.model;

import br.com.autoaudit.enums.StatusNC;

import java.time.LocalDate;

public class NaoConformidade {

    private Long id;
    private String descricao;
    private StatusNC status;
    private ClassificacaoNC classificacao;
    private Auditoria auditoria;
    private LocalDate dataIdentificacao;
    private Responsavel responsavel;
    private LocalDate dataResolucao;
    private LocalDate dataEscalonamento;
    private String acaoCorretiva;

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

    public LocalDate getDataIdentificacao() {
        return dataIdentificacao;
    }

    public void setDataIdentificacao(LocalDate dataIdentificacao) {
        this.dataIdentificacao = dataIdentificacao;
    }

    public Responsavel getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Responsavel responsavel) {
        this.responsavel = responsavel;
    }

    public LocalDate getDataResolucao() {
        return dataResolucao;
    }

    public void setDataResolucao(LocalDate dataResolucao) {
        this.dataResolucao = dataResolucao;
    }

    public LocalDate getDataEscalonamento() {
        return dataEscalonamento;
    }

    public void setDataEscalonamento(LocalDate dataEscalonamento) {
        this.dataEscalonamento = dataEscalonamento;
    }

    public String getAcaoCorretiva() {
        return acaoCorretiva;
    }

    public void setAcaoCorretiva(String acaoCorretiva) {
        this.acaoCorretiva = acaoCorretiva;
    }
}