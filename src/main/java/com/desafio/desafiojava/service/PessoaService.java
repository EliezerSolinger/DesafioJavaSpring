package com.desafio.desafiojava.service;

import com.desafio.desafiojava.entity.Pessoa;
import com.desafio.desafiojava.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    @Autowired
    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public Pessoa save(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public Pessoa update(Long id, Pessoa updatedPessoa) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa not found with ID: " + id));
        pessoa.setNome(updatedPessoa.getNome());
        pessoa.setDataNascimento(updatedPessoa.getDataNascimento());
        pessoa.setCpf(updatedPessoa.getCpf());
        pessoa.setFuncionario(updatedPessoa.getFuncionario());
        pessoa.setGerente(updatedPessoa.getGerente());

        return pessoaRepository.save(pessoa);
    }

    public List<Pessoa> findAll() {
        return pessoaRepository.findAllByOrderByIdAsc();
    }

    public Optional<Pessoa> findById(Long id) {
        return pessoaRepository.findById(id);
    }
}
