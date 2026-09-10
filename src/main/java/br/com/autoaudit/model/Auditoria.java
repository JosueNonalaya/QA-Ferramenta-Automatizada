package br.com.autoaudit.model;

import br.com.autoaudit.enums.StatusAuditoria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Auditoria {

    private Long id;
    private String nomeProjeto;
    private LocalDateTime dataHoraAgendada;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private StatusAuditoria status;
    private double percentualAderenciaAceitavel;
    private double percentualAderencia;

    private List<Responsavel> responsaveis;
    private List<Documento> documentos;
    private Checklist checklist;
    private List<NaoConformidade> naoConformidades;

    public Auditoria() {
        this.responsaveis = new ArrayList<>();
        this.documentos = new ArrayList<>();
        this.naoConformidades = new ArrayList<>();
    }

    public Auditoria(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
        this.status = StatusAuditoria.AGENDADA;
        this.responsaveis = new ArrayList<>();
        this.documentos = new ArrayList<>();
        this.naoConformidades = new ArrayList<>();
    }

    //GETTER's e SETTER's
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNomeProjeto() {
        return nomeProjeto;
    }
    public void setNomeProjeto(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }
    public LocalDateTime getDataHoraAgendada() {
        return dataHoraAgendada;
    }
    public void setDataHoraAgendada(LocalDateTime dataHoraAgendada) {
        this.dataHoraAgendada = dataHoraAgendada;
    }
    public LocalDateTime getInicio() {
        return inicio;
    }
    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }
    public LocalDateTime getFim() {
        return fim;
    }
    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }
    public StatusAuditoria getStatus() {
        return status;
    }
    public void setStatus(StatusAuditoria status) {
        this.status = status;
    }
    public double getPercentualAderenciaAceitavel() {
        return percentualAderenciaAceitavel;
    }
    public void setPercentualAderenciaAceitavel(double percentualAderenciaAceitavel) {
        this.percentualAderenciaAceitavel = percentualAderenciaAceitavel;
    }
    public double getPercentualAderencia() {
        return percentualAderencia;
    }
    public void setPercentualAderencia(double percentualAderencia) {
        this.percentualAderencia = percentualAderencia;
    }
    public List<Responsavel> getResponsaveis() {
        return responsaveis;
    }
    public void setResponsaveis(List<Responsavel> responsaveis) {
        this.responsaveis = responsaveis;
    }
    public List<Documento> getDocumentos() {
        return documentos;
    }
    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }
    public Checklist getChecklist() {
        return checklist;
    }
    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }
    public List<NaoConformidade> getNaoConformidades() {
        return naoConformidades;
    }
    public void setNaoConformidades(List<NaoConformidade> naoConformidades) {
        this.naoConformidades = naoConformidades;
    }

    //METODOS
    public void adicionarResponsavel(Responsavel responsavel) {
        responsaveis.add(responsavel);
    }

    public void adicionarDocumento(Documento documento) {
        documentos.add(documento);
    }

    public void definirChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public void definirPercentualAderenciaAceitavel(double percentual) {
        this.percentualAderenciaAceitavel = percentual;
    }

    public void agendar(LocalDateTime dataHora) {
        this.dataHoraAgendada = dataHora;
        this.status = StatusAuditoria.AGENDADA;
    }

    public void alterarDataAgendada(LocalDateTime novaData) {
        this.dataHoraAgendada = novaData;
    }

    public void cancelar() {
        this.status = StatusAuditoria.CANCELADA;
    }

    public void iniciar() {
        this.inicio = LocalDateTime.now();
        this.status = StatusAuditoria.EM_ANDAMENTO;
    }

    public void pausar() {
        this.status = StatusAuditoria.PAUSADA;
    }

    public void finalizar() {
        this.fim = LocalDateTime.now();
        this.status = StatusAuditoria.FINALIZADA;
        calcularAderencia();
        gerarNaoConformidades();
    }

    public void calcularAderencia() {
        // Será implementado quando trabalharmos com as respostas do checklist.
    }

    public boolean atingiuAderenciaMinima() {
        return percentualAderencia >= percentualAderenciaAceitavel;
    }

    public void gerarNaoConformidades() {
        // Será implementado quando trabalharmos com as respostas do checklist.
    }

}