package br.com.autoaudit.model;

import java.util.ArrayList;
import java.util.List;

public class Checklist {

    private Long id;
    private String nome;
    private List<PerguntaChecklist> perguntas;

    public Checklist() {
        this.perguntas = new ArrayList<>();
    }

    public Checklist(String nome) {
        this.nome = nome;
        this.perguntas = new ArrayList<>();
    }

    //GETTER's e SETTER's
    public void adicionarPergunta(PerguntaChecklist pergunta) {
        perguntas.add(pergunta);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<PerguntaChecklist> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<PerguntaChecklist> perguntas) {
        this.perguntas = perguntas;
    }
}