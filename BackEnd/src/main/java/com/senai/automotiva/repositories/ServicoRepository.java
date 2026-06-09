package com.senai.automotiva.repositories;

import com.senai.automotiva.entities.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByNomeContainingIgnoreCase(String nome);
    List<Servico> findByAtivo(Boolean ativo);
}
