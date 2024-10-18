package com.desafio.desafiojava.controller;

import com.desafio.desafiojava.entity.Pessoa;
import com.desafio.desafiojava.entity.Projeto;
import com.desafio.desafiojava.service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    @Autowired
    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    // Create a new project
    @PostMapping
    public ResponseEntity<Projeto> create(@RequestBody Projeto projeto) {
        Projeto newProjeto = projetoService.save(projeto);
        return new ResponseEntity<>(newProjeto, HttpStatus.CREATED);
    }

    // Update an existing project
    @PutMapping("/{id}")
    public ResponseEntity<Projeto> update(@PathVariable Long id, @RequestBody Projeto updatedProjeto) {
        try {
            Projeto projeto = projetoService.update(id, updatedProjeto);
            return ResponseEntity.ok(projeto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get all projects
    @GetMapping
    public ResponseEntity<List<Projeto>> getAll() {
        List<Projeto> projetos = projetoService.findAll();
        return ResponseEntity.ok(projetos);
    }

    // Get a project by ID
    @GetMapping("/{id}")
    public ResponseEntity<Projeto> getById(@PathVariable Long id) {
        Optional<Projeto> projeto = projetoService.findById(id);
        return projeto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            projetoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            System.out.println();
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<Projeto> addMember(@PathVariable Long projectId, @RequestBody Pessoa member) {
        Projeto projeto = projetoService.addMember(projectId, member);
        return ResponseEntity.ok(projeto);
    }

    @DeleteMapping("/{projectId}/members")
    public ResponseEntity<Projeto> removeMember(@PathVariable Long projectId, @RequestBody Pessoa member) {
        Projeto projeto = projetoService.removeMember(projectId, member);
        return ResponseEntity.ok(projeto);
    }

    @GetMapping("/{id}/risk")
    public ResponseEntity<String> getRiskClassification(@PathVariable Long id) {
        Optional<Projeto> projeto = projetoService.findById(id);
        return projeto.map(p -> ResponseEntity.ok(p.getRiskClassification()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}

