package com.desafio.desafiojava.service;

import com.desafio.desafiojava.entity.Pessoa;
import com.desafio.desafiojava.entity.Projeto;
import com.desafio.desafiojava.entity.StatusProjeto;
import com.desafio.desafiojava.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    @Autowired
    public ProjetoService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    public Projeto save(Projeto projeto) {
        return projetoRepository.save(projeto);
    }

    public Projeto update(Long id, Projeto updatedProjeto) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto not found with ID: " + id));

        projeto.setNome(updatedProjeto.getNome());
        projeto.setDataInicio(updatedProjeto.getDataInicio());
        projeto.setDataPrevisaoFim(updatedProjeto.getDataPrevisaoFim());
        projeto.setDataFim(updatedProjeto.getDataFim());
        projeto.setDescricao(updatedProjeto.getDescricao());
        projeto.setStatus(updatedProjeto.getStatus());
        projeto.setOrcamento(updatedProjeto.getOrcamento());

        projeto.setGerente(updatedProjeto.getGerente());
        //projeto.setMembros(updatedProjeto.getMembros());

        return projetoRepository.save(projeto);
    }

    public List<Projeto> findAll() {
        return projetoRepository.findAllByOrderByIdAsc();
    }

    public Optional<Projeto> findById(Long id) {
        return projetoRepository.findById(id);
    }
    public static String getReadableProjectStatus(StatusProjeto status) {
        if (status == null) {
            return "Status Desconhecido";
        }

        switch (status) {
            case EM_ANALISE:
                return "Em Análise";
            case ANALISE_REALIZADA:
                return "Análise Realizada";
            case ANALISE_APROVADA:
                return "Análise Aprovada";
            case INICIADO:
                return "Iniciado";
            case PLANEJADO:
                return "Planejado";
            case EM_ANDAMENTO:
                return "Em Andamento";
            case ENCERRADO:
                return "Encerrado";
            case CANCELADO:
                return "Cancelado";
            default:
                return "Status Desconhecido";
        }
    }
    public void delete(Long id) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto not found with ID: " + id));

        if (!projeto.canBeDeleted()) {
            throw new RuntimeException("Não é possivel deletar um projeto com status: " + getReadableProjectStatus(projeto.getStatus()));
        }

        projetoRepository.deleteById(id);
    }

    public Projeto addMember(Long projectId, Pessoa member) {
        Projeto projeto = projetoRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        projeto.getMembros().add(member);
        return projetoRepository.save(projeto);
    }

    public Projeto removeMember(Long projectId, Pessoa member) {
        Projeto projeto = projetoRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        projeto.getMembros().remove(member);

        List<Pessoa> updated_members=new ArrayList<>();

        for(var m : projeto.getMembros()) {
            if(!m.getId().equals(member.getId())) updated_members.add(m);
        }
        projeto.setMembros(updated_members);


        return projetoRepository.save(projeto);
    }
}
