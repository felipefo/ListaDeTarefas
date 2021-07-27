package com.todolist.edu.service.mapper;

import com.todolist.edu.domain.*;
import com.todolist.edu.service.dto.TarefaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tarefa} and its DTO {@link TarefaDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, CategoriaMapper.class })
public interface TarefaMapper extends EntityMapper<TarefaDTO, Tarefa> {
    @Mapping(target = "dono", source = "dono", qualifiedByName = "login")
    @Mapping(target = "responsavel", source = "responsavel", qualifiedByName = "login")
    @Mapping(target = "categoria", source = "categoria", qualifiedByName = "id")
    TarefaDTO toDto(Tarefa s);
}
