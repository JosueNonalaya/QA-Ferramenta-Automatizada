package br.com.autoaudit.controller;

import br.com.autoaudit.enums.OpcaoResposta;
import br.com.autoaudit.enums.StatusNC;
import br.com.autoaudit.model.Auditoria;
import br.com.autoaudit.model.ClassificacaoNC;
import br.com.autoaudit.model.NaoConformidade;
import br.com.autoaudit.model.PerguntaChecklist;
import br.com.autoaudit.model.Responsavel;
import br.com.autoaudit.model.RespostaChecklist;
import br.com.autoaudit.service.AuditoriaService;
import br.com.autoaudit.service.NaoConformidadeService;
import br.com.autoaudit.service.RespostaChecklistService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RealizarAuditoriaController {

    private static final int TOTAL_ETAPAS = 4;

    private int etapaAtual = 1;

    private final AuditoriaService auditoriaService =
            new AuditoriaService();

    private final RespostaChecklistService respostaChecklistService =
            new RespostaChecklistService();

    private final NaoConformidadeService naoConformidadeService =
            new NaoConformidadeService();

    private Auditoria auditoria;

    // Carrega a auditoria que será realizada.
    public void carregarAuditoria(Long auditoriaId) {

        auditoria = auditoriaService.buscarPorId(auditoriaId);

        if (auditoria == null) {
            throw new IllegalArgumentException(
                    "Auditoria não encontrada."
            );
        }
    }

    // Retorna a etapa atual da realização.
    public int getEtapaAtual() {
        return etapaAtual;
    }

    // Retorna o total de etapas.
    public int getTotalEtapas() {
        return TOTAL_ETAPAS;
    }

    // Retorna o progresso atual.
    public double getProgresso() {
        return (double) etapaAtual / TOTAL_ETAPAS;
    }

    // Avança para a próxima etapa.
    public void avancarEtapa() {
        if (etapaAtual < TOTAL_ETAPAS) {
            etapaAtual++;
        }
    }

    // Retorna para a etapa anterior.
    public void voltarEtapa() {
        if (etapaAtual > 1) {
            etapaAtual--;
        }
    }

    // Retorna a auditoria carregada.
    public Auditoria getAuditoria() {
        return auditoria;
    }

    // Registra uma resposta para uma pergunta do checklist.
    public void responderPergunta(
            PerguntaChecklist pergunta,
            OpcaoResposta opcao
    ) {

        if (pergunta.getId() == null) {
            throw new IllegalArgumentException(
                    "A pergunta ainda não foi salva."
            );
        }

        List<RespostaChecklist> respostas =
                respostaChecklistService.listarPorPergunta(
                        pergunta.getId()
                );

        if (respostas.isEmpty()) {

            RespostaChecklist resposta =
                    new RespostaChecklist(
                            opcao,
                            pergunta
                    );

            resposta.setDataHoraResposta(
                    LocalDateTime.now()
            );

            respostaChecklistService.salvar(resposta);

        } else {

            RespostaChecklist resposta =
                    respostas.get(respostas.size() - 1);

            resposta.setOpcao(opcao);
            resposta.setDataHoraResposta(
                    LocalDateTime.now()
            );

            respostaChecklistService.atualizar(resposta);
        }
    }

    // Cria uma nova não conformidade identificada durante a auditoria.
    public NaoConformidade criarNaoConformidade(
            String descricao,
            ClassificacaoNC classificacao,
            Responsavel responsavel,
            LocalDate dataIdentificacao,
            LocalDate dataResolucao,
            LocalDate dataEscalonamento,
            String acaoCorretiva,
            StatusNC status
    ) {

        NaoConformidade naoConformidade =
                new NaoConformidade(
                        descricao,
                        classificacao,
                        auditoria
                );

        naoConformidade.setDataIdentificacao(
                dataIdentificacao
        );

        // Calcula automaticamente o prazo de resolução
        // considerando dias úteis e feriados cadastrados.
        naoConformidadeService.definirDataResolucaoAutomatica(
                naoConformidade
        );

        naoConformidade.setResponsavel(
                responsavel
        );

        naoConformidade.setDataResolucao(
                dataResolucao
        );

        naoConformidade.setDataEscalonamento(
                dataEscalonamento
        );

        naoConformidade.setAcaoCorretiva(
                acaoCorretiva
        );

        naoConformidade.setStatus(status);

        naoConformidadeService.salvar(
                naoConformidade
        );

        auditoria.getNaoConformidades()
                .add(naoConformidade);

        return naoConformidade;
    }

    // Finaliza a auditoria e salva sua data final e status.
    public void finalizarAuditoria() {

        if (auditoria == null) {
            throw new IllegalStateException(
                    "Nenhuma auditoria foi carregada."
            );
        }

        auditoriaService.finalizar(
                auditoria.getId()
        );
    }
}