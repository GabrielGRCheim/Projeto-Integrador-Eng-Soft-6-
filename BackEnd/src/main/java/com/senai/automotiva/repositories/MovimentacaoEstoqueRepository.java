package com.senai.automotiva.repositories;

import com.senai.automotiva.entities.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {
    List<MovimentacaoEstoque> findByPecaIdOrderByCriadoEmDesc(Long pecaId);
    List<MovimentacaoEstoque> findByOrdemServicoId(Long ordemServicoId);
}
