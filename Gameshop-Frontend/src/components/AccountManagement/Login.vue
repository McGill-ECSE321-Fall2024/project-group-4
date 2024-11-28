<template>
    JHELLO
</template>


<!-- 

<template>
    <div id="mainContainer">
        <div class="display-6 my-3" align="center">
    Login
  </div>
  <div align="center">
    New to Gameshop? 
    <a href="/sign-up" class="email-link">Sign Up</a>
  </div>
  <br>
  
  <BForm @submit="onSubmit" @reset="onReset" v-if="show">
    <BFormGroup
      id="username-label"
      label="Username:"
      label-for="input-1"
    >
      <BFormInput
        id="input-1"
        v-model="username"
        type="text"
        placeholder="Enter username"
        required
      />
    </BFormGroup>

    <BFormGroup id="password-label" label="Password:" label-for="input-2">
      <BFormInput id="input-2" v-model="password" placeholder="Enter password" required />
    </BFormGroup>

    <BFormGroup id="input-group-3" label="Account Type:" label-for="input-3">
      <BFormSelect id="input-3" v-model="accountType" :options="accountTypes" required />
    </BFormGroup>
    
    <BButton type="submit" variant="primary">Login</BButton>
  </BForm>


    </div>
</template>

<script setup>

import { ref } from 'vue';
import axios from 'axios';
import router from '../../router';

const accountTypes = [{ text: 'Select One', value: null }, 'Customer', 'Employee', 'Manager'];

const frontendURL = window.location.origin;
const backendURL = `http://${process.env.VUE_APP_BACKEND_HOST}:${process.env.VUE_APP_BACKEND_PORT}`;

const AXIOS = axios.create({
  baseURL: backendURL,
  headers: {
    'Access-Control-Allow-Origin': frontendURL,
  },
});

const show = ref(true);
</script>


<script>

export default{
    name: 'Login',
    data() {
        return {
            username: null,
            password: null,
        }
    },
    methods:{
        async loginCustomer(){
            try{
                const response = await AXIOS.get('/login/customers' + this.username);
                console.log(response.data);

                if (response.status == 200){
                    this.setLoggedIn(true);
                    this.setUsername(this.username);

                    let id='0';
                    const responseId = await AXIOS.get('/customers');
                    console.log(responseId.data);

                    for (const account of responseId.data){
                        if (account.username == this.username && account.password == this.password){
                            id = account.id;
                            break;
                        }
                    }

                    this.setAccountId(id);

                    this.clearInputs();
                    this.$router.push('/');
                } else{
                    console.log('Login unsuccessful');
                }

            } catch (error){
                alert(error.message);
            }
        },

        async loginEmployee() {
            try{
                const response = await AXIOS.get('/login/employees' + this.username);
                console.log(response.data);

                if (response.status == 200){
                    this.setLoggedIn(true);
                    this.setUsername(this.username);

                    let id='0';
                    const responseId = await AXIOS.get('/employees');
                    console.log(responseId.data);

                    for (const account of responseId.data){
                        if (account.username == this.username && account.password == this.password){
                            id = account.id;
                            break;
                        }
                    }

                    this.setAccountId(id);

                    this.clearInputs();
                    this.$router.push('/');
                } else{
                    console.log('Login unsuccessful');
                }

            } catch (error){
                alert(error.message);
            }
        },
        async loginManager(){
            try{
                const response = await AXIOS.get('/login/managers' + this.username);
                console.log(response.data);

                if (response.status == 200){
                    this.setLoggedIn(true);
                    this.setUsername(this.username);

                    let id='0';
                    const responseId = await AXIOS.get('/managers');
                    console.log(responseId.data);

                    for (const account of responseId.data){
                        if (account.username == this.username && account.password == this.password){
                            id = account.id;
                            break;
                        }
                    }

                    this.setAccountId(id);

                    this.clearInputs();
                    this.$router.push('/');
                } else{
                    console.log('Login unsuccessful');
                }

            } catch (error){
                alert(error.message);
            }
        },

        //other methods
        goForgotPassword(){
            this.$router.push('/forgot-password');
        },
        goCreateAccount(){
            this.$router.push('/sign-up');
        },
        clearInputs(){
            this.username = null;
            this.password = null;
        },
        getLoggedIn(){
            return localStorage.getItem('loggedIn');
        },
        setLoggedIn(loggedIn){
            localStorage.setItem('loggedIn', loggedIn);
        },
        getUsername(){
            return localStorage.getItem('username');
        },
        setUsername(username){
            localStorage.setItem('username', username);
        },
        setAccountId(accountId){
            localStorage.setItem('accountId', accountId);
        },
        getAccountId(){
            return localStorage.getItem('accountId');
        },

    }
}

</script> --> 