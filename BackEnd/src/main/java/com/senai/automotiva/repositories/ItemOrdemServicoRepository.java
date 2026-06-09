package com.senai.automotiva.repositories;

import com.senai.automotiva.entities.ItemOrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemOrdemServicoRepository extends JpaRepository<ItemOrdemServico, Long> {
    List<ItemOrdemServico> findByOrdemServicoId(Long ordemServicoId);
    void deleteByOrdemServicoId(Long ordemServicoId);
}
