package com.livora.livora.dto;

import java.time.LocalDate;

public class LivroResponseDTO {

    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private String editora;
    private LocalDate dataPublicacao;
    private String genero;
    private Integer quantidadePaginas;
    private String descricao;
    private String capaUrl;

    public LivroResponseDTO() {
    }

    public LivroResponseDTO(
            Long id,
            String titulo,
            String autor,
            String isbn,
            String editora,
            LocalDate dataPublicacao,
            String genero,
            Integer quantidadePaginas,
            String descricao,
            String capaUrl) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.editora = editora;
        this.dataPublicacao = dataPublicacao;
        this.genero = genero;
        this.quantidadePaginas = quantidadePaginas;
        this.descricao = descricao;
        this.capaUrl = capaUrl;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getEditora() {
        return editora;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public String getGenero() {
        return genero;
    }

    public Integer getQuantidadePaginas() {
        return quantidadePaginas;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCapaUrl() {
        return capaUrl;
    }
}
