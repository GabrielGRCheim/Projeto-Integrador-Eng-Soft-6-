package com.senai.automotiva.repositories;

import com.senai.automotiva.entities.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Long> {
    Optional<Carro> findByPlaca(String placa);
    boolean existsByPlaca(String placa);
    List<Carro> findByClienteId(Long clienteId);
    List<Carro> findByMarcaContainingIgnoreCase(String marca);
    List<Carro> findByModeloContainingIgnoreCase(String modelo);
}
