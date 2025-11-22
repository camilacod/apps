package com.example.proveedores.controller;

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

import com.example.proveedores.entity.Proveedor;
import com.example.proveedores.repository.ProveedorRepository;

@RestController
@RequestMapping("/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

  private final ProveedorRepository repository;

  public ProveedorController(ProveedorRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<Proveedor> list() {
    return repository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Proveedor> get(@PathVariable Long id) {
    Optional<Proveedor> p = repository.findById(id);
    return p.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Proveedor> create(@RequestBody Proveedor input) {
    Proveedor saved = repository.save(input);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Proveedor> update(@PathVariable Long id, @RequestBody Proveedor input) {
    return repository.findById(id).map(existing -> {
      existing.setNombre(input.getNombre());
      existing.setEmail(input.getEmail());
      existing.setTelefono(input.getTelefono());
      existing.setDireccion(input.getDireccion());
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

