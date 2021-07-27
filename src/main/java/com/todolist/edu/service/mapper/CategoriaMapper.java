package com.todolist.edu.service.mapper;

import com.todolist.edu.domain.*;
import com.todolist.edu.service.dto.CategoriaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Categoria} and its DTO {@link CategoriaDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface CategoriaMapper extends EntityMapper<CategoriaDTO, Categoria> {
    @Mapping(target = "dono", source = "dono", qualifiedByName = "login")
    CategoriaDTO toDto(Categoria s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoriaDTO toDtoId(Categoria categoria);
}
