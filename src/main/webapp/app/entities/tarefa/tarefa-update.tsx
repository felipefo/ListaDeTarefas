import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ICategoria } from 'app/shared/model/categoria.model';
import { getEntities as getCategorias } from 'app/entities/categoria/categoria.reducer';
import { getEntity, updateEntity, createEntity, reset } from './tarefa.reducer';
import { ITarefa } from 'app/shared/model/tarefa.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITarefaUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TarefaUpdate = (props: ITarefaUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { tarefaEntity, users, categorias, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/tarefa' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
    props.getCategorias();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.dataDeFim = convertDateTimeToServer(values.dataDeFim);
    values.dataDeCriacao = convertDateTimeToServer(values.dataDeCriacao);

    if (errors.length === 0) {
      const entity = {
        ...tarefaEntity,
        ...values,
        dono: users.find(it => it.id.toString() === values.donoId.toString()),
        responsavel: users.find(it => it.id.toString() === values.responsavelId.toString()),
        categoria: categorias.find(it => it.id.toString() === values.categoriaId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="listaDeTarefasApp.tarefa.home.createOrEditLabel" data-cy="TarefaCreateUpdateHeading">
            <Translate contentKey="listaDeTarefasApp.tarefa.home.createOrEditLabel">Create or edit a Tarefa</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : tarefaEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="tarefa-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="tarefa-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="descricaoLabel" for="tarefa-descricao">
                  <Translate contentKey="listaDeTarefasApp.tarefa.descricao">Descricao</Translate>
                </Label>
                <AvField
                  id="tarefa-descricao"
                  data-cy="descricao"
                  type="text"
                  name="descricao"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="descricaoCurtaLabel" for="tarefa-descricaoCurta">
                  <Translate contentKey="listaDeTarefasApp.tarefa.descricaoCurta">Descricao Curta</Translate>
                </Label>
                <AvField
                  id="tarefa-descricaoCurta"
                  data-cy="descricaoCurta"
                  type="text"
                  name="descricaoCurta"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="dataDeFimLabel" for="tarefa-dataDeFim">
                  <Translate contentKey="listaDeTarefasApp.tarefa.dataDeFim">Data De Fim</Translate>
                </Label>
                <AvInput
                  id="tarefa-dataDeFim"
                  data-cy="dataDeFim"
                  type="datetime-local"
                  className="form-control"
                  name="dataDeFim"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.tarefaEntity.dataDeFim)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="dataDeCriacaoLabel" for="tarefa-dataDeCriacao">
                  <Translate contentKey="listaDeTarefasApp.tarefa.dataDeCriacao">Data De Criacao</Translate>
                </Label>
                <AvInput
                  id="tarefa-dataDeCriacao"
                  data-cy="dataDeCriacao"
                  type="datetime-local"
                  className="form-control"
                  name="dataDeCriacao"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.tarefaEntity.dataDeCriacao)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="tarefa-status">
                  <Translate contentKey="listaDeTarefasApp.tarefa.status">Status</Translate>
                </Label>
                <AvInput
                  id="tarefa-status"
                  data-cy="status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && tarefaEntity.status) || 'AFAZER'}
                >
                  <option value="AFAZER">{translate('listaDeTarefasApp.Status.AFAZER')}</option>
                  <option value="FAZENDO">{translate('listaDeTarefasApp.Status.FAZENDO')}</option>
                  <option value="FEITA">{translate('listaDeTarefasApp.Status.FEITA')}</option>
                  <option value="ARQUIVADA">{translate('listaDeTarefasApp.Status.ARQUIVADA')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="tarefa-dono">
                  <Translate contentKey="listaDeTarefasApp.tarefa.dono">Dono</Translate>
                </Label>
                <AvInput id="tarefa-dono" data-cy="dono" type="select" className="form-control" name="donoId">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.login}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="tarefa-responsavel">
                  <Translate contentKey="listaDeTarefasApp.tarefa.responsavel">Responsavel</Translate>
                </Label>
                <AvInput id="tarefa-responsavel" data-cy="responsavel" type="select" className="form-control" name="responsavelId">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.login}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="tarefa-categoria">
                  <Translate contentKey="listaDeTarefasApp.tarefa.categoria">Categoria</Translate>
                </Label>
                <AvInput id="tarefa-categoria" data-cy="categoria" type="select" className="form-control" name="categoriaId">
                  <option value="" key="0" />
                  {categorias
                    ? categorias.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/tarefa" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  categorias: storeState.categoria.entities,
  tarefaEntity: storeState.tarefa.entity,
  loading: storeState.tarefa.loading,
  updating: storeState.tarefa.updating,
  updateSuccess: storeState.tarefa.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getCategorias,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TarefaUpdate);
