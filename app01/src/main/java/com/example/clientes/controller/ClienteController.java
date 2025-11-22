package com.example.clientes.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clientes.entity.Cliente;
import com.example.clientes.repository.ClienteRepository;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

  private final ClienteRepository repository;

  public ClienteController(ClienteRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<Cliente> list() {
    return repository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Cliente> get(@PathVariable Long id) {
    Optional<Cliente> c = repository.findById(id);
    return c.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Cliente> create(@RequestBody Cliente input) {
    Cliente saved = repository.save(input);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody Cliente input) {
    return repository.findById(id).map(existing -> {
      existing.setNombre(input.getNombre());
      existing.setEmail(input.getEmail());
      existing.setTelefono(input.getTelefono());
      return ResponseEntity.ok(repository.save(existing));
    }).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (!repository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    repository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}

