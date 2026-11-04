package com.livora.livora.service;

import tools.jackson.databind.JsonNode;
import com.livora.livora.dto.LivroPesquisaDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenLibraryService {

    private final RestClient restClient = RestClient.create();

    public List<LivroPesquisaDTO> pesquisarPorTitulo(String titulo) {

        URI uri = UriComponentsBuilder
                .fromUriString("https://openlibrary.org/search.json")
                .queryParam("title", titulo)
                .queryParam("fields",
                        "title,author_name,isbn,publisher,first_publish_year,cover_i,number_of_pages_median,subject")
                .queryParam("limit", 10)
                .build()
                .encode()
                .toUri();

        JsonNode resposta = restClient.get()
                .uri(uri)
                .retrieve()
                .body(JsonNode.class);

        List<LivroPesquisaDTO> livros = new ArrayList<>();

        if (resposta == null || !resposta.has("docs")) {
            return livros;
        }

        for (JsonNode item : resposta.get("docs")) {

            String autor = null;
            JsonNode autores = item.path("author_name");

            if (autores.isArray() && !autores.isEmpty()) {
                autor = String.join(", ", obterTextos(autores));
            }

            String isbn = primeiroValor(item.path("isbn"));
            String editora = primeiroValor(item.path("publisher"));
            String genero = primeiroValor(item.path("subject"));

            Integer ano = item.path("first_publish_year").isNumber()
                    ? item.path("first_publish_year").asInt()
                    : null;

            Integer paginas = item.path("number_of_pages_median").isNumber()
                    ? item.path("number_of_pages_median").asInt()
                    : null;

            String capaUrl = null;

            if (item.path("cover_i").isNumber()) {
                capaUrl = "https://covers.openlibrary.org/b/id/"
                        + item.path("cover_i").asInt()
                        + "-M.jpg";
            }

            livros.add(new LivroPesquisaDTO(
                    item.path("title").asText(null),
                    autor,
                    isbn,
                    editora,
                    ano,
                    null,
                    capaUrl,
                    paginas,
                    genero
            ));
        }

        return livros;
    }

    private String primeiroValor(JsonNode valores) {
        if (valores.isArray() && !valores.isEmpty()) {
            return valores.get(0).asText();
        }

        return null;
    }

    private List<String> obterTextos(JsonNode valores) {
        List<String> textos = new ArrayList<>();

        valores.forEach(valor -> textos.add(valor.asText()));

        return textos;
    }
}
