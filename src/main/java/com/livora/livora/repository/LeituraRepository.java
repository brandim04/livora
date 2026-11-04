
package com.livora.livora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.livora.livora.model.Leitura;
import com.livora.livora.model.StatusLeitura;

public interface LeituraRepository extends JpaRepository<Leitura, Long> {

    List<Leitura> findByLivroId(Long livroId);

    List<Leitura> findByStatus(StatusLeitura status);

    boolean existsByLivroIdAndStatusIn(Long livroId, List<StatusLeitura> status);
}
