package com.livora.livora.repository;

import com.livora.livora.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    Optional<Livro> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    boolean existsByTituloIgnoreCase(String titulo);

    
 
    @Query("""
        SELECT l FROM Livro l
        WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))
        AND LOWER(l.autor) LIKE LOWER(CONCAT('%', :autor, '%'))
        AND LOWER(l.genero) LIKE LOWER(CONCAT('%', :genero, '%'))
        """)
    List<Livro> filtrar(
        @Param("titulo") String titulo,
        @Param("autor") String autor,
        @Param("genero") String genero
    );

}
