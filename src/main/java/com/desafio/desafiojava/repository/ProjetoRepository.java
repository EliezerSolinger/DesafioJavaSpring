package com.desafio.desafiojava.repository;

import com.desafio.desafiojava.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
    List<Projeto> findAllByOrderByIdAsc(); // Ordena por ID de forma crescente
}
