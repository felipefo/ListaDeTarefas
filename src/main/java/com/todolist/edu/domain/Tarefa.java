package com.todolist.edu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.todolist.edu.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Tarefa.
 */
@Entity
@Table(name = "tarefa")
public class Tarefa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @NotNull
    @Column(name = "descricao_curta", nullable = false)
    private String descricaoCurta;

    @Column(name = "data_de_fim")
    private Instant dataDeFim;

    @Column(name = "data_de_criacao")
    private Instant dataDeCriacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne
    private User dono;

    @ManyToOne
    private User responsavel;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tarefas", "dono" }, allowSetters = true)
    private Categoria categoria;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tarefa id(Long id) {
        this.id = id;
        return this;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Tarefa descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricaoCurta() {
        return this.descricaoCurta;
    }

    public Tarefa descricaoCurta(String descricaoCurta) {
        this.descricaoCurta = descricaoCurta;
        return this;
    }

    public void setDescricaoCurta(String descricaoCurta) {
        this.descricaoCurta = descricaoCurta;
    }

    public Instant getDataDeFim() {
        return this.dataDeFim;
    }

    public Tarefa dataDeFim(Instant dataDeFim) {
        this.dataDeFim = dataDeFim;
        return this;
    }

    public void setDataDeFim(Instant dataDeFim) {
        this.dataDeFim = dataDeFim;
    }

    public Instant getDataDeCriacao() {
        return this.dataDeCriacao;
    }

    public Tarefa dataDeCriacao(Instant dataDeCriacao) {
        this.dataDeCriacao = dataDeCriacao;
        return this;
    }

    public void setDataDeCriacao(Instant dataDeCriacao) {
        this.dataDeCriacao = dataDeCriacao;
    }

    public Status getStatus() {
        return this.status;
    }

    public Tarefa status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getDono() {
        return this.dono;
    }

    public Tarefa dono(User user) {
        this.setDono(user);
        return this;
    }

    public void setDono(User user) {
        this.dono = user;
    }

    public User getResponsavel() {
        return this.responsavel;
    }

    public Tarefa responsavel(User user) {
        this.setResponsavel(user);
        return this;
    }

    public void setResponsavel(User user) {
        this.responsavel = user;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public Tarefa categoria(Categoria categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tarefa)) {
            return false;
        }
        return id != null && id.equals(((Tarefa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tarefa{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", descricaoCurta='" + getDescricaoCurta() + "'" +
            ", dataDeFim='" + getDataDeFim() + "'" +
            ", dataDeCriacao='" + getDataDeCriacao() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
