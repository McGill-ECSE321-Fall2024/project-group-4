import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import GamesViewAll from '../views/GamesViewAll.vue'
import Login from '../components/AccountManagement/Login.vue'
import Signup from '../components/AccountManagement/Signup.vue'
 

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/games',
      name: 'games',
      component: GamesViewAll
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/sign-up',
      name: 'signup',
      component: Signup
    }
    
  ],
})

export default router;