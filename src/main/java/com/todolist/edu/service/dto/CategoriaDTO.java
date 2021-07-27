package com.todolist.edu.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.todolist.edu.domain.Categoria} entity.
 */
public class CategoriaDTO implements Serializable {

    private Long id;

    @NotNull
    private String descricao;

    private UserDTO dono;

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

    public UserDTO getDono() {
        return dono;
    }

    public void setDono(UserDTO dono) {
        this.dono = dono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaDTO)) {
            return false;
        }

        CategoriaDTO categoriaDTO = (CategoriaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoriaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", dono=" + getDono() +
            "}";
    }
}
