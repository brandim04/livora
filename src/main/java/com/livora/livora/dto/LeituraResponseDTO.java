package com.livora.livora.dto;

import java.time.LocalDate;

import com.livora.livora.model.StatusLeitura;

public class LeituraResponseDTO {

    private Long id;
    private Long livroId;
    private String tituloLivro;
    private StatusLeitura status;
    private LocalDate dataInicio;
    private LocalDate dataConclusao;
    private Integer paginaAtual;
    private Double avaliacao;
    private String observacoes;

    public LeituraResponseDTO() {
    }

    public LeituraResponseDTO(
            Long id,
            Long livroId,
            String tituloLivro,
            StatusLeitura status,
            LocalDate dataInicio,
            LocalDate dataConclusao,
            Integer paginaAtual,
            Double avaliacao,
            String observacoes) {
        this.id = id;
        this.livroId = livroId;
        this.tituloLivro = tituloLivro;
        this.status = status;
        this.dataInicio = dataInicio;
        this.dataConclusao = dataConclusao;
        this.paginaAtual = paginaAtual;
        this.avaliacao = avaliacao;
        this.observacoes = observacoes;
    }

    public Long getId() {
        return id;
    }

    public Long getLivroId() {
        return livroId;
    }

    public String getTituloLivro() {
        return tituloLivro;
    }

    public StatusLeitura getStatus() {
        return status;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public Integer getPaginaAtual() {
        return paginaAtual;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public String getObservacoes() {
        return observacoes;
    }
}
