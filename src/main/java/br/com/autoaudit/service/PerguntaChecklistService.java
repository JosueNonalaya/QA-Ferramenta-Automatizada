package br.com.autoaudit.service;

import br.com.autoaudit.dao.PerguntaChecklistDAO;
import br.com.autoaudit.model.PerguntaChecklist;

import java.util.List;

public class PerguntaChecklistService {

    private final PerguntaChecklistDAO perguntaChecklistDAO =
            new PerguntaChecklistDAO();

    // Salva uma pergunta vinculada ao seu checklist.
    public void salvar(PerguntaChecklist pergunta) {
        perguntaChecklistDAO.salvar(pergunta);
    }

    // Busca uma pergunta pelo ID.
    public PerguntaChecklist buscarPorId(Long id) {
        return perguntaChecklistDAO.buscarPorId(id);
    }

    // Retorna todas as perguntas cadastradas.
    public List<PerguntaChecklist> listarTodos() {
        return perguntaChecklistDAO.listarTodos();
    }

    // Retorna somente as perguntas de um checklist.
    public List<PerguntaChecklist> listarPorChecklist(
            Long checklistId
    ) {
        return perguntaChecklistDAO.listarPorChecklist(checklistId);
    }

    // Atualiza uma pergunta existente.
    public void atualizar(PerguntaChecklist pergunta) {
        perguntaChecklistDAO.atualizar(pergunta);
    }

    // Exclui uma pergunta pelo ID.
    public void excluir(Long id) {
        perguntaChecklistDAO.excluir(id);
    }
}