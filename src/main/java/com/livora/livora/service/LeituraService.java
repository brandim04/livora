package com.livora.livora.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.livora.livora.dto.LeituraRequestDTO;
import com.livora.livora.dto.LeituraResponseDTO;
import com.livora.livora.exception.ConflitoException;
import com.livora.livora.exception.RecursoNaoEncontradoException;
import com.livora.livora.model.Leitura;
import com.livora.livora.model.Livro;
import com.livora.livora.model.StatusLeitura;
import com.livora.livora.repository.LeituraRepository;
import com.livora.livora.repository.LivroRepository;

@Service
public class LeituraService {

    private final LeituraRepository leituraRepository;
    private final LivroRepository livroRepository;

    public LeituraService(
            LeituraRepository leituraRepository,
            LivroRepository livroRepository) {
        this.leituraRepository = leituraRepository;
        this.livroRepository = livroRepository;
    }

    public List<LeituraResponseDTO> listarTodas() {
        return leituraRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public LeituraResponseDTO buscarPorId(Long id) {
        return converterParaDTO(buscarEntidadePorId(id));
    }

    public List<LeituraResponseDTO> buscarPorLivro(Long livroId) {
        buscarLivroPorId(livroId);

        return leituraRepository.findByLivroId(livroId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<LeituraResponseDTO> buscarPorStatus(StatusLeitura status) {
        return leituraRepository.findByStatus(status)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public LeituraResponseDTO cadastrar(
            Long livroId, LeituraRequestDTO dados) {

        Livro livro = buscarLivroPorId(livroId);

        Leitura leitura = converterParaEntidade(dados);
        leitura.setLivro(livro);

        validarLeitura(leitura);
        validarLeituraAtiva(livroId, null);
        validarProgresso(leitura, null);
        concluirSeNecessario(leitura);
        validarLeitura(leitura);

        return converterParaDTO(leituraRepository.save(leitura));
    }

    public LeituraResponseDTO atualizar(
            Long id, LeituraRequestDTO dados) {

        Leitura leitura = buscarEntidadePorId(id);
        Integer paginaAnterior = leitura.getPaginaAtual();

        leitura.setStatus(dados.getStatus());
        leitura.setDataInicio(dados.getDataInicio());
        leitura.setDataConclusao(dados.getDataConclusao());
        leitura.setPaginaAtual(dados.getPaginaAtual());
        leitura.setAvaliacao(dados.getAvaliacao());
        leitura.setObservacoes(dados.getObservacoes());

        validarLeitura(leitura);
        validarLeituraAtiva(leitura.getLivro().getId(), id);
        validarProgresso(leitura, paginaAnterior);
        concluirSeNecessario(leitura);
        validarLeitura(leitura);

        return converterParaDTO(leituraRepository.save(leitura));
    }

    public void excluir(Long id) {
        leituraRepository.delete(buscarEntidadePorId(id));
    }

    private Livro buscarLivroPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Livro não encontrado com o ID: " + id));
    }

    private Leitura buscarEntidadePorId(Long id) {
        return leituraRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Leitura não encontrada com o ID: " + id));
    }

    private Leitura converterParaEntidade(LeituraRequestDTO dados) {
        Leitura leitura = new Leitura();

        leitura.setStatus(dados.getStatus());
        leitura.setDataInicio(dados.getDataInicio());
        leitura.setDataConclusao(dados.getDataConclusao());
        leitura.setPaginaAtual(dados.getPaginaAtual());
        leitura.setAvaliacao(dados.getAvaliacao());
        leitura.setObservacoes(dados.getObservacoes());

        return leitura;
    }

    private LeituraResponseDTO converterParaDTO(Leitura leitura) {
        return new LeituraResponseDTO(
                leitura.getId(),
                leitura.getLivro().getId(),
                leitura.getLivro().getTitulo(),
                leitura.getStatus(),
                leitura.getDataInicio(),
                leitura.getDataConclusao(),
                leitura.getPaginaAtual(),
                leitura.getAvaliacao(),
                leitura.getObservacoes()
        );
    }

    private void validarLeitura(Leitura leitura) {
        if (leitura.getStatus() == null) {
            throw new IllegalArgumentException(
                    "O status da leitura é obrigatório");
        }

        if (leitura.getDataInicio() != null
                && leitura.getDataInicio().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "A data de início não pode ser futura");
        }

        if (leitura.getDataConclusao() != null
                && leitura.getDataConclusao().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "A data de conclusão não pode ser futura");
        }

        if (leitura.getDataInicio() != null
                && leitura.getDataConclusao() != null
                && leitura.getDataConclusao()
                .isBefore(leitura.getDataInicio())) {
            throw new IllegalArgumentException(
                    "A conclusão não pode ser anterior ao início");
        }

        if (leitura.getAvaliacao() != null) {
            double avaliacao = leitura.getAvaliacao();

            if (avaliacao < 0 || avaliacao > 5
                    || avaliacao * 2 != Math.floor(avaliacao * 2)) {
                throw new IllegalArgumentException(
                        "A avaliação deve ser de 0 a 5, em intervalos de 0,5");
            }

            if (leitura.getStatus() != StatusLeitura.LIDO) {
                throw new IllegalArgumentException(
                        "A avaliação só pode ser atribuída a uma leitura concluída");
            }
        }

        if ((leitura.getStatus() == StatusLeitura.LENDO
                || leitura.getStatus() == StatusLeitura.RELENDO)
                && leitura.getDataInicio() == null) {
            throw new IllegalArgumentException(
                    "Informe a data de início da leitura");
        }

        if (leitura.getDataConclusao() != null
                && leitura.getDataInicio() == null) {
            throw new IllegalArgumentException(
                    "Informe a data de início antes da conclusão");
        }
    }

    
    private void validarLeituraAtiva(Long livroId, Long leituraAtualId) {
        List<Leitura> leituras = leituraRepository.findByLivroId(livroId);

        boolean existeLeituraAtiva = leituras.stream().anyMatch(leitura ->
            (leitura.getStatus() == StatusLeitura.LENDO
                    || leitura.getStatus() == StatusLeitura.RELENDO)
                    && !leitura.getId().equals(leituraAtualId)
    );

        if (existeLeituraAtiva) {
            throw new ConflitoException(
                "Este livro já possui uma leitura em andamento");
        }
    }


    private void validarProgresso(
            Leitura leitura, Integer paginaAnterior) {

        Integer paginaAtual = leitura.getPaginaAtual();

        if (paginaAtual == null) {
            return;
        }

        if (paginaAtual < 0) {
            throw new IllegalArgumentException(
                    "A página atual não pode ser negativa");
        }

        if (paginaAnterior != null && paginaAtual < paginaAnterior) {
            throw new IllegalArgumentException(
                    "O progresso de leitura não pode diminuir");
        }

        Integer totalPaginas = leitura.getLivro().getQuantidadePaginas();

        if (totalPaginas != null && paginaAtual > totalPaginas) {
            throw new IllegalArgumentException(
                    "A página atual não pode ultrapassar o total de páginas");
        }
    }

    private void concluirSeNecessario(Leitura leitura) {
        Integer totalPaginas = leitura.getLivro().getQuantidadePaginas();
        Integer paginaAtual = leitura.getPaginaAtual();

        if (totalPaginas != null && paginaAtual != null
                && paginaAtual.equals(totalPaginas)) {
            leitura.setStatus(StatusLeitura.LIDO);

            if (leitura.getDataConclusao() == null) {
                leitura.setDataConclusao(LocalDate.now());
            }
        }
    }
}
