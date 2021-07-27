package com.todolist.edu.cucumber;

import com.todolist.edu.ListaDeTarefasApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = ListaDeTarefasApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
