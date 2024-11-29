
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import './assets/main.css'
import * as BootstrapVueNext from 'bootstrap-vue-next';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css';

// import { BootstrapVueNext } from 'bootstrap-vue-next';

if (!localStorage.getItem('username')){
    localStorage.setItem('username', '')
    localStorage.setItem('loggedIn', 'false')
    localStorage.setItem('id', '0')
}

const app = createApp(App)

app.use(BootstrapVueNext);

app.use(router)

app.mount('#app')

