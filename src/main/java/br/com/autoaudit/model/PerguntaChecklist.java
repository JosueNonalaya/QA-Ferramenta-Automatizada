package br.com.autoaudit.model;

public class PerguntaChecklist {

    private Long id;
    private String descricao;
    private int ordem;
    private Checklist checklist;

    public PerguntaChecklist() {
    }

    public PerguntaChecklist(String descricao, int ordem) {
        this.descricao = descricao;
        this.ordem = ordem;
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

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }
}