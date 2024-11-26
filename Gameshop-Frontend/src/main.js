import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css'

import BootstrapVueNext from 'bootstrap-vue-next'

const view = createApp(App)
view.use(BootstrapVueNext)
view.use(router)

// 4 types: Guest, Customer, Employee, Manager
// Check if global variables are already initialized in localStorage
if (!localStorage.getItem('Customer') || !localStorage.getItem('Employee') || !localStorage.getItem('Manager')) {
    // Initialize global variables
    localStorage.setItem('accountType', 'Guest');
    localStorage.setItem('username', '');
    localStorage.setItem('loggedIn', false);
    localStorage.setItem('id', '0');
    localStorage.setItem('time', new Date().getDate() + "/" + new Date().getMonth() + "/" + new Date().getFullYear());
    localStorage.setItem('debugging_mode', true);
  }

  view.mount('#app')