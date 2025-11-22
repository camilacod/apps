package com.example.productos.controller;

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

import com.example.productos.entity.Producto;
import com.example.productos.repository.ProductoRepository;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

  private final ProductoRepository repository;

  public ProductoController(ProductoRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<Producto> list() {
    return repository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Producto> get(@PathVariable Long id) {
    Optional<Producto> p = repository.findById(id);
    return p.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Producto> create(@RequestBody Producto input) {
    Producto saved = repository.save(input);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto input) {
    return repository.findById(id).map(existing -> {
      existing.setNombre(input.getNombre());
      existing.setPrecio(input.getPrecio());
      existing.setStock(input.getStock());
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

