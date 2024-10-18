package com.desafio.desafiojava;


import com.desafio.desafiojava.controller.PessoaController;
import com.desafio.desafiojava.entity.Pessoa;
import com.desafio.desafiojava.service.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class PessoaControllerTest {

    @InjectMocks
    private PessoaController pessoaController;

    @Mock
    private PessoaService pessoaService;

    private Pessoa pessoa;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicia os mocks
        pessoa = new Pessoa(1L, "Maria Silva", LocalDate.parse("1985-04-15"), "987.654.321-00", true, false);
    }

    @Test
    void testListarPessoas() {
        // Mock do serviço para retornar uma lista de pessoas
        List<Pessoa> pessoas = Arrays.asList(pessoa);
        when(pessoaService.findAll()).thenReturn(pessoas);

        ResponseEntity<List<Pessoa>> response = pessoaController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Maria Silva", response.getBody().get(0).getNome());
    }

    @Test
    void testObterPessoa() {
        // Mock para simular o retorno de uma pessoa por ID
        when(pessoaService.findById(anyLong())).thenReturn(Optional.of(pessoa));

        ResponseEntity<Pessoa> response = pessoaController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Maria Silva", response.getBody().getNome());
    }

    @Test
    void testObterPessoaNotFound() {
        // Mock para simular pessoa não encontrada
        when(pessoaService.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Pessoa> response = pessoaController.getById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCadastrarPessoa() {
        // Mock para simular o cadastro de uma nova pessoa
        when(pessoaService.save(any(Pessoa.class))).thenReturn(pessoa);

        ResponseEntity<Pessoa> response = pessoaController.create(pessoa);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Maria Silva", response.getBody().getNome());
    }

    @Test
    void testEditarPessoa() {
        // Mock para simular a edição de uma pessoa
        when(pessoaService.update(anyLong(), any(Pessoa.class))).thenReturn(pessoa);

        ResponseEntity<Pessoa> response = pessoaController.update(1L, pessoa);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Maria Silva", response.getBody().getNome());
    }

}
