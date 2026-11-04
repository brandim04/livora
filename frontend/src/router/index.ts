
import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '../views/HomeView.vue'
import ExplorarView from '../views/ExplorarView.vue'
import CadastroLivroView from '../views/CadastroLivroView.vue'
import EstanteView from '../views/EstanteView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/explorar',
    name: 'explorar',
    component: ExplorarView
  },
  {
    path: '/livros/cadastrar',
    name: 'cadastro-livro',
    component: CadastroLivroView
  },
  {
    path: '/estante',
    name: 'estante',
    component: EstanteView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
