package com.senai.automotiva.repositories;

import com.senai.automotiva.entities.OrdemServico;
import com.senai.automotiva.enums.StatusOrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
    Optional<OrdemServico> findByNumeroOs(String numeroOs);
    List<OrdemServico> findByStatus(StatusOrdemServico status);
    List<OrdemServico> findByCarroId(Long carroId);
    List<OrdemServico> findByCarroClienteId(Long clienteId);
    List<OrdemServico> findByResponsavelId(Long responsavelId);

    @Query("SELECT os FROM OrdemServico os WHERE os.status NOT IN ('CONCLUIDA', 'CANCELADA')")
    List<OrdemServico> findOrdensAbertas();
}
