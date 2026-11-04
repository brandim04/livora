package com.livora.livora.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.livora.livora.dto.LivroRequestDTO;
import com.livora.livora.dto.LivroResponseDTO;
import com.livora.livora.exception.ConflitoException;
import com.livora.livora.exception.RecursoNaoEncontradoException;
import com.livora.livora.model.Livro;
import com.livora.livora.repository.LivroRepository;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<LivroResponseDTO> listarTodos() {
        return livroRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    
    public List<LivroResponseDTO> filtrar(String titulo, String autor, String genero) {
        String tituloFiltro = normalizarFiltro(titulo);
        String autorFiltro = normalizarFiltro(autor);
        String generoFiltro = normalizarFiltro(genero);

        return livroRepository.filtrar(tituloFiltro, autorFiltro, generoFiltro)
            .stream()
            .map(this::converterParaDTO)
            .toList();
    }

    private String normalizarFiltro(String valor) {
        if (valor == null || valor.isBlank()) {
            return "";
        }

        return valor.trim();
    }


    public LivroResponseDTO buscarPorId(Long id) {
        return converterParaDTO(buscarEntidadePorId(id));
    }

    public LivroResponseDTO cadastrar(LivroRequestDTO dados) {
        String isbn = normalizarIsbn(dados.getIsbn());
        validarIsbn(isbn, null);

        Livro livro = converterParaEntidade(dados);
        livro.setIsbn(isbn);

        return converterParaDTO(livroRepository.save(livro));
    }

    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dados) {
        Livro livro = buscarEntidadePorId(id);

        String isbn = normalizarIsbn(dados.getIsbn());
        validarIsbn(isbn, id);

        livro.setTitulo(dados.getTitulo());
        livro.setAutor(dados.getAutor());
        livro.setIsbn(isbn);
        livro.setEditora(dados.getEditora());
        livro.setDataPublicacao(dados.getDataPublicacao());
        livro.setGenero(dados.getGenero());
        livro.setQuantidadePaginas(dados.getQuantidadePaginas());
        livro.setDescricao(dados.getDescricao());
        livro.setCapaUrl(dados.getCapaUrl());

        return converterParaDTO(livroRepository.save(livro));
    }

    public void excluir(Long id) {
        Livro livro = buscarEntidadePorId(id);
        livroRepository.delete(livro);
    }

    private Livro buscarEntidadePorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Livro não encontrado com o ID: " + id));
    }

    private void validarIsbn(String isbn, Long idAtual) {
        if (isbn == null) {
            return;
        }

        livroRepository.findByIsbn(isbn).ifPresent(livro -> {
            if (!livro.getId().equals(idAtual)) {
                throw new ConflitoException(
                        "Já existe um livro cadastrado com esse ISBN");
            }
        });
    }

    private String normalizarIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return null;
        }

        return isbn.trim();
    }

    private Livro converterParaEntidade(LivroRequestDTO dados) {
        Livro livro = new Livro();

        livro.setTitulo(dados.getTitulo());
        livro.setAutor(dados.getAutor());
        livro.setIsbn(normalizarIsbn(dados.getIsbn()));
        livro.setEditora(dados.getEditora());
        livro.setDataPublicacao(dados.getDataPublicacao());
        livro.setGenero(dados.getGenero());
        livro.setQuantidadePaginas(dados.getQuantidadePaginas());
        livro.setDescricao(dados.getDescricao());
        livro.setCapaUrl(dados.getCapaUrl());

        return livro;
    }

    private LivroResponseDTO converterParaDTO(Livro livro) {
        return new LivroResponseDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getIsbn(),
                livro.getEditora(),
                livro.getDataPublicacao(),
                livro.getGenero(),
                livro.getQuantidadePaginas(),
                livro.getDescricao(),
                livro.getCapaUrl()
        );
    }
}
