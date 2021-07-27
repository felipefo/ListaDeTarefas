package com.todolist.edu.service.criteria;

import com.todolist.edu.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.todolist.edu.domain.Tarefa} entity. This class is used
 * in {@link com.todolist.edu.web.rest.TarefaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tarefas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TarefaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter descricao;

    private StringFilter descricaoCurta;

    private InstantFilter dataDeFim;

    private InstantFilter dataDeCriacao;

    private StatusFilter status;

    private LongFilter donoId;

    private LongFilter responsavelId;

    private LongFilter categoriaId;

    public TarefaCriteria() {}

    public TarefaCriteria(TarefaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.descricaoCurta = other.descricaoCurta == null ? null : other.descricaoCurta.copy();
        this.dataDeFim = other.dataDeFim == null ? null : other.dataDeFim.copy();
        this.dataDeCriacao = other.dataDeCriacao == null ? null : other.dataDeCriacao.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.donoId = other.donoId == null ? null : other.donoId.copy();
        this.responsavelId = other.responsavelId == null ? null : other.responsavelId.copy();
        this.categoriaId = other.categoriaId == null ? null : other.categoriaId.copy();
    }

    @Override
    public TarefaCriteria copy() {
        return new TarefaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescricao() {
        return descricao;
    }

    public StringFilter descricao() {
        if (descricao == null) {
            descricao = new StringFilter();
        }
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public StringFilter getDescricaoCurta() {
        return descricaoCurta;
    }

    public StringFilter descricaoCurta() {
        if (descricaoCurta == null) {
            descricaoCurta = new StringFilter();
        }
        return descricaoCurta;
    }

    public void setDescricaoCurta(StringFilter descricaoCurta) {
        this.descricaoCurta = descricaoCurta;
    }

    public InstantFilter getDataDeFim() {
        return dataDeFim;
    }

    public InstantFilter dataDeFim() {
        if (dataDeFim == null) {
            dataDeFim = new InstantFilter();
        }
        return dataDeFim;
    }

    public void setDataDeFim(InstantFilter dataDeFim) {
        this.dataDeFim = dataDeFim;
    }

    public InstantFilter getDataDeCriacao() {
        return dataDeCriacao;
    }

    public InstantFilter dataDeCriacao() {
        if (dataDeCriacao == null) {
            dataDeCriacao = new InstantFilter();
        }
        return dataDeCriacao;
    }

    public void setDataDeCriacao(InstantFilter dataDeCriacao) {
        this.dataDeCriacao = dataDeCriacao;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public LongFilter getDonoId() {
        return donoId;
    }

    public LongFilter donoId() {
        if (donoId == null) {
            donoId = new LongFilter();
        }
        return donoId;
    }

    public void setDonoId(LongFilter donoId) {
        this.donoId = donoId;
    }

    public LongFilter getResponsavelId() {
        return responsavelId;
    }

    public LongFilter responsavelId() {
        if (responsavelId == null) {
            responsavelId = new LongFilter();
        }
        return responsavelId;
    }

    public void setResponsavelId(LongFilter responsavelId) {
        this.responsavelId = responsavelId;
    }

    public LongFilter getCategoriaId() {
        return categoriaId;
    }

    public LongFilter categoriaId() {
        if (categoriaId == null) {
            categoriaId = new LongFilter();
        }
        return categoriaId;
    }

    public void setCategoriaId(LongFilter categoriaId) {
        this.categoriaId = categoriaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TarefaCriteria that = (TarefaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(descricaoCurta, that.descricaoCurta) &&
            Objects.equals(dataDeFim, that.dataDeFim) &&
            Objects.equals(dataDeCriacao, that.dataDeCriacao) &&
            Objects.equals(status, that.status) &&
            Objects.equals(donoId, that.donoId) &&
            Objects.equals(responsavelId, that.responsavelId) &&
            Objects.equals(categoriaId, that.categoriaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, descricaoCurta, dataDeFim, dataDeCriacao, status, donoId, responsavelId, categoriaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TarefaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (descricaoCurta != null ? "descricaoCurta=" + descricaoCurta + ", " : "") +
            (dataDeFim != null ? "dataDeFim=" + dataDeFim + ", " : "") +
            (dataDeCriacao != null ? "dataDeCriacao=" + dataDeCriacao + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (donoId != null ? "donoId=" + donoId + ", " : "") +
            (responsavelId != null ? "responsavelId=" + responsavelId + ", " : "") +
            (categoriaId != null ? "categoriaId=" + categoriaId + ", " : "") +
            "}";
    }
}
