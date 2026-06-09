package com.senai.automotiva.repositories;

import com.senai.automotiva.entities.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PecaRepository extends JpaRepository<Peca, Long> {
    Optional<Peca> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    List<Peca> findByNomeContainingIgnoreCase(String nome);
    List<Peca> findByAtivo(Boolean ativo);

    @Query("SELECT p FROM Peca p WHERE p.quantidadeEstoque <= p.quantidadeMinima AND p.ativo = true")
    List<Peca> findPecasComEstoqueBaixo();
}
