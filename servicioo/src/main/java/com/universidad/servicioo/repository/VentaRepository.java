package com.universidad.servicioo.repository;
import com.universidad.servicioo.model.Venta;

// Repositorio CRUD de ventas; JpaRepository implementa las operaciones sin SQL manual.
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {}
