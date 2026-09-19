package br.com.autoaudit.service;

import br.com.autoaudit.dao.DocumentoDAO;
import br.com.autoaudit.model.Documento;

import java.util.List;

public class DocumentoService {

    private final DocumentoDAO documentoDAO = new DocumentoDAO();

    // Salva um novo documento.
    public void salvar(Documento documento) {
        documentoDAO.salvar(documento);
    }

    // Busca um documento pelo ID.
    public Documento buscarPorId(Long id) {
        return documentoDAO.buscarPorId(id);
    }

    // Retorna todos os documentos cadastrados.
    public List<Documento> listarTodos() {
        return documentoDAO.listarTodos();
    }

    // Atualiza um documento existente.
    public void atualizar(Documento documento) {
        documentoDAO.atualizar(documento);
    }

    // Exclui um documento pelo ID.
    public void excluir(Long id) {
        documentoDAO.excluir(id);
    }
}