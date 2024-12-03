import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import GameCatalogue from '../components/GameManagement/GameCatalogue.vue'
import Login from '../components/AccountManagement/Login.vue'
import Signup from '../components/AccountManagement/Signup.vue'
import Account from '../components/AccountManagement/Account.vue'
import Wishlist from '../components/AccountManagement/Wishlist.vue'
import Cart from '../components/GameManagement/Cart.vue'
import ManageCenter from '@/components/AccountManagement/ManageCenter.vue'
import PurchaseHistory from "@/components/GameManagement/PurchaseHistory.vue";
import RefundRequest from "@/components/GameManagement/RefundRequest.vue";
import GameZone from '@/components/AccountManagement/GameZone.vue'
 
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
      component: GameCatalogue
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
    },
    {
      path: '/account',
      name: 'account',
      component: Account
    },
    {path: '/wishlist',
    name: 'wishlist',
    component: Wishlist
    },
    {
      path: '/cart',
      name: 'cart',
      component: Cart
    },
    {
      path: '/manage-center',
      name: 'manage-center',
      component: ManageCenter
    },
    {
      path: '/history',
      name: 'purchase-history',
      component: PurchaseHistory,
    },
    {
      path: '/refund-requests',
      name: 'refund-requests',
      component: RefundRequest,
    },
    {
      path: '/game-zone',
      name: 'game-zone',
      component: GameZone,
    }
    
  ],
})

export default router;
