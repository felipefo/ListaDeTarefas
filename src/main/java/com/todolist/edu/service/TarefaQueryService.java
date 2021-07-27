package com.todolist.edu.service;

import com.todolist.edu.domain.*; // for static metamodels
import com.todolist.edu.domain.Tarefa;
import com.todolist.edu.repository.TarefaRepository;
import com.todolist.edu.service.criteria.TarefaCriteria;
import com.todolist.edu.service.dto.TarefaDTO;
import com.todolist.edu.service.mapper.TarefaMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Tarefa} entities in the database.
 * The main input is a {@link TarefaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TarefaDTO} or a {@link Page} of {@link TarefaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TarefaQueryService extends QueryService<Tarefa> {

    private final Logger log = LoggerFactory.getLogger(TarefaQueryService.class);

    private final TarefaRepository tarefaRepository;

    private final TarefaMapper tarefaMapper;

    public TarefaQueryService(TarefaRepository tarefaRepository, TarefaMapper tarefaMapper) {
        this.tarefaRepository = tarefaRepository;
        this.tarefaMapper = tarefaMapper;
    }

    /**
     * Return a {@link List} of {@link TarefaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TarefaDTO> findByCriteria(TarefaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tarefa> specification = createSpecification(criteria);
        return tarefaMapper.toDto(tarefaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TarefaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TarefaDTO> findByCriteria(TarefaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tarefa> specification = createSpecification(criteria);
        return tarefaRepository.findAll(specification, page).map(tarefaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TarefaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tarefa> specification = createSpecification(criteria);
        return tarefaRepository.count(specification);
    }

    /**
     * Function to convert {@link TarefaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tarefa> createSpecification(TarefaCriteria criteria) {
        Specification<Tarefa> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tarefa_.id));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Tarefa_.descricao));
            }
            if (criteria.getDescricaoCurta() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricaoCurta(), Tarefa_.descricaoCurta));
            }
            if (criteria.getDataDeFim() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataDeFim(), Tarefa_.dataDeFim));
            }
            if (criteria.getDataDeCriacao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataDeCriacao(), Tarefa_.dataDeCriacao));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Tarefa_.status));
            }
            if (criteria.getDonoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDonoId(), root -> root.join(Tarefa_.dono, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getResponsavelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getResponsavelId(), root -> root.join(Tarefa_.responsavel, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getCategoriaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoriaId(),
                            root -> root.join(Tarefa_.categoria, JoinType.LEFT).get(Categoria_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
