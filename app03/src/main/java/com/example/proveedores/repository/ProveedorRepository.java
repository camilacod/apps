package com.example.proveedores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proveedores.entity.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
}

