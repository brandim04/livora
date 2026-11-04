package com.livora.livora.dto;

public class LivroPesquisaDTO {

    private String titulo;
    private String autor;
    private String isbn;
    private String editora;
    private Integer anoPublicacao;
    private String descricao;
    private String capaUrl;
    private Integer quantidadePaginas;
    private String genero;

    public LivroPesquisaDTO() {
    }

    public LivroPesquisaDTO(
            String titulo,
            String autor,
            String isbn,
            String editora,
            Integer anoPublicacao,
            String descricao,
            String capaUrl,
            Integer quantidadePaginas,
            String genero) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.editora = editora;
        this.anoPublicacao = anoPublicacao;
        this.descricao = descricao;
        this.capaUrl = capaUrl;
        this.quantidadePaginas = quantidadePaginas;
        this.genero = genero;
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

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCapaUrl() {
        return capaUrl;
    }

    public Integer getQuantidadePaginas() {
        return quantidadePaginas;
    }

    public String getGenero() {
        return genero;
    }
}