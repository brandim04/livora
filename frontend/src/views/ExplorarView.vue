
<template>
  <main class="explore-page">
    <header class="page-header">
      <span class="eyebrow">DESCUBRA NOVAS HISTÓRIAS</span>
      <h1>Explore livros</h1>
      <p>Encontre seu próximo livro favorito e adicione à sua estante.</p>
    </header>

    <section class="search-area">
      <div class="search-box">
        <Search :size="21" />
        <input
          v-model="titulo"
          placeholder="Digite o título de um livro..."
          @keyup.enter="pesquisarLivros"
        />
        <button @click="pesquisarLivros" :disabled="carregando">
          {{ carregando ? 'Buscando...' : 'Pesquisar' }}
        </button>
      </div>
    </section>

    <p v-if="erroPesquisa" class="error-message">
      {{ erroPesquisa }}
    </p>

    <section v-if="livros.length > 0">
      <div class="results-header">
        <div>
          <span class="eyebrow">RESULTADOS DA BUSCA</span>
          <h2>Escolha sua edição</h2>
        </div>
        <span>{{ livros.length }} resultados</span>
      </div>

      <div class="books-grid">
        <article
          v-for="(livro, index) in livros"
          :key="index"
          class="book-card"
          @click="abrirCadastro(livro)"
        >
          <div class="cover-container">
            <img
              v-if="livro.capaUrl"
              :src="livro.capaUrl"
              :alt="livro.titulo"
              class="cover"
            />
            <div v-else class="no-cover">
              <BookOpen :size="40" />
              <span>Sem capa disponível</span>
            </div>
          </div>

          <div class="book-info">
            <h3>{{ livro.titulo }}</h3>
            <p>{{ livro.autor || 'Autor não informado' }}</p>
            <span>{{ livro.anoPublicacao || 'Ano não informado' }}</span>
          </div>

          <span class="select-hint">Selecionar edição</span>
        </article>
      </div>
    </section>

    <section v-else-if="pesquisou && !carregando" class="empty-state">
      <BookOpen :size="40" />
      <h3>Nenhum livro encontrado</h3>
      <p>Tente pesquisar utilizando outro título.</p>
    </section>

    <section v-else-if="!carregando" class="initial-state">
      <BookHeart :size="48" />
      <h3>Qual história você está procurando?</h3>
      <p>Digite o título acima para encontrar diferentes edições e capas.</p>
    </section>

    <Teleport to="body">
      <div
        v-if="modalAberto"
        class="modal-overlay"
        @click.self="fecharModal"
        @keydown.esc="fecharModal"
      >
        <section class="modal">
          <header class="modal-header">
            <div>
              <span class="eyebrow">SUA COLEÇÃO LITERÁRIA</span>
              <h2>Adicionar livro</h2>
              <p>Confira os dados antes de adicionar à sua estante.</p>
            </div>

            <button class="close-button" @click="fecharModal">
              <X :size="21" />
            </button>
          </header>

          <form @submit.prevent="cadastrarLivro">
            <div class="modal-cover-section">
              <div class="modal-cover">
                <img
                  v-if="form.capaUrl"
                  :src="form.capaUrl"
                  alt="Capa selecionada"
                />
                <div v-else class="no-cover">
                  <BookOpen :size="35" />
                  <span>Sem capa</span>
                </div>
              </div>

              <div class="cover-field">
                <label for="capaUrl">URL da capa</label>
                <input
                  id="capaUrl"
                  v-model="form.capaUrl"
                  type="url"
                  placeholder="https://..."
                />
              </div>
            </div>

            <div class="form-divider"></div>

            <div class="form-grid">
              <div class="field full">
                <label for="tituloLivro">Título *</label>
                <input
                  id="tituloLivro"
                  v-model="form.titulo"
                  required
                  maxlength="200"
                />
              </div>

              <div class="field">
                <label for="autor">Autor *</label>
                <input
                  id="autor"
                  v-model="form.autor"
                  required
                  maxlength="150"
                />
              </div>

              <div class="field">
                <label for="isbn">ISBN</label>
                <input
                  id="isbn"
                  v-model="form.isbn"
                />
              </div>

              <div class="field">
                <label for="editora">Editora</label>
                <input
                  id="editora"
                  v-model="form.editora"
                  maxlength="100"
                />
              </div>

              <div class="field">
                <label for="ano">Ano de publicação</label>
                <input
                  id="ano"
                  v-model.number="form.anoPublicacao"
                  type="number"
                  min="1"
                  max="9999"
                />
              </div>

              <div class="field">
                <label for="genero">Gênero</label>
                <input
                  id="genero"
                  v-model="form.genero"
                  maxlength="100"
                />
              </div>

              <div class="field">
                <label for="paginas">Quantidade de páginas</label>
                <input
                  id="paginas"
                  v-model.number="form.quantidadePaginas"
                  type="number"
                  min="1"
                />
              </div>

              <div class="field full">
                <label for="descricao">Descrição</label>
                <textarea
                  id="descricao"
                  v-model="form.descricao"
                  rows="3"
                  maxlength="2000"
                  placeholder="Sobre o livro..."
                ></textarea>
              </div>
            </div>

            <p v-if="erroCadastro" class="error-message">
              {{ erroCadastro }}
            </p>

            <footer class="modal-actions">
              <button
                type="button"
                class="cancel-button"
                @click="fecharModal"
              >
                Cancelar
              </button>

              <button
                type="submit"
                class="save-button"
                :disabled="salvando"
              >
                <Check :size="17" />
                {{ salvando ? 'Salvando...' : 'Adicionar à estante' }}
              </button>
            </footer>
          </form>
        </section>
      </div>
    </Teleport>
  </main>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../services/api'

import {
  Search,
  BookOpen,
  BookHeart,
  Check,
  X
} from '@lucide/vue'

interface LivroPesquisa {
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

const router = useRouter()

const titulo = ref('')
const livros = ref<LivroPesquisa[]>([])
const carregando = ref(false)
const pesquisou = ref(false)
const erroPesquisa = ref('')

const modalAberto = ref(false)
const salvando = ref(false)
const erroCadastro = ref('')

const form = reactive({
  titulo: '',
  autor: '',
  isbn: '',
  editora: '',
  anoPublicacao: null as number | null,
  genero: '',
  quantidadePaginas: null as number | null,
  descricao: '',
  capaUrl: ''
})

async function pesquisarLivros() {
  if (!titulo.value.trim()) {
    erroPesquisa.value = 'Digite o título de um livro.'
    return
  }

  carregando.value = true
  pesquisou.value = false
  erroPesquisa.value = ''
  livros.value = []

  try {
    const resposta = await api.get<LivroPesquisa[]>(
      '/livros/pesquisar',
      {
        params: { titulo: titulo.value.trim() }
      }
    )

    livros.value = resposta.data
    pesquisou.value = true
  } catch (error) {
    console.error('Erro ao pesquisar livros:', error)
    erroPesquisa.value = 'Não foi possível pesquisar. Verifique sua conexão.'
  } finally {
    carregando.value = false
  }
}

function abrirCadastro(livro: LivroPesquisa) {
  form.titulo = livro.titulo || ''
  form.autor = livro.autor || ''
  form.isbn = livro.isbn || ''
  form.editora = livro.editora || ''
  form.anoPublicacao = livro.anoPublicacao
  form.genero = livro.genero || ''
  form.quantidadePaginas = livro.quantidadePaginas
  form.descricao = livro.descricao || ''
  form.capaUrl = livro.capaUrl || ''

  erroCadastro.value = ''
  modalAberto.value = true
}

function fecharModal() {
  if (salvando.value) return
  modalAberto.value = false
}

async function cadastrarLivro() {
  erroCadastro.value = ''

  if (!form.titulo.trim() || !form.autor.trim()) {
    erroCadastro.value = 'Título e autor são obrigatórios.'
    return
  }

  if (
    form.anoPublicacao !== null &&
    (!Number.isInteger(form.anoPublicacao) ||
      form.anoPublicacao < 1 ||
      form.anoPublicacao > 9999)
  ) {
    erroCadastro.value = 'Informe um ano de publicação válido.'
    return
  }

  if (
    form.quantidadePaginas !== null &&
    (!Number.isInteger(form.quantidadePaginas) ||
      form.quantidadePaginas < 1)
  ) {
    erroCadastro.value = 'Informe uma quantidade de páginas válida.'
    return
  }

  const dados = {
    titulo: form.titulo.trim(),
    autor: form.autor.trim(),
    isbn: form.isbn.trim() || null,
    editora: form.editora.trim() || null,
    dataPublicacao: form.anoPublicacao
      ? `${form.anoPublicacao}-01-01`
      : null,
    genero: form.genero.trim() || null,
    quantidadePaginas: form.quantidadePaginas,
    descricao: form.descricao.trim() || null,
    capaUrl: form.capaUrl.trim() || null
  }

  salvando.value = true

  try {
    await api.post('/livros', dados)

    modalAberto.value = false
    router.push('/')
  } catch (error: any) {
    console.error('Erro ao cadastrar livro:', error)

    erroCadastro.value =
      error.response?.data?.erro ||
      error.response?.data?.mensagem ||
      'Não foi possível cadastrar o livro. Verifique os dados.'
  } finally {
    salvando.value = false
  }
}
</script>

<style scoped>
.explore-page {
  min-height: 100vh;
  padding: 45px;
  background: var(--background);
}

.page-header h1 {
  font-family: 'Playfair Display', serif;
  font-size: 34px;
  color: var(--text);
  margin: 10px 0;
}

.page-header p,
.initial-state p,
.empty-state p {
  color: var(--text-light);
  font-size: 14px;
}

.search-area {
  margin: 32px 0 45px;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 13px;
  max-width: 750px;
  padding: 10px 12px 10px 18px;
  background: white;
  border: 1px solid #eedce6;
  border-radius: 12px;
  color: var(--pink-dark);
}

.search-box input {
  flex: 1;
  min-width: 0;
  border: none;
  outline: none;
  color: var(--text);
  font-size: 14px;
}

.search-box button {
  background: var(--pink-dark);
  color: white;
  border: none;
  border-radius: 9px;
  padding: 12px 18px;
  cursor: pointer;
}

.search-box button:disabled,
.save-button:disabled {
  opacity: 0.6;
  cursor: wait;
}

.results-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 25px;
}

.results-header h2 {
  font-family: 'Playfair Display', serif;
  font-size: 24px;
  margin-top: 7px;
}

.results-header > span {
  color: var(--text-light);
  font-size: 13px;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(165px, 1fr));
  gap: 22px;
}

.book-card {
  background: white;
  border: 1px solid #f0e5eb;
  border-radius: 13px;
  padding: 12px;
  cursor: pointer;
  transition: 0.2s;
}

.book-card:hover {
  transform: translateY(-4px);
  border-color: var(--pink);
  box-shadow: 0 5px 18px #d979a81c;
}

.cover {
  width: 100%;
  height: 225px;
  object-fit: cover;
  border-radius: 7px;
}

.no-cover {
  height: 225px;
  background: var(--pink-light);
  color: var(--pink-dark);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 11px;
  border-radius: 7px;
}

.book-info {
  padding: 12px 3px 5px;
}

.book-info h3 {
  font-family: 'Playfair Display', serif;
  font-size: 15px;
  margin-bottom: 6px;
}

.book-info p,
.book-info span {
  display: block;
  color: var(--text-light);
  font-size: 12px;
  margin-top: 5px;
}

.select-hint {
  display: block;
  text-align: center;
  color: var(--pink-dark);
  font-size: 11px;
  margin-top: 12px;
  padding: 8px;
  background: var(--pink-light);
  border-radius: 7px;
}

.initial-state,
.empty-state {
  text-align: center;
  padding: 65px 20px;
  color: var(--pink-dark);
}

.initial-state h3,
.empty-state h3 {
  font-family: 'Playfair Display', serif;
  color: var(--text);
  margin: 18px 0 8px;
}

.error-message {
  color: #c03958;
  margin: 15px 0;
  font-size: 13px;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  z-index: 1000;
  background: #30202c88;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  overflow-y: auto;
}

.modal {
  width: 100%;
  max-width: 760px;
  max-height: 90vh;
  overflow-y: auto;
  background: white;
  border-radius: 18px;
  padding: 30px;
  box-shadow: 0 15px 60px #0002;
}

.modal-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 25px;
}

.modal-header h2 {
  font-family: 'Playfair Display', serif;
  font-size: 27px;
  color: var(--text);
  margin: 8px 0;
}

.modal-header p {
  color: var(--text-light);
  font-size: 13px;
}

.close-button {
  border: none;
  background: var(--pink-light);
  color: var(--pink-dark);
  width: 38px;
  height: 38px;
  border-radius: 50%;
  display: grid;
  place-items: center;
  cursor: pointer;
}

.modal-cover-section {
  display: flex;
  align-items: center;
  gap: 22px;
  margin-bottom: 25px;
}

.modal-cover {
  width: 105px;
  height: 140px;
  flex-shrink: 0;
  border-radius: 8px;
  overflow: hidden;
  background: var(--pink-light);
}

.modal-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.modal-cover .no-cover {
  height: 100%;
}

.cover-field {
  flex: 1;
}

.cover-field label,
.field label {
  display: block;
  font-size: 12px;
  font-weight: 600;
  color: var(--text);
  margin-bottom: 8px;
}

.cover-field input,
.field input,
.field textarea {
  width: 100%;
  padding: 12px 13px;
  border: 1px solid #eadde5;
  border-radius: 9px;
  outline: none;
  background: #fffdfd;
  color: var(--text);
  font-size: 13px;
}

.cover-field input:focus,
.field input:focus,
.field textarea:focus {
  border-color: var(--pink);
}

.form-divider {
  height: 1px;
  background: #f0e5eb;
  margin: 22px 0;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 18px;
}

.field.full {
  grid-column: 1 / -1;
}

.field textarea {
  resize: vertical;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 25px;
  padding-top: 20px;
  border-top: 1px solid #f0e5eb;
}

.cancel-button,
.save-button {
  padding: 12px 18px;
  border-radius: 9px;
  font-size: 13px;
  cursor: pointer;
}

.cancel-button {
  border: 1px solid #eadde5;
  background: white;
  color: var(--text-light);
}

.save-button {
  border: none;
  background: var(--pink-dark);
  color: white;
  display: flex;
  align-items: center;
  gap: 8px;
}

@media (max-width: 600px) {
  .explore-page {
    padding: 25px 18px;
  }

  .search-box {
    flex-wrap: wrap;
  }

  .search-box input {
    width: 70%;
  }

  .search-box button {
    width: 100%;
  }

  .modal {
    padding: 20px;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .field.full {
    grid-column: auto;
  }

  .modal-actions {
    flex-direction: column-reverse;
  }

  .modal-actions button {
    justify-content: center;
  }
}
</style>
