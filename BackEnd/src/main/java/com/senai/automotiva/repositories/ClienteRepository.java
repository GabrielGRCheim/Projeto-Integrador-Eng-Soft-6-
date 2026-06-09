package com.senai.automotiva.repositories;

import com.senai.automotiva.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    List<Cliente> findByAtivo(Boolean ativo);
}
