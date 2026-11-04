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

      <section class="welcome">
        <div>
          <span class="eyebrow">SEU CANTINHO LITERÁRIO</span>
          <h2>Bem-vinda ao seu universo de leitura.</h2>
          <p>
            Organize suas histórias, acompanhe suas leituras
            e descubra novos mundos.
          </p>
        </div>

        <div class="welcome-decoration">
          <BookOpen :size="64" />
        </div>
      </section>

      <section class="stats">
        <div class="stat-card">
          <span>Livros na estante</span>
          <strong>{{ livros.length }}</strong>
          <Library :size="22" />
        </div>

        <div class="stat-card">
          <span>Leituras concluídas</span>
          <strong>0</strong>
          <BookCheck :size="22" />
        </div>

        <div class="stat-card">
          <span>Lendo atualmente</span>
          <strong>0</strong>
          <BookOpen :size="22" />
        </div>

        <div class="stat-card">
          <span>Lista de desejos</span>
          <strong>0</strong>
          <Heart :size="22" />
        </div>
      </section>

      <section class="recent-section">
        <div class="section-heading">
          <div>
            <span class="eyebrow">SUA COLEÇÃO</span>
            <h3>Minha estante</h3>
          </div>

          <RouterLink to="/explorar">
            Explorar livros
          </RouterLink>
        </div>

        <div v-if="carregando" class="empty-state">
          Carregando seus livros...
        </div>

        <div v-else-if="erro" class="empty-state">
          <h4>Não foi possível carregar sua estante.</h4>
          <p>{{ erro }}</p>

          <button class="add-button" @click="buscarLivros">
            Tentar novamente
          </button>
        </div>

        <div v-else-if="livros.length === 0" class="empty-state">
          <div class="empty-icon">
            <BookHeart :size="36" />
          </div>

          <h4>Sua estante está esperando por você!</h4>
          <p>
            Adicione seu primeiro livro e comece a organizar suas histórias.
          </p>

          <RouterLink to="/explorar" class="add-button">
            <Plus :size="18" />
            Explorar livros
          </RouterLink>
        </div>

        <div v-else class="books-grid">
          <article
            v-for="livro in livros"
            :key="livro.id"
            class="book-card"
          >
            <img
              v-if="livro.capaUrl"
              :src="livro.capaUrl"
              :alt="`Capa do livro ${livro.titulo}`"
              class="book-cover"
              @error="livro.capaUrl = null"
            />

            <div v-else class="book-cover no-cover">
              <BookOpen :size="35" />
              <span>Sem capa</span>
            </div>

            <div class="book-info">
              <h4>{{ livro.titulo }}</h4>
              <p>{{ livro.autor }}</p>
              <span>{{ livro.genero || 'Sem categoria' }}</span>
            </div>
          </article>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import api from '../services/api'

import {
  BookOpen,
  House,
  Library,
  Search,
  Heart,
  UserRound,
  BookCheck,
  BookHeart,
  Plus
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

const livros = ref<Livro[]>([])
const carregando = ref(false)
const erro = ref('')

async function buscarLivros() {
  carregando.value = true
  erro.value = ''

  try {
    const resposta = await api.get<Livro[]>('/livros')
    livros.value = resposta.data

    console.log('Livros recebidos do backend:', resposta.data)
  } catch (error) {
    console.error('Erro ao buscar livros:', error)
    erro.value = 'Verifique se o backend está funcionando.'
  } finally {
    carregando.value = false
  }
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

.welcome {
  background: linear-gradient(110deg, #f8e5ef, #fceff5);
  border-radius: 20px;
  padding: 42px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30px;
}

.welcome h2 {
  font-family: 'Playfair Display', serif;
  font-size: clamp(25px, 3vw, 36px);
  color: #633d52;
  max-width: 550px;
  margin: 12px 0;
}

.welcome p {
  color: #866b7b;
  font-size: 14px;
  line-height: 1.7;
  max-width: 520px;
}

.welcome-decoration {
  color: #d78caf;
  padding: 25px;
}

.stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 45px;
}

.stat-card {
  position: relative;
  background: white;
  border: 1px solid #f0e5eb;
  border-radius: 14px;
  padding: 22px;
  color: var(--text-light);
  font-size: 12px;
}

.stat-card strong {
  display: block;
  color: var(--text);
  font-size: 28px;
  margin-top: 12px;
}

.stat-card svg {
  position: absolute;
  right: 18px;
  top: 20px;
  color: var(--pink);
}

.section-heading {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.section-heading h3 {
  font-family: 'Playfair Display', serif;
  font-size: 25px;
  margin-top: 7px;
  color: var(--text);
}

.section-heading a {
  color: var(--pink-dark);
  font-size: 13px;
}

.empty-state {
  background: white;
  border: 1px dashed #e8c9d9;
  border-radius: 16px;
  padding: 45px 20px;
  text-align: center;
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

.empty-state h4 {
  font-family: 'Playfair Display', serif;
  font-size: 20px;
  margin-bottom: 9px;
  color: var(--text);
}

.empty-state p {
  color: var(--text-light);
  font-size: 13px;
  margin-bottom: 20px;
}

.add-button {
  background: var(--pink-dark);
  color: white;
  border: none;
  border-radius: 9px;
  padding: 12px 18px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 24px;
}

.book-card {
  background: white;
  border: 1px solid #f0e5eb;
  border-radius: 14px;
  padding: 12px;
  overflow: hidden;
  transition: transform 0.2s;
}

.book-card:hover {
  transform: translateY(-4px);
}

.book-cover {
  width: 100%;
  height: 210px;
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
  padding: 12px 3px 4px;
}

.book-info h4 {
  font-family: 'Playfair Display', serif;
  font-size: 15px;
  margin-bottom: 5px;
  color: var(--text);
}

.book-info p,
.book-info span {
  display: block;
  color: var(--text-light);
  font-size: 12px;
  margin-top: 5px;
}

@media (max-width: 1100px) {
  .stats {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 900px) {
  .sidebar {
    width: 210px;
  }

  .main-content {
    padding: 25px;
  }

  .welcome-decoration {
    display: none;
  }
}

@media (max-width: 600px) {
  .sidebar {
    display: none;
  }

  .main-content {
    padding: 20px;
  }

  .welcome {
    padding: 25px;
  }

  .stats {
    grid-template-columns: 1fr 1fr;
  }
}
</style>