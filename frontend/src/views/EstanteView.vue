<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="brand">
        <div class="brand-icon">
          <BookOpen :size="24" />
        </div>
        <div>
          <h1>Livora</h1>
          <span>YOUR READING SPACE</span>
        </div>
      </div>

      <div class="menu-section">
        <span class="menu-label">MENU PRINCIPAL</span>

        <nav>
          <RouterLink to="/" class="menu-item">
            <House :size="19" />
            <span>Início</span>
          </RouterLink>

          <RouterLink to="/estante" class="menu-item">
            <Library :size="19" />
            <span>Minha estante</span>
          </RouterLink>

          <a href="#" class="menu-item">
            <BookOpen :size="19" />
            <span>Minhas leituras</span>
          </a>

          <RouterLink to="/explorar" class="menu-item">
            <Search :size="19" />
            <span>Explorar livros</span>
          </RouterLink>

          <a href="#" class="menu-item">
            <Heart :size="19" />
            <span>Lista de desejos</span>
          </a>
        </nav>
      </div>

      <div class="sidebar-footer">
        <div class="quote">
          <Heart :size="17" />
          <p>Entre páginas e histórias, encontre um pouco de você.</p>
        </div>
      </div>
    </aside>

    <main class="main-content">
      <header class="topbar">
        <span>Seu próximo capítulo começa aqui.</span>

        <button class="profile-button">
          <UserRound :size="19" />
        </button>
      </header>

      <section class="page-header">
        <div>
          <span class="eyebrow">SUA COLEÇÃO PARTICULAR</span>
          <h2>Minha estante</h2>
          <p>Todos os livros que fazem parte da sua história.</p>
        </div>

        <RouterLink to="/explorar" class="add-button">
          <Plus :size="18" />
          Adicionar livro
        </RouterLink>
      </section>

      <section class="collection-summary">
        <div>
          <Library :size="23" />
          <div>
            <strong>{{ livros.length }}</strong>
            <span>{{ livros.length === 1 ? 'livro cadastrado' : 'livros cadastrados' }}</span>
          </div>
        </div>

        <span class="summary-message">
          Sua coleção, organizada do seu jeito.
        </span>
      </section>

      <section class="filters">
        <div class="search-box">
          <Search :size="19" />
          <input
            v-model="termoPesquisa"
            type="text"
            placeholder="Pesquisar por título ou autor..."
          />
        </div>

        <select v-model="generoSelecionado">
          <option value="">Todos os gêneros</option>
          <option
            v-for="genero in generos"
            :key="genero"
            :value="genero"
          >
            {{ genero }}
          </option>
        </select>
      </section>

      <div v-if="carregando" class="state-message">
        Carregando sua estante...
      </div>

      <div v-else-if="erro" class="state-message error-message">
        <h3>Não foi possível carregar os livros.</h3>
        <p>{{ erro }}</p>
        <button class="add-button" @click="buscarLivros">
          Tentar novamente
        </button>
      </div>

      <section v-else-if="livrosFiltrados.length > 0" class="books-grid">
        <article
          v-for="livro in livrosFiltrados"
          :key="livro.id"
          class="book-card"
        >
          <div class="cover-container">
            <img
              v-if="livro.capaUrl"
              :src="livro.capaUrl"
              :alt="`Capa do livro ${livro.titulo}`"
              class="book-cover"
              @error="livro.capaUrl = null"
            />

            <div v-else class="book-cover no-cover">
              <BookOpen :size="38" />
              <span>Sem capa</span>
            </div>
          </div>

          <div class="book-info">
            <h3 :title="livro.titulo">{{ livro.titulo }}</h3>
            <p>{{ livro.autor }}</p>
            <span class="genre">{{ livro.genero || 'Sem categoria' }}</span>

            <div class="book-actions">
              <button
                class="edit-button"
                @click="abrirEdicao(livro)"
              >
                <Pencil :size="15" />
                Editar
              </button>

              <button
                class="delete-button"
                @click="excluirLivro(livro)"
                :disabled="excluindoId === livro.id"
              >
                <Trash2 :size="15" />
                {{ excluindoId === livro.id ? 'Excluindo...' : 'Excluir' }}
              </button>
            </div>
          </div>
        </article>
      </section>

      <section v-else class="empty-state">
        <div class="empty-icon">
          <BookHeart :size="38" />
        </div>

        <h3>
          {{ livros.length === 0 ? 'Sua estante está vazia!' : 'Nenhum livro encontrado.' }}
        </h3>

        <p>
          {{
            livros.length === 0
              ? 'Explore novos livros e comece a montar sua coleção.'
              : 'Tente pesquisar com outro título, autor ou gênero.'
          }}
        </p>

        <RouterLink
          v-if="livros.length === 0"
          to="/explorar"
          class="add-button"
        >
          <Plus :size="18" />
          Explorar livros
        </RouterLink>

        <button
          v-else
          class="clear-button"
          @click="limparFiltros"
        >
          Limpar filtros
        </button>
      </section>
    </main>

    <!-- Modal de edição -->
    <div
      v-if="modalAberto"
      class="modal-overlay"
      @click.self="fecharModal"
    >
      <div class="modal">
        <div class="modal-header">
          <div>
            <span class="eyebrow">SUA COLEÇÃO</span>
            <h2>Editar livro</h2>
            <p>Atualize as informações da sua obra.</p>
          </div>

          <button class="close-button" @click="fecharModal">
            <X :size="21" />
          </button>
        </div>

        <form @submit.prevent="salvarEdicao">
          <div class="form-grid">
            <div class="form-group full-width">
              <label for="titulo">Título *</label>
              <input
                id="titulo"
                v-model="form.titulo"
                type="text"
                required
                placeholder="Nome do livro"
              />
            </div>

            <div class="form-group full-width">
              <label for="autor">Autor *</label>
              <input
                id="autor"
                v-model="form.autor"
                type="text"
                required
                placeholder="Nome do autor"
              />
            </div>

            <div class="form-group">
              <label for="editora">Editora</label>
              <input
                id="editora"
                v-model="form.editora"
                type="text"
                placeholder="Editora"
              />
            </div>

            <div class="form-group">
              <label for="genero">Gênero</label>
              <input
                id="genero"
                v-model="form.genero"
                type="text"
                placeholder="Ex.: Romance"
              />
            </div>

            <div class="form-group">
              <label for="isbn">ISBN</label>
              <input
                id="isbn"
                v-model="form.isbn"
                type="text"
                placeholder="ISBN"
              />
            </div>

            <div class="form-group">
              <label for="paginas">Quantidade de páginas</label>
              <input
                id="paginas"
                v-model.number="form.quantidadePaginas"
                type="number"
                min="1"
                placeholder="Ex.: 320"
              />
            </div>

            <div class="form-group">
              <label for="dataPublicacao">Data de publicação</label>
              <input
                id="dataPublicacao"
                v-model="form.dataPublicacao"
                type="date"
              />
            </div>

            <div class="form-group full-width">
              <label for="capaUrl">URL da capa</label>
              <input
                id="capaUrl"
                v-model="form.capaUrl"
                type="url"
                placeholder="https://..."
              />
            </div>

            <div class="form-group full-width">
              <label for="descricao">Descrição</label>
              <textarea
                id="descricao"
                v-model="form.descricao"
                rows="3"
                placeholder="Sobre o livro..."
              ></textarea>
            </div>
          </div>

          <p v-if="erroModal" class="modal-error">
            {{ erroModal }}
          </p>

          <div class="modal-actions">
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
              {{ salvando ? 'Salvando...' : 'Salvar alterações' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { RouterLink } from 'vue-router'
import api from '../services/api'

import {
  BookOpen,
  House,
  Library,
  Search,
  Heart,
  UserRound,
  BookHeart,
  Plus,
  Pencil,
  Trash2,
  X,
  Check
} from '@lucide/vue'

interface Livro {
  id: number
  titulo: string
  autor: string
  isbn: string | null
  editora: string | null
  dataPublicacao: string | null
  genero: string | null
  quantidadePaginas: number | null
  descricao: string | null
  capaUrl: string | null
}

interface LivroForm {
  titulo: string
  autor: string
  isbn: string
  editora: string
  dataPublicacao: string
  genero: string
  quantidadePaginas: number | null
  descricao: string
  capaUrl: string
}

const livros = ref<Livro[]>([])
const carregando = ref(false)
const erro = ref('')

const termoPesquisa = ref('')
const generoSelecionado = ref('')

const modalAberto = ref(false)
const salvando = ref(false)
const excluindoId = ref<number | null>(null)
const erroModal = ref('')
const livroEditandoId = ref<number | null>(null)

const form = reactive<LivroForm>({
  titulo: '',
  autor: '',
  isbn: '',
  editora: '',
  dataPublicacao: '',
  genero: '',
  quantidadePaginas: null,
  descricao: '',
  capaUrl: ''
})

const generos = computed(() => {
  const lista = livros.value
    .map(livro => livro.genero?.trim())
    .filter((genero): genero is string => Boolean(genero))

  return [...new Set(lista)].sort()
})

const livrosFiltrados = computed(() => {
  const termo = termoPesquisa.value.trim().toLowerCase()

  return livros.value.filter(livro => {
    const correspondePesquisa =
      !termo ||
      livro.titulo.toLowerCase().includes(termo) ||
      livro.autor.toLowerCase().includes(termo)

    const correspondeGenero =
      !generoSelecionado.value ||
      livro.genero === generoSelecionado.value

    return correspondePesquisa && correspondeGenero
  })
})

async function buscarLivros() {
  carregando.value = true
  erro.value = ''

  try {
    const resposta = await api.get<Livro[]>('/livros')
    livros.value = resposta.data
  } catch (error) {
    console.error('Erro ao buscar livros:', error)
    erro.value = 'Verifique se o backend está funcionando.'
  } finally {
    carregando.value = false
  }
}

function abrirEdicao(livro: Livro) {
  livroEditandoId.value = livro.id
  erroModal.value = ''

  Object.assign(form, {
    titulo: livro.titulo,
    autor: livro.autor,
    isbn: livro.isbn || '',
    editora: livro.editora || '',
    dataPublicacao: livro.dataPublicacao || '',
    genero: livro.genero || '',
    quantidadePaginas: livro.quantidadePaginas,
    descricao: livro.descricao || '',
    capaUrl: livro.capaUrl || ''
  })

  modalAberto.value = true
}

function fecharModal() {
  if (salvando.value) return

  modalAberto.value = false
  livroEditandoId.value = null
  erroModal.value = ''
}

async function salvarEdicao() {
  if (livroEditandoId.value === null) return

  salvando.value = true
  erroModal.value = ''

  const payload = {
    titulo: form.titulo.trim(),
    autor: form.autor.trim(),
    isbn: form.isbn.trim() || null,
    editora: form.editora.trim() || null,
    dataPublicacao: form.dataPublicacao || null,
    genero: form.genero.trim() || null,
    quantidadePaginas: form.quantidadePaginas || null,
    descricao: form.descricao.trim() || null,
    capaUrl: form.capaUrl.trim() || null
  }

  try {
    await api.put(`/livros/${livroEditandoId.value}`, payload)

    await buscarLivros()
    fecharModal()
  } catch (error: any) {
    console.error('Erro ao editar livro:', error)

    erroModal.value =
      error.response?.data?.mensagem ||
      error.response?.data?.message ||
      'Não foi possível salvar as alterações.'
  } finally {
    salvando.value = false
  }
}

async function excluirLivro(livro: Livro) {
  const confirmar = window.confirm(
    `Tem certeza que deseja excluir "${livro.titulo}" da sua estante?`
  )

  if (!confirmar) return

  excluindoId.value = livro.id

  try {
    await api.delete(`/livros/${livro.id}`)
    livros.value = livros.value.filter(item => item.id !== livro.id)
  } catch (error: any) {
    console.error('Erro ao excluir livro:', error)

    window.alert(
      error.response?.data?.mensagem ||
      error.response?.data?.message ||
      'Não foi possível excluir o livro.'
    )
  } finally {
    excluindoId.value = null
  }
}

function limparFiltros() {
  termoPesquisa.value = ''
  generoSelecionado.value = ''
}

onMounted(() => {
  buscarLivros()
})
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
  background: var(--background);
}

.sidebar {
  width: 255px;
  min-height: 100vh;
  background: white;
  border-right: 1px solid #f0e5eb;
  padding: 32px 20px;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 55px;
}

.brand-icon {
  width: 45px;
  height: 45px;
  background: var(--pink-light);
  color: var(--pink-dark);
  border-radius: 13px;
  display: grid;
  place-items: center;
}

.brand h1 {
  font-family: 'Playfair Display', serif;
  font-size: 25px;
  color: var(--pink-dark);
}

.brand span,
.menu-label,
.eyebrow {
  font-size: 10px;
  letter-spacing: 1.5px;
  color: var(--text-light);
  font-weight: 700;
}

.menu-label {
  display: block;
  margin: 0 0 16px 10px;
}

nav {
  display: flex;
  flex-direction: column;
  gap: 7px;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 13px;
  padding: 13px 12px;
  border-radius: 10px;
  color: var(--text-light);
  font-size: 14px;
  transition: 0.2s;
}

.menu-item:hover,
.menu-item.router-link-active {
  background: var(--pink-light);
  color: var(--pink-dark);
}

.sidebar-footer {
  margin-top: auto;
}

.quote {
  padding: 18px;
  background: #fff5f9;
  border-radius: 13px;
  color: var(--pink-dark);
}

.quote p {
  font-size: 12px;
  line-height: 1.6;
  margin-top: 10px;
}

.main-content {
  flex: 1;
  padding: 30px 45px;
  max-width: 1600px;
  margin: 0 auto;
  min-width: 0;
}

.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: var(--text-light);
  font-size: 13px;
  margin-bottom: 35px;
}

.profile-button {
  width: 42px;
  height: 42px;
  border: 1px solid #f0e5eb;
  background: white;
  color: var(--pink-dark);
  border-radius: 50%;
  display: grid;
  place-items: center;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  margin-bottom: 28px;
}

.page-header h2 {
  font-family: 'Playfair Display', serif;
  font-size: 34px;
  color: var(--text);
  margin: 9px 0;
}

.page-header p {
  color: var(--text-light);
  font-size: 14px;
}

.add-button,
.save-button {
  background: var(--pink-dark);
  color: white;
  border: none;
  border-radius: 9px;
  padding: 12px 18px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-size: 13px;
}

.collection-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border: 1px solid #f0e5eb;
  border-radius: 14px;
  padding: 19px 22px;
  margin-bottom: 25px;
}

.collection-summary > div {
  display: flex;
  align-items: center;
  gap: 14px;
  color: var(--pink-dark);
}

.collection-summary strong,
.collection-summary span {
  display: block;
}

.collection-summary strong {
  font-size: 23px;
  color: var(--text);
}

.collection-summary span {
  font-size: 12px;
  color: var(--text-light);
}

.summary-message {
  color: var(--text-light);
}

.filters {
  display: flex;
  gap: 14px;
  margin-bottom: 28px;
}

.search-box {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 10px;
  background: white;
  border: 1px solid #f0e5eb;
  border-radius: 10px;
  padding: 0 14px;
  color: var(--pink-dark);
}

.search-box input {
  width: 100%;
  height: 45px;
  border: none;
  outline: none;
  background: transparent;
  color: var(--text);
  font-size: 13px;
}

.filters select {
  min-width: 190px;
  border: 1px solid #f0e5eb;
  border-radius: 10px;
  padding: 0 12px;
  background: white;
  color: var(--text);
  outline: none;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(175px, 1fr));
  gap: 22px;
}

.book-card {
  background: white;
  border: 1px solid #f0e5eb;
  border-radius: 14px;
  padding: 12px;
  overflow: hidden;
  transition: 0.2s;
}

.book-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 22px #a94f7d12;
}

.cover-container {
  width: 100%;
}

.book-cover {
  width: 100%;
  height: 220px;
  object-fit: cover;
  border-radius: 8px;
  background: var(--pink-light);
}

.no-cover {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: var(--pink-dark);
  font-size: 12px;
}

.book-info {
  padding: 13px 3px 3px;
}

.book-info h3 {
  font-family: 'Playfair Display', serif;
  font-size: 15px;
  color: var(--text);
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.book-info p {
  font-size: 12px;
  color: var(--text-light);
  margin-bottom: 9px;
}

.genre {
  display: inline-block;
  background: var(--pink-light);
  color: var(--pink-dark);
  border-radius: 20px;
  padding: 5px 9px;
  font-size: 10px;
}

.book-actions {
  display: flex;
  gap: 7px;
  margin-top: 15px;
}

.edit-button,
.delete-button {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  padding: 8px 5px;
  border-radius: 7px;
  font-size: 11px;
}

.edit-button {
  border: 1px solid #e8c9d9;
  background: #fff5f9;
  color: var(--pink-dark);
}

.delete-button {
  border: 1px solid #f1d5d5;
  background: #fff7f7;
  color: #bd5c68;
}

.delete-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.state-message,
.empty-state {
  background: white;
  border: 1px dashed #e8c9d9;
  border-radius: 16px;
  padding: 45px 20px;
  text-align: center;
  color: var(--text-light);
}

.error-message h3,
.empty-state h3 {
  font-family: 'Playfair Display', serif;
  color: var(--text);
  font-size: 21px;
  margin-bottom: 10px;
}

.empty-state p {
  font-size: 13px;
  margin-bottom: 20px;
}

.empty-icon {
  width: 65px;
  height: 65px;
  border-radius: 50%;
  background: var(--pink-light);
  color: var(--pink-dark);
  display: grid;
  place-items: center;
  margin: 0 auto 18px;
}

.clear-button {
  border: none;
  background: transparent;
  color: var(--pink-dark);
  font-size: 13px;
  text-decoration: underline;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  z-index: 100;
  background: #392b3480;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.modal {
  background: white;
  width: 100%;
  max-width: 650px;
  max-height: 90vh;
  overflow-y: auto;
  border-radius: 18px;
  padding: 28px;
  box-shadow: 0 15px 50px #392b3425;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 25px;
}

.modal-header h2 {
  font-family: 'Playfair Display', serif;
  color: var(--text);
  font-size: 25px;
  margin: 7px 0;
}

.modal-header p {
  font-size: 13px;
  color: var(--text-light);
}

.close-button {
  border: none;
  background: var(--pink-light);
  color: var(--pink-dark);
  width: 35px;
  height: 35px;
  border-radius: 50%;
  display: grid;
  place-items: center;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 7px;
}

.full-width {
  grid-column: 1 / -1;
}

.form-group label {
  color: var(--text);
  font-size: 12px;
  font-weight: 600;
}

.form-group input,
.form-group textarea {
  width: 100%;
  border: 1px solid #eadce4;
  border-radius: 8px;
  padding: 11px;
  font-size: 13px;
  outline: none;
  color: var(--text);
  background: white;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: var(--pink);
  box-shadow: 0 0 0 3px #d979a81a;
}

.form-group textarea {
  resize: vertical;
}

.modal-error {
  color: #bd4555;
  font-size: 12px;
  margin-top: 15px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 25px;
}

.cancel-button {
  background: white;
  color: var(--text-light);
  border: 1px solid #eadce4;
  border-radius: 9px;
  padding: 11px 17px;
  font-size: 13px;
}

.save-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@media (max-width: 1100px) {
  .sidebar {
    width: 220px;
  }

  .main-content {
    padding: 25px;
  }
}

@media (max-width: 750px) {
  .sidebar {
    display: none;
  }

  .main-content {
    padding: 20px;
  }

  .page-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .filters {
    flex-direction: column;
  }

  .filters select {
    height: 45px;
  }

  .summary-message {
    display: none;
  }
}

@media (max-width: 500px) {
  .form-grid {
    grid-template-columns: 1fr;
  }

  .full-width {
    grid-column: auto;
  }

  .modal {
    padding: 20px;
  }

  .modal-actions {
    flex-direction: column-reverse;
  }

  .modal-actions button {
    width: 100%;
  }
}
</style>
