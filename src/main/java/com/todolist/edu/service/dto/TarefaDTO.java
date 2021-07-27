package com.todolist.edu.service.dto;

import com.todolist.edu.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.todolist.edu.domain.Tarefa} entity.
 */
public class TarefaDTO implements Serializable {

    private Long id;

    @NotNull
    private String descricao;

    @NotNull
    private String descricaoCurta;

    private Instant dataDeFim;

    private Instant dataDeCriacao;

    @NotNull
    private Status status;

    private UserDTO dono;

    private UserDTO responsavel;

    private CategoriaDTO categoria;

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

    public String getDescricaoCurta() {
        return descricaoCurta;
    }

    public void setDescricaoCurta(String descricaoCurta) {
        this.descricaoCurta = descricaoCurta;
    }

    public Instant getDataDeFim() {
        return dataDeFim;
    }

    public void setDataDeFim(Instant dataDeFim) {
        this.dataDeFim = dataDeFim;
    }

    public Instant getDataDeCriacao() {
        return dataDeCriacao;
    }

    public void setDataDeCriacao(Instant dataDeCriacao) {
        this.dataDeCriacao = dataDeCriacao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UserDTO getDono() {
        return dono;
    }

    public void setDono(UserDTO dono) {
        this.dono = dono;
    }

    public UserDTO getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(UserDTO responsavel) {
        this.responsavel = responsavel;
    }

    public CategoriaDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDTO categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TarefaDTO)) {
            return false;
        }

        TarefaDTO tarefaDTO = (TarefaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tarefaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TarefaDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", descricaoCurta='" + getDescricaoCurta() + "'" +
            ", dataDeFim='" + getDataDeFim() + "'" +
            ", dataDeCriacao='" + getDataDeCriacao() + "'" +
            ", status='" + getStatus() + "'" +
            ", dono=" + getDono() +
            ", responsavel=" + getResponsavel() +
            ", categoria=" + getCategoria() +
            "}";
    }
}
