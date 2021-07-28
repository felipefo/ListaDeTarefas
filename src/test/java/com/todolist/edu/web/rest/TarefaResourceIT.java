package com.todolist.edu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.todolist.edu.IntegrationTest;
import com.todolist.edu.domain.Authority;
import com.todolist.edu.domain.Categoria;
import com.todolist.edu.domain.Tarefa;
import com.todolist.edu.domain.User;
import com.todolist.edu.domain.enumeration.Status;
import com.todolist.edu.repository.AuthorityRepository;
import com.todolist.edu.repository.TarefaRepository;
import com.todolist.edu.security.AuthoritiesConstants;
import com.todolist.edu.service.criteria.TarefaCriteria;
import com.todolist.edu.service.dto.TarefaDTO;
import com.todolist.edu.service.mapper.TarefaMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TarefaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TarefaResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO_CURTA = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO_CURTA = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_DE_FIM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_DE_FIM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_DE_CRIACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_DE_CRIACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Status DEFAULT_STATUS = Status.AFAZER;
    private static final Status UPDATED_STATUS = Status.FAZENDO;

    private static final String ENTITY_API_URL = "/api/tarefas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private TarefaRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    

    @Autowired
    private TarefaMapper tarefaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTarefaMockMvc;

    private Tarefa tarefa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarefa createEntity(EntityManager em) {
            
        User user = new User();
        user.setId(2L);
        Tarefa tarefa = new Tarefa()
            .descricao(DEFAULT_DESCRICAO)
            .descricaoCurta(DEFAULT_DESCRICAO_CURTA)
            .dataDeFim(DEFAULT_DATA_DE_FIM)
            .dataDeCriacao(DEFAULT_DATA_DE_CRIACAO)
            .status(DEFAULT_STATUS);
        tarefa.setDono(user);
        return tarefa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarefa createUpdatedEntity(EntityManager em) {
        Tarefa tarefa = new Tarefa()
            .descricao(UPDATED_DESCRICAO)
            .descricaoCurta(UPDATED_DESCRICAO_CURTA)
            .dataDeFim(UPDATED_DATA_DE_FIM)
            .dataDeCriacao(UPDATED_DATA_DE_CRIACAO)
            .status(UPDATED_STATUS);
        return tarefa;
    }

    @BeforeEach
    public void initTest() {
        tarefa = createEntity(em);
    }

    @Test
    @Transactional
    void createTarefa() throws Exception {
        int databaseSizeBeforeCreate = tarefaRepository.findAll().size();
        // Create the Tarefa
        TarefaDTO tarefaDTO = tarefaMapper.toDto(tarefa);
        restTarefaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarefaDTO)))
            .andExpect(status().isCreated());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeCreate + 1);
        Tarefa testTarefa = tarefaList.get(tarefaList.size() - 1);
        assertThat(testTarefa.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testTarefa.getDescricaoCurta()).isEqualTo(DEFAULT_DESCRICAO_CURTA);
        assertThat(testTarefa.getDataDeFim()).isEqualTo(DEFAULT_DATA_DE_FIM);
        assertThat(testTarefa.getDataDeCriacao()).isEqualTo(DEFAULT_DATA_DE_CRIACAO);
        assertThat(testTarefa.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createTarefaWithExistingId() throws Exception {
        // Create the Tarefa with an existing ID
        tarefa.setId(1L);
        TarefaDTO tarefaDTO = tarefaMapper.toDto(tarefa);

        int databaseSizeBeforeCreate = tarefaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTarefaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarefaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tarefaRepository.findAll().size();
        // set the field null
        tarefa.setDescricao(null);

        // Create the Tarefa, which fails.
        TarefaDTO tarefaDTO = tarefaMapper.toDto(tarefa);

        restTarefaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarefaDTO)))
            .andExpect(status().isBadRequest());

        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescricaoCurtaIsRequired() throws Exception {
        int databaseSizeBeforeTest = tarefaRepository.findAll().size();
        // set the field null
        tarefa.setDescricaoCurta(null);

        // Create the Tarefa, which fails.
        TarefaDTO tarefaDTO = tarefaMapper.toDto(tarefa);

        restTarefaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarefaDTO)))
            .andExpect(status().isBadRequest());

        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = tarefaRepository.findAll().size();
        // set the field null
        tarefa.setStatus(null);

        // Create the Tarefa, which fails.
        TarefaDTO tarefaDTO = tarefaMapper.toDto(tarefa);

        restTarefaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarefaDTO)))
            .andExpect(status().isBadRequest());

        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTarefas() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList
        restTarefaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarefa.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].descricaoCurta").value(hasItem(DEFAULT_DESCRICAO_CURTA)))
            .andExpect(jsonPath("$.[*].dataDeFim").value(hasItem(DEFAULT_DATA_DE_FIM.toString())))
            .andExpect(jsonPath("$.[*].dataDeCriacao").value(hasItem(DEFAULT_DATA_DE_CRIACAO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getTarefa() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get the tarefa
        restTarefaMockMvc
            .perform(get(ENTITY_API_URL_ID, tarefa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tarefa.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.descricaoCurta").value(DEFAULT_DESCRICAO_CURTA))
            .andExpect(jsonPath("$.dataDeFim").value(DEFAULT_DATA_DE_FIM.toString()))
            .andExpect(jsonPath("$.dataDeCriacao").value(DEFAULT_DATA_DE_CRIACAO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getTarefasByIdFiltering() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        Long id = tarefa.getId();

        defaultTarefaShouldBeFound("id.equals=" + id);
        defaultTarefaShouldNotBeFound("id.notEquals=" + id);

        defaultTarefaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTarefaShouldNotBeFound("id.greaterThan=" + id);

        defaultTarefaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTarefaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTarefasByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where descricao equals to DEFAULT_DESCRICAO
        defaultTarefaShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the tarefaList where descricao equals to UPDATED_DESCRICAO
        defaultTarefaShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTarefasByDescricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where descricao not equals to DEFAULT_DESCRICAO
        defaultTarefaShouldNotBeFound("descricao.notEquals=" + DEFAULT_DESCRICAO);

        // Get all the tarefaList where descricao not equals to UPDATED_DESCRICAO
        defaultTarefaShouldBeFound("descricao.notEquals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTarefasByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultTarefaShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the tarefaList where descricao equals to UPDATED_DESCRICAO
        defaultTarefaShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTarefasByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where descricao is not null
        defaultTarefaShouldBeFound("descricao.specified=true");

        // Get all the tarefaList where descricao is null
        defaultTarefaShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllTarefasByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where descricao contains DEFAULT_DESCRICAO
        defaultTarefaShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the tarefaList where descricao contains UPDATED_DESCRICAO
        defaultTarefaShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTarefasByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where descricao does not contain DEFAULT_DESCRICAO
        defaultTarefaShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the tarefaList where descricao does not contain UPDATED_DESCRICAO
        defaultTarefaShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTarefasByDescricaoCurtaIsEqualToSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where descricaoCurta equals to DEFAULT_DESCRICAO_CURTA
        defaultTarefaShouldBeFound("descricaoCurta.equals=" + DEFAULT_DESCRICAO_CURTA);

        // Get all the tarefaList where descricaoCurta equals to UPDATED_DESCRICAO_CURTA
        defaultTarefaShouldNotBeFound("descricaoCurta.equals=" + UPDATED_DESCRICAO_CURTA);
    }

    @Test
    @Transactional
    void getAllTarefasByDescricaoCurtaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where descricaoCurta not equals to DEFAULT_DESCRICAO_CURTA
        defaultTarefaShouldNotBeFound("descricaoCurta.notEquals=" + DEFAULT_DESCRICAO_CURTA);

        // Get all the tarefaList where descricaoCurta not equals to UPDATED_DESCRICAO_CURTA
        defaultTarefaShouldBeFound("descricaoCurta.notEquals=" + UPDATED_DESCRICAO_CURTA);
    }

    @Test
    @Transactional
    void getAllTarefasByDescricaoCurtaIsInShouldWork() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where descricaoCurta in DEFAULT_DESCRICAO_CURTA or UPDATED_DESCRICAO_CURTA
        defaultTarefaShouldBeFound("descricaoCurta.in=" + DEFAULT_DESCRICAO_CURTA + "," + UPDATED_DESCRICAO_CURTA);

        // Get all the tarefaList where descricaoCurta equals to UPDATED_DESCRICAO_CURTA
        defaultTarefaShouldNotBeFound("descricaoCurta.in=" + UPDATED_DESCRICAO_CURTA);
    }

    @Test
    @Transactional
    void getAllTarefasByDescricaoCurtaIsNullOrNotNull() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where descricaoCurta is not null
        defaultTarefaShouldBeFound("descricaoCurta.specified=true");

        // Get all the tarefaList where descricaoCurta is null
        defaultTarefaShouldNotBeFound("descricaoCurta.specified=false");
    }

    @Test
    @Transactional
    void getAllTarefasByDescricaoCurtaContainsSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where descricaoCurta contains DEFAULT_DESCRICAO_CURTA
        defaultTarefaShouldBeFound("descricaoCurta.contains=" + DEFAULT_DESCRICAO_CURTA);

        // Get all the tarefaList where descricaoCurta contains UPDATED_DESCRICAO_CURTA
        defaultTarefaShouldNotBeFound("descricaoCurta.contains=" + UPDATED_DESCRICAO_CURTA);
    }

    @Test
    @Transactional
    void getAllTarefasByDescricaoCurtaNotContainsSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where descricaoCurta does not contain DEFAULT_DESCRICAO_CURTA
        defaultTarefaShouldNotBeFound("descricaoCurta.doesNotContain=" + DEFAULT_DESCRICAO_CURTA);

        // Get all the tarefaList where descricaoCurta does not contain UPDATED_DESCRICAO_CURTA
        defaultTarefaShouldBeFound("descricaoCurta.doesNotContain=" + UPDATED_DESCRICAO_CURTA);
    }

    @Test
    @Transactional
    void getAllTarefasByDataDeFimIsEqualToSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where dataDeFim equals to DEFAULT_DATA_DE_FIM
        defaultTarefaShouldBeFound("dataDeFim.equals=" + DEFAULT_DATA_DE_FIM);

        // Get all the tarefaList where dataDeFim equals to UPDATED_DATA_DE_FIM
        defaultTarefaShouldNotBeFound("dataDeFim.equals=" + UPDATED_DATA_DE_FIM);
    }

    @Test
    @Transactional
    void getAllTarefasByDataDeFimIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where dataDeFim not equals to DEFAULT_DATA_DE_FIM
        defaultTarefaShouldNotBeFound("dataDeFim.notEquals=" + DEFAULT_DATA_DE_FIM);

        // Get all the tarefaList where dataDeFim not equals to UPDATED_DATA_DE_FIM
        defaultTarefaShouldBeFound("dataDeFim.notEquals=" + UPDATED_DATA_DE_FIM);
    }

    @Test
    @Transactional
    void getAllTarefasByDataDeFimIsInShouldWork() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where dataDeFim in DEFAULT_DATA_DE_FIM or UPDATED_DATA_DE_FIM
        defaultTarefaShouldBeFound("dataDeFim.in=" + DEFAULT_DATA_DE_FIM + "," + UPDATED_DATA_DE_FIM);

        // Get all the tarefaList where dataDeFim equals to UPDATED_DATA_DE_FIM
        defaultTarefaShouldNotBeFound("dataDeFim.in=" + UPDATED_DATA_DE_FIM);
    }

    @Test
    @Transactional
    void getAllTarefasByDataDeFimIsNullOrNotNull() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where dataDeFim is not null
        defaultTarefaShouldBeFound("dataDeFim.specified=true");

        // Get all the tarefaList where dataDeFim is null
        defaultTarefaShouldNotBeFound("dataDeFim.specified=false");
    }

    @Test
    @Transactional
    void getAllTarefasByDataDeCriacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where dataDeCriacao equals to DEFAULT_DATA_DE_CRIACAO
        defaultTarefaShouldBeFound("dataDeCriacao.equals=" + DEFAULT_DATA_DE_CRIACAO);

        // Get all the tarefaList where dataDeCriacao equals to UPDATED_DATA_DE_CRIACAO
        defaultTarefaShouldNotBeFound("dataDeCriacao.equals=" + UPDATED_DATA_DE_CRIACAO);
    }

    @Test
    @Transactional
    void getAllTarefasByDataDeCriacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where dataDeCriacao not equals to DEFAULT_DATA_DE_CRIACAO
        defaultTarefaShouldNotBeFound("dataDeCriacao.notEquals=" + DEFAULT_DATA_DE_CRIACAO);

        // Get all the tarefaList where dataDeCriacao not equals to UPDATED_DATA_DE_CRIACAO
        defaultTarefaShouldBeFound("dataDeCriacao.notEquals=" + UPDATED_DATA_DE_CRIACAO);
    }

    @Test
    @Transactional
    void getAllTarefasByDataDeCriacaoIsInShouldWork() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where dataDeCriacao in DEFAULT_DATA_DE_CRIACAO or UPDATED_DATA_DE_CRIACAO
        defaultTarefaShouldBeFound("dataDeCriacao.in=" + DEFAULT_DATA_DE_CRIACAO + "," + UPDATED_DATA_DE_CRIACAO);

        // Get all the tarefaList where dataDeCriacao equals to UPDATED_DATA_DE_CRIACAO
        defaultTarefaShouldNotBeFound("dataDeCriacao.in=" + UPDATED_DATA_DE_CRIACAO);
    }

    @Test
    @Transactional
    void getAllTarefasByDataDeCriacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where dataDeCriacao is not null
        defaultTarefaShouldBeFound("dataDeCriacao.specified=true");

        // Get all the tarefaList where dataDeCriacao is null
        defaultTarefaShouldNotBeFound("dataDeCriacao.specified=false");
    }

    @Test
    @Transactional
    void getAllTarefasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where status equals to DEFAULT_STATUS
        defaultTarefaShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the tarefaList where status equals to UPDATED_STATUS
        defaultTarefaShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTarefasByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where status not equals to DEFAULT_STATUS
        defaultTarefaShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the tarefaList where status not equals to UPDATED_STATUS
        defaultTarefaShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTarefasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTarefaShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the tarefaList where status equals to UPDATED_STATUS
        defaultTarefaShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTarefasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList where status is not null
        defaultTarefaShouldBeFound("status.specified=true");

        // Get all the tarefaList where status is null
        defaultTarefaShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTarefasByDonoIsEqualToSomething() throws Exception {
        // Initialize the database
       
        tarefaRepository.saveAndFlush(tarefa);
        User dono = UserResourceIT.createEntity(em);
        //em.persist(dono);
        //em.flush();
        tarefa.setDono(dono);
        tarefaRepository.saveAndFlush(tarefa);
        Long donoId = dono.getId();

        // Get all the tarefaList where dono equals to donoId
        defaultTarefaShouldBeFound("donoId.equals=" + donoId);

        // Get all the tarefaList where dono equals to (donoId + 1)
        //defaultTarefaShouldNotBeFound("donoId.equals=" + (donoId + 1));
    }

    @Test
    @Transactional
    void getAllTarefasByResponsavelIsEqualToSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);
        User responsavel = UserResourceIT.createEntity(em);
        em.persist(responsavel);
        em.flush();
        User dono = UserResourceIT.createEntity(em);
        tarefa.setDono(dono);
        tarefa.setResponsavel(responsavel);
        tarefaRepository.saveAndFlush(tarefa);
        Long responsavelId = responsavel.getId();

        // Get all the tarefaList where responsavel equals to responsavelId
        defaultTarefaShouldBeFound("responsavelId.equals=" + responsavelId);

        // Get all the tarefaList where responsavel equals to (responsavelId + 1)
        defaultTarefaShouldNotBeFound("responsavelId.equals=" + (responsavelId + 1));
    }

    @Test
    @Transactional
    void getAllTarefasByCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);
        Categoria categoria = CategoriaResourceIT.createEntity(em);
        em.persist(categoria);
        em.flush();
        tarefa.setCategoria(categoria);
        tarefaRepository.saveAndFlush(tarefa);
        Long categoriaId = categoria.getId();

        // Get all the tarefaList where categoria equals to categoriaId
        defaultTarefaShouldBeFound("categoriaId.equals=" + categoriaId);

        // Get all the tarefaList where categoria equals to (categoriaId + 1)
        defaultTarefaShouldNotBeFound("categoriaId.equals=" + (categoriaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTarefaShouldBeFound(String filter) throws Exception {
        restTarefaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarefa.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].descricaoCurta").value(hasItem(DEFAULT_DESCRICAO_CURTA)))
            .andExpect(jsonPath("$.[*].dataDeFim").value(hasItem(DEFAULT_DATA_DE_FIM.toString())))
            .andExpect(jsonPath("$.[*].dataDeCriacao").value(hasItem(DEFAULT_DATA_DE_CRIACAO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restTarefaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTarefaShouldNotBeFound(String filter) throws Exception {
        restTarefaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTarefaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTarefa() throws Exception {
        // Get the tarefa
        restTarefaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTarefa() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        int databaseSizeBeforeUpdate = tarefaRepository.findAll().size();

        // Update the tarefa
        Tarefa updatedTarefa = tarefaRepository.findById(tarefa.getId()).get();
        // Disconnect from session so that the updates on updatedTarefa are not directly saved in db
        em.detach(updatedTarefa);
        updatedTarefa
            .descricao(UPDATED_DESCRICAO)
            .descricaoCurta(UPDATED_DESCRICAO_CURTA)
            .dataDeFim(UPDATED_DATA_DE_FIM)
            .dataDeCriacao(UPDATED_DATA_DE_CRIACAO)
            .status(UPDATED_STATUS);
        TarefaDTO tarefaDTO = tarefaMapper.toDto(updatedTarefa);

        restTarefaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarefaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarefaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeUpdate);
        Tarefa testTarefa = tarefaList.get(tarefaList.size() - 1);
        assertThat(testTarefa.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTarefa.getDescricaoCurta()).isEqualTo(UPDATED_DESCRICAO_CURTA);
        assertThat(testTarefa.getDataDeFim()).isEqualTo(UPDATED_DATA_DE_FIM);
        assertThat(testTarefa.getDataDeCriacao()).isEqualTo(UPDATED_DATA_DE_CRIACAO);
        assertThat(testTarefa.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingTarefa() throws Exception {
        int databaseSizeBeforeUpdate = tarefaRepository.findAll().size();
        tarefa.setId(count.incrementAndGet());

        // Create the Tarefa
        TarefaDTO tarefaDTO = tarefaMapper.toDto(tarefa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarefaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarefaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarefaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTarefa() throws Exception {
        int databaseSizeBeforeUpdate = tarefaRepository.findAll().size();
        tarefa.setId(count.incrementAndGet());

        // Create the Tarefa
        TarefaDTO tarefaDTO = tarefaMapper.toDto(tarefa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarefaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarefaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTarefa() throws Exception {
        int databaseSizeBeforeUpdate = tarefaRepository.findAll().size();
        tarefa.setId(count.incrementAndGet());

        // Create the Tarefa
        TarefaDTO tarefaDTO = tarefaMapper.toDto(tarefa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarefaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarefaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTarefaWithPatch() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        int databaseSizeBeforeUpdate = tarefaRepository.findAll().size();

        // Update the tarefa using partial update
        Tarefa partialUpdatedTarefa = new Tarefa();
        partialUpdatedTarefa.setId(tarefa.getId());

        partialUpdatedTarefa.descricao(UPDATED_DESCRICAO).descricaoCurta(UPDATED_DESCRICAO_CURTA);

        restTarefaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarefa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarefa))
            )
            .andExpect(status().isOk());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeUpdate);
        Tarefa testTarefa = tarefaList.get(tarefaList.size() - 1);
        assertThat(testTarefa.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTarefa.getDescricaoCurta()).isEqualTo(UPDATED_DESCRICAO_CURTA);
        assertThat(testTarefa.getDataDeFim()).isEqualTo(DEFAULT_DATA_DE_FIM);
        assertThat(testTarefa.getDataDeCriacao()).isEqualTo(DEFAULT_DATA_DE_CRIACAO);
        assertThat(testTarefa.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateTarefaWithPatch() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        int databaseSizeBeforeUpdate = tarefaRepository.findAll().size();

        // Update the tarefa using partial update
        Tarefa partialUpdatedTarefa = new Tarefa();
        partialUpdatedTarefa.setId(tarefa.getId());

        partialUpdatedTarefa
            .descricao(UPDATED_DESCRICAO)
            .descricaoCurta(UPDATED_DESCRICAO_CURTA)
            .dataDeFim(UPDATED_DATA_DE_FIM)
            .dataDeCriacao(UPDATED_DATA_DE_CRIACAO)
            .status(UPDATED_STATUS);

        restTarefaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarefa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarefa))
            )
            .andExpect(status().isOk());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeUpdate);
        Tarefa testTarefa = tarefaList.get(tarefaList.size() - 1);
        assertThat(testTarefa.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTarefa.getDescricaoCurta()).isEqualTo(UPDATED_DESCRICAO_CURTA);
        assertThat(testTarefa.getDataDeFim()).isEqualTo(UPDATED_DATA_DE_FIM);
        assertThat(testTarefa.getDataDeCriacao()).isEqualTo(UPDATED_DATA_DE_CRIACAO);
        assertThat(testTarefa.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingTarefa() throws Exception {
        int databaseSizeBeforeUpdate = tarefaRepository.findAll().size();
        tarefa.setId(count.incrementAndGet());

        // Create the Tarefa
        TarefaDTO tarefaDTO = tarefaMapper.toDto(tarefa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarefaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tarefaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tarefaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTarefa() throws Exception {
        int databaseSizeBeforeUpdate = tarefaRepository.findAll().size();
        tarefa.setId(count.incrementAndGet());

        // Create the Tarefa
        TarefaDTO tarefaDTO = tarefaMapper.toDto(tarefa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarefaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tarefaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTarefa() throws Exception {
        int databaseSizeBeforeUpdate = tarefaRepository.findAll().size();
        tarefa.setId(count.incrementAndGet());

        // Create the Tarefa
        TarefaDTO tarefaDTO = tarefaMapper.toDto(tarefa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarefaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tarefaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTarefa() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        int databaseSizeBeforeDelete = tarefaRepository.findAll().size();

        // Delete the tarefa
        restTarefaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tarefa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
