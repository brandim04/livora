
<template>
  <main class="register-page">
    <header class="page-header">
      <button class="back-button" @click="voltar">
        <ArrowLeft :size="18" />
        Voltar
      </button>

      <span class="eyebrow">SUA COLEÇÃO LITERÁRIA</span>
      <h1>Adicionar livro</h1>
      <p>Revise as informações e adicione o livro à sua estante.</p>
    </header>

    <form class="book-form" @submit.prevent="cadastrarLivro">
      <section class="cover-section">
        <div class="cover-preview">
          <img
            v-if="form.capaUrl"
            :src="form.capaUrl"
            alt="Capa do livro"
          />

          <div v-else class="no-cover">
            <BookOpen :size="42" />
            <span>Sem capa</span>
          </div>
        </div>

        <div class="cover-description">
          <span class="eyebrow">CAPA DO LIVRO</span>
          <h3>Prévia da edição escolhida</h3>
          <p>A imagem será utilizada na sua estante.</p>

          <label for="capaUrl">URL da capa</label>
          <input
            id="capaUrl"
            v-model="form.capaUrl"
            type="url"
            placeholder="https://..."
          />
        </div>
      </section>

      <div class="form-divider"></div>

      <section class="form-section">
        <div class="section-title">
          <BookOpen :size="19" />
          <h2>Informações bibliográficas</h2>
        </div>

        <div class="form-grid">
          <div class="field full">
            <label for="titulo">Título *</label>
            <input
              id="titulo"
              v-model="form.titulo"
              required
              maxlength="200"
              placeholder="Nome do livro"
            />
          </div>

          <div class="field">
            <label for="autor">Autor *</label>
            <input
              id="autor"
              v-model="form.autor"
              required
              maxlength="150"
              placeholder="Nome do autor"
            />
          </div>

          <div class="field">
            <label for="isbn">ISBN</label>
            <input
              id="isbn"
              v-model="form.isbn"
              placeholder="ISBN do livro"
            />
          </div>

          <div class="field">
            <label for="editora">Editora</label>
            <input
              id="editora"
              v-model="form.editora"
              maxlength="100"
              placeholder="Nome da editora"
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
              placeholder="Ex.: 2020"
            />
          </div>

          <div class="field">
            <label for="genero">Gênero</label>
            <input
              id="genero"
              v-model="form.genero"
              maxlength="100"
              placeholder="Ex.: Romance"
            />
          </div>

          <div class="field">
            <label for="paginas">Quantidade de páginas</label>
            <input
              id="paginas"
              v-model.number="form.quantidadePaginas"
              type="number"
              min="1"
              placeholder="Ex.: 350"
            />
          </div>

          <div class="field full">
            <label for="descricao">Descrição</label>
            <textarea
              id="descricao"
              v-model="form.descricao"
              rows="4"
              maxlength="2000"
              placeholder="Conte um pouco sobre o livro..."
            ></textarea>
          </div>
        </div>
      </section>

      <p v-if="erro" class="error-message">{{ erro }}</p>

      <div class="form-actions">
        <button
          type="button"
          class="cancel-button"
          @click="voltar"
        >
          Cancelar
        </button>

        <button
          type="submit"
          class="save-button"
          :disabled="salvando"
        >
          <Check :size="18" />
          {{ salvando ? 'Salvando...' : 'Adicionar à estante' }}
        </button>
      </div>
    </form>
  </main>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '../services/api'
import { useLivroPesquisaStore } from '../stores/livroPesquisa'

import {
  ArrowLeft,
  BookOpen,
  Check
} from '@lucide/vue'

const router = useRouter()
const livroPesquisaStore = useLivroPesquisaStore()

const salvando = ref(false)
const erro = ref('')

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

onMounted(() => {
  const livro = livroPesquisaStore.livroSelecionado

  if (!livro) {
    router.replace('/explorar')
    return
  }

  form.titulo = livro.titulo || ''
  form.autor = livro.autor || ''
  form.isbn = livro.isbn || ''
  form.editora = livro.editora || ''
  form.anoPublicacao = livro.anoPublicacao
  form.genero = livro.genero || ''
  form.quantidadePaginas = livro.quantidadePaginas
  form.descricao = livro.descricao || ''
  form.capaUrl = livro.capaUrl || ''
})

function voltar() {
  router.push('/explorar')
}

async function cadastrarLivro() {
  erro.value = ''

  if (!form.titulo.trim() || !form.autor.trim()) {
    erro.value = 'Título e autor são obrigatórios.'
    return
  }

  if (
    form.anoPublicacao !== null &&
    (!Number.isInteger(form.anoPublicacao) ||
      form.anoPublicacao < 1 ||
      form.anoPublicacao > 9999)
  ) {
    erro.value = 'Informe um ano de publicação válido.'
    return
  }

  const dataPublicacao = form.anoPublicacao
    ? `${form.anoPublicacao}-01-01`
    : null

  const dados = {
    titulo: form.titulo.trim(),
    autor: form.autor.trim(),
    isbn: form.isbn.trim() || null,
    editora: form.editora.trim() || null,
    dataPublicacao,
    genero: form.genero.trim() || null,
    quantidadePaginas: form.quantidadePaginas,
    descricao: form.descricao.trim() || null,
    capaUrl: form.capaUrl.trim() || null
  }

  salvando.value = true

  try {
    await api.post('/livros', dados)

    livroPesquisaStore.limparSelecao()

    router.push('/')
  } catch (error: any) {
    console.error('Erro ao cadastrar livro:', error)

    erro.value =
      error.response?.data?.erro ||
      error.response?.data?.mensagem ||
      'Não foi possível cadastrar o livro. Verifique os dados e tente novamente.'
  } finally {
    salvando.value = false
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  padding: 40px;
  background: var(--background);
}

.page-header {
  margin-bottom: 30px;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 8px;
  border: none;
  background: transparent;
  color: var(--pink-dark);
  margin-bottom: 30px;
  cursor: pointer;
}

.page-header h1 {
  font-family: 'Playfair Display', serif;
  font-size: 34px;
  color: var(--text);
  margin: 10px 0;
}

.page-header p {
  color: var(--text-light);
  font-size: 14px;
}

.book-form {
  max-width: 950px;
  background: white;
  border: 1px solid #f0e5eb;
  border-radius: 18px;
  padding: 30px;
}

.cover-section {
  display: flex;
  align-items: center;
  gap: 25px;
}

.cover-preview {
  width: 130px;
  height: 175px;
  flex-shrink: 0;
  background: var(--pink-light);
  border-radius: 9px;
  overflow: hidden;
}

.cover-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-cover {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: var(--pink-dark);
  font-size: 12px;
}

.cover-description {
  flex: 1;
}

.cover-description h3 {
  font-family: 'Playfair Display', serif;
  font-size: 20px;
  margin: 8px 0;
}

.cover-description p {
  font-size: 13px;
  color: var(--text-light);
  margin-bottom: 15px;
}

.cover-description label,
.field label {
  display: block;
  font-size: 12px;
  font-weight: 600;
  color: var(--text);
  margin-bottom: 8px;
}

.cover-description input,
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

.cover-description input:focus,
.field input:focus,
.field textarea:focus {
  border-color: var(--pink);
}

.form-divider {
  height: 1px;
  background: #f0e5eb;
  margin: 30px 0;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  color: var(--pink-dark);
  margin-bottom: 24px;
}

.section-title h2 {
  font-family: 'Playfair Display', serif;
  font-size: 21px;
  color: var(--text);
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.field.full {
  grid-column: 1 / -1;
}

.field textarea {
  resize: vertical;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 30px;
  padding-top: 25px;
  border-top: 1px solid #f0e5eb;
}

.cancel-button,
.save-button {
  padding: 12px 19px;
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

.save-button:disabled {
  opacity: 0.6;
  cursor: wait;
}

.error-message {
  margin-top: 20px;
  color: #c03958;
  font-size: 13px;
}

@media (max-width: 600px) {
  .register-page {
    padding: 22px 15px;
  }

  .book-form {
    padding: 20px;
  }

  .cover-section {
    align-items: flex-start;
    flex-direction: column;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .field.full {
    grid-column: auto;
  }

  .form-actions {
    flex-direction: column-reverse;
  }

  .form-actions button {
    justify-content: center;
  }
}
</style>
