package com.livora.livora.controller;

import com.livora.livora.dto.LivroRequestDTO;
import com.livora.livora.dto.LivroResponseDTO;
import com.livora.livora.dto.LivroPesquisaDTO;
import com.livora.livora.service.LivroService;
import com.livora.livora.service.OpenLibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Livros",
        description = "Gerenciamento da coleção pessoal de livros."
)
@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;
    private final OpenLibraryService openLibraryService;

    public LivroController(
            LivroService livroService,
            OpenLibraryService openLibraryService) {
        this.livroService = livroService;
        this.openLibraryService = openLibraryService;
    }

    @Operation(summary = "Listar todos os livros")
    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listarTodos() {
        return ResponseEntity.ok(livroService.listarTodos());
    }

    @Operation(summary = "Filtrar livros")
    @GetMapping("/filtrar")
    public ResponseEntity<List<LivroResponseDTO>> filtrar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String genero) {
        return ResponseEntity.ok(
                livroService.filtrar(titulo, autor, genero));
    }

    @Operation(
            summary = "Pesquisar livros na Open Library",
            description = "Busca livros pelo título e retorna os dados bibliográficos disponíveis."
    )
    @GetMapping("/pesquisar")
    public ResponseEntity<List<LivroPesquisaDTO>> pesquisar(
            @RequestParam String titulo) {

        if (titulo.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                openLibraryService.pesquisarPorTitulo(titulo));
    }

    @Operation(summary = "Buscar livro por ID")
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    @Operation(summary = "Cadastrar livro")
    @PostMapping
    public ResponseEntity<LivroResponseDTO> cadastrar(
            @Valid @RequestBody LivroRequestDTO livro) {
        LivroResponseDTO novoLivro = livroService.cadastrar(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLivro);
    }

    @Operation(summary = "Atualizar livro")
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LivroRequestDTO livro) {
        return ResponseEntity.ok(livroService.atualizar(id, livro));
    }

    @Operation(summary = "Excluir livro")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        livroService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
