package com.livora.livora.controller;

import com.livora.livora.dto.LeituraRequestDTO;
import com.livora.livora.dto.LeituraResponseDTO;
import com.livora.livora.model.StatusLeitura;
import com.livora.livora.service.LeituraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "Leituras",
    description = "Gerenciamento do histórico de leitura, progresso, avaliações e status dos livros."
)
@RestController
@RequestMapping("/api/leituras")
public class LeituraController {

    private final LeituraService leituraService;

    public LeituraController(LeituraService leituraService) {
        this.leituraService = leituraService;
    }

    @Operation(
        summary = "Listar todas as leituras",
        description = "Retorna o histórico completo de leituras cadastradas."
    )
    @GetMapping
    public ResponseEntity<List<LeituraResponseDTO>> listarTodas() {
        return ResponseEntity.ok(leituraService.listarTodas());
    }

    @Operation(
        summary = "Buscar leitura por ID",
        description = "Retorna os dados de uma leitura específica."
    )
    @GetMapping("/{id}")
    public ResponseEntity<LeituraResponseDTO> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(leituraService.buscarPorId(id));
    }

    @Operation(
        summary = "Buscar leituras por livro",
        description = "Retorna todas as leituras vinculadas ao ID de um livro."
    )
    @GetMapping("/livro/{livroId}")
    public ResponseEntity<List<LeituraResponseDTO>> buscarPorLivro(
            @PathVariable Long livroId) {
        return ResponseEntity.ok(leituraService.buscarPorLivro(livroId));
    }

    @Operation(
        summary = "Filtrar leituras por status",
        description = "Retorna as leituras que possuem o status informado."
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeituraResponseDTO>> buscarPorStatus(
            @PathVariable StatusLeitura status) {
        return ResponseEntity.ok(leituraService.buscarPorStatus(status));
    }

    @Operation(
        summary = "Cadastrar leitura",
        description = "Registra uma nova leitura vinculada a um livro específico."
    )
    @PostMapping("/livro/{livroId}")
    public ResponseEntity<LeituraResponseDTO> cadastrar(
            @PathVariable Long livroId,
            @Valid @RequestBody LeituraRequestDTO leitura) {
        LeituraResponseDTO novaLeitura = leituraService.cadastrar(livroId, leitura);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaLeitura);
    }

    @Operation(
        summary = "Atualizar leitura",
        description = "Atualiza o status, progresso, datas, avaliação e observações de uma leitura."
    )
    @PutMapping("/{id}")
    public ResponseEntity<LeituraResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LeituraRequestDTO leitura) {
        return ResponseEntity.ok(leituraService.atualizar(id, leitura));
    }

    @Operation(
        summary = "Excluir leitura",
        description = "Remove um registro do histórico de leitura."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        leituraService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
