package com.livora.livora.dto;

import java.time.LocalDate;

import com.livora.livora.model.StatusLeitura;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

public class LeituraRequestDTO {

    @NotNull(message = "O status é obrigatório")
    private StatusLeitura status;

    @PastOrPresent(message = "A data de início não pode ser futura")
    private LocalDate dataInicio;

    @PastOrPresent(message = "A data de conclusão não pode ser futura")
    private LocalDate dataConclusao;

    @PositiveOrZero(message = "A página atual não pode ser negativa")
    private Integer paginaAtual;

    @DecimalMin(value = "0.0", message = "A avaliação mínima é 0")
    @DecimalMax(value = "5.0", message = "A avaliação máxima é 5")
    @Digits(integer = 1, fraction = 1)
    private Double avaliacao;

    private String observacoes;

    public LeituraRequestDTO() {
    }

    public StatusLeitura getStatus() {
        return status;
    }

    public void setStatus(StatusLeitura status) {
        this.status = status;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public Integer getPaginaAtual() {
        return paginaAtual;
    }

    public void setPaginaAtual(Integer paginaAtual) {
        this.paginaAtual = paginaAtual;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
