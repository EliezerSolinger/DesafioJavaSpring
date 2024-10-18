package com.desafio.desafiojava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.objenesis.instantiator.annotations.Instantiator;

import java.time.LocalDate;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "pessoa")
public class Pessoa implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // Gera 'BIGSERIAL' no PostgreSQL
  private Long id;

  @Column(nullable = false, length = 100)
  private String nome;

  private LocalDate dataNascimento;

  @Column(length = 14)
  private String cpf;

  private Boolean funcionario;
  private Boolean gerente;

  public Pessoa(Long id, String nome, LocalDate dataNascimento, String cpf, Boolean funcionario, Boolean gerente) {
    this.id = id;
    this.nome = nome;
    this.dataNascimento = dataNascimento;
    this.cpf = cpf;
    this.funcionario = funcionario;
    this.gerente = gerente;
  }

  public Pessoa(Long id) {
    this.id=id;
  }
  public Pessoa() {
  }

}