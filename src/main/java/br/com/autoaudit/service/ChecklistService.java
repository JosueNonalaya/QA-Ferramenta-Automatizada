package br.com.autoaudit.service;

import br.com.autoaudit.dao.ChecklistDAO;
import br.com.autoaudit.model.Checklist;

import java.util.List;

public class ChecklistService {

    private final ChecklistDAO checklistDAO = new ChecklistDAO();

    // Salva um novo checklist.
    public void salvar(Checklist checklist) {
        checklistDAO.salvar(checklist);
    }

    // Busca um checklist pelo ID.
    public Checklist buscarPorId(Long id) {
        return checklistDAO.buscarPorId(id);
    }

    // Retorna todos os checklists.
    public List<Checklist> listarTodos() {
        return checklistDAO.listarTodos();
    }

    // Atualiza um checklist existente.
    public void atualizar(Checklist checklist) {
        checklistDAO.atualizar(checklist);
    }

    // Exclui um checklist pelo ID.
    public void excluir(Long id) {
        checklistDAO.excluir(id);
    }
}