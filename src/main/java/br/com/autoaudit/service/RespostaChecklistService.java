package br.com.autoaudit.service;

import br.com.autoaudit.dao.RespostaChecklistDAO;
import br.com.autoaudit.model.RespostaChecklist;

import java.util.List;

public class RespostaChecklistService {

    private final RespostaChecklistDAO respostaChecklistDAO =
            new RespostaChecklistDAO();

    // Salva uma nova resposta vinculada a uma pergunta.
    public void salvar(RespostaChecklist resposta) {
        respostaChecklistDAO.salvar(resposta);
    }

    // Busca uma resposta pelo ID.
    public RespostaChecklist buscarPorId(Long id) {
        return respostaChecklistDAO.buscarPorId(id);
    }

    // Retorna todas as respostas cadastradas.
    public List<RespostaChecklist> listarTodos() {
        return respostaChecklistDAO.listarTodos();
    }

    // Retorna as respostas vinculadas a uma pergunta.
    public List<RespostaChecklist> listarPorPergunta(
            Long perguntaId
    ) {
        return respostaChecklistDAO.listarPorPergunta(perguntaId);
    }

    // Atualiza uma resposta existente.
    public void atualizar(RespostaChecklist resposta) {
        respostaChecklistDAO.atualizar(resposta);
    }

    // Exclui uma resposta pelo ID.
    public void excluir(Long id) {
        respostaChecklistDAO.excluir(id);
    }
}