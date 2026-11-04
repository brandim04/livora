
import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface LivroSelecionado {
  titulo: string
  autor: string | null
  isbn: string | null
  editora: string | null
  anoPublicacao: number | null
  descricao: string | null
  capaUrl: string | null
  quantidadePaginas: number | null
  genero: string | null
}

export const useLivroPesquisaStore = defineStore('livroPesquisa', () => {
  const livroSelecionado = ref<LivroSelecionado | null>(null)

  function selecionarLivro(livro: LivroSelecionado) {
    livroSelecionado.value = livro
  }

  function limparSelecao() {
    livroSelecionado.value = null
  }

  return {
    livroSelecionado,
    selecionarLivro,
    limparSelecao
  }
})
