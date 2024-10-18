package com.desafio.desafiojava.repository;

import com.desafio.desafiojava.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    List<Pessoa> findAllByOrderByIdAsc(); // Ordena por ID de forma crescente
}