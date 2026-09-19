package br.com.autoaudit.controller;

import br.com.autoaudit.enums.OpcaoResposta;
import br.com.autoaudit.enums.StatusNC;
import br.com.autoaudit.model.Auditoria;
import br.com.autoaudit.model.NaoConformidade;
import br.com.autoaudit.model.PerguntaChecklist;
import br.com.autoaudit.model.RespostaChecklist;
import br.com.autoaudit.service.AuditoriaService;
import br.com.autoaudit.service.NaoConformidadeService;
import br.com.autoaudit.service.RespostaChecklistService;

import java.util.ArrayList;
import java.util.List;

public class AuditoriaEspecificaController {

    private final AuditoriaService auditoriaService =
            new AuditoriaService();

    private final NaoConformidadeService naoConformidadeService =
            new NaoConformidadeService();

    private final RespostaChecklistService respostaChecklistService =
            new RespostaChecklistService();

    private Auditoria auditoria;

    // Carrega uma auditoria específica pelo ID.
    public void carregarAuditoria(Long auditoriaId) {

        auditoria = auditoriaService.buscarPorId(
                auditoriaId
        );

        if (auditoria == null) {
            throw new IllegalArgumentException(
                    "Auditoria não encontrada."
            );
        }
    }

    // Retorna a auditoria carregada.
    public Auditoria getAuditoria() {
        return auditoria;
    }

    // Carrega as não conformidades pertencentes à auditoria.
    public List<NaoConformidade> carregarNaoConformidades() {

        if (auditoria == null) {
            return new ArrayList<>();
        }

        List<NaoConformidade> resultado =
                new ArrayList<>();

        for (NaoConformidade nc :
                naoConformidadeService.listarTodos()) {

            if (nc.getAuditoria() != null
                    && nc.getAuditoria().getId()
                    .equals(auditoria.getId())) {

                resultado.add(nc);
            }
        }

        return resultado;
    }

    // Altera o status de uma não conformidade.
    public void alterarStatusNC(
            Long naoConformidadeId,
            StatusNC novoStatus
    ) {

        NaoConformidade nc =
                naoConformidadeService.buscarPorId(
                        naoConformidadeId
                );

        if (nc == null) {
            throw new IllegalArgumentException(
                    "Não conformidade não encontrada."
            );
        }

        nc.setStatus(novoStatus);

        naoConformidadeService.atualizar(nc);
    }

    // Conta quantas não conformidades existem na auditoria.
    public int contarNaoConformidades() {
        return carregarNaoConformidades().size();
    }

    // Retorna a quantidade de respostas SIM.
    public int contarConformes() {
        return contarRespostas(OpcaoResposta.SIM);
    }

    // Retorna a quantidade de respostas NÃO.
    public int contarNaoConformes() {
        return contarRespostas(OpcaoResposta.NAO);
    }

    // Retorna a quantidade de respostas NÃO APLICÁVEL.
    public int contarNaoAplicaveis() {
        return contarRespostas(OpcaoResposta.NAO_APLICAVEL);
    }

    // Conta respostas de uma determinada opção.
    private int contarRespostas(OpcaoResposta opcao) {

        if (auditoria == null
                || auditoria.getChecklist() == null) {

            return 0;
        }

        int total = 0;

        for (PerguntaChecklist pergunta :
                auditoria.getChecklist().getPerguntas()) {

            List<RespostaChecklist> respostas =
                    respostaChecklistService.listarPorPergunta(
                            pergunta.getId()
                    );

            if (!respostas.isEmpty()) {

                RespostaChecklist resposta =
                        respostas.get(respostas.size() - 1);

                if (resposta.getOpcao() == opcao) {
                    total++;
                }
            }
        }

        return total;
    }

    // Retorna o percentual de aderência registrado na auditoria.
    public double getPercentualAderencia() {

        if (auditoria == null) {
            return 0;
        }

        return auditoria.getPercentualAderencia();
    }
}