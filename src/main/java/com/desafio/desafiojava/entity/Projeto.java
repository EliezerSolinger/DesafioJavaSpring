package com.desafio.desafiojava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "projeto")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(name = "data_inicio")
    private Date dataInicio;

    @Column(name = "data_previsao_fim")
    private Date dataPrevisaoFim;

    @Column(name = "data_fim")
    private Date dataFim;

    @Column(length = 5000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private StatusProjeto status;

    private Float orcamento;



    @ManyToOne
    @JoinColumn(name = "idgerente", nullable = true)
    private Pessoa gerente;

    @ManyToMany
    @JoinTable(
            name = "projeto_membros",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "pessoa_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"projeto_id", "pessoa_id"}) // Garantir unicidade
    )
    private List<Pessoa> membros;

    // Getters e Setters
    // ...
    public boolean canBeDeleted() {
        return !(status == StatusProjeto.INICIADO ||
                status == StatusProjeto.EM_ANDAMENTO ||
                status == StatusProjeto.ENCERRADO);
    }


    public String getRiskClassification() {
        if (orcamento < 100000) {
            return "Baixo Risco";
        } else if (orcamento >= 100000 && orcamento <= 500000) {
            return "Médio Risco";
        } else {
            return "Alto Risco";
        }
    }

}
