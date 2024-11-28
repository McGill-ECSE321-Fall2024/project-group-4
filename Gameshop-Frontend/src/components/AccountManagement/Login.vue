
<template>
    <div id="mainContainer">
        <div class="display-6 my-3" align="center">
    Login
  </div>
  <div align="center">
    New to Gameshop? 
    <a @click="goCreateAccount()" class="email-link">Sign Up</a>
  </div>
  <br>
  
  <BForm>
    <BFormGroup id="input-group-3" label="Account Type:" label-for="input-3">
        <BFormSelect
          id="inline-form-custom-select-pref"
          v-model="customSelect"
          required
          @change="logCustomSelect"
        >
        <BFormSelectOption :value="null" selected>Please select an option</BFormSelectOption>
        <BFormSelectOption value="Customer">Customer</BFormSelectOption>
        <BFormSelectOption value="Employee">Employee</BFormSelectOption>
        <BFormSelectOption value="Manager">Manager</BFormSelectOption>
        </BFormSelect>
    </BFormGroup>
    
    <br>
    <BFormGroup v-if="this.customSelect == 'Customer'" id="email-label" label="Email:" label-for="input-0">
        <BFormInput id="input-0" type="email" v-model="email" placeholder="Enter email" required />
        <br>
    </BFormGroup>

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
    <br>
    <BFormGroup id="password-label" label="Password:" label-for="input-2">
      <BFormInput id="input-2" v-model="password" placeholder="Enter password" required />
    </BFormGroup>

    <br>
    
    <BButton v-if="this.customSelect == 'Customer'" @click="loginCustomer()" variant="primary">Login</BButton>
    <BButton v-else-if="this.customSelect == 'Employee'" @click="loginEmployee()" variant="primary">Login</BButton>
    <BButton v-else-if="this.customSelect == 'Manager'" @click="loginManager()" variant="primary">Login</BButton>
    <BButton v-else disabled>Login</BButton>
  </BForm>

<br>
    </div>
</template>

<!-- <script setup>
import { ref } from 'vue';

const accountTypes = ['Customer', 'Employee', 'Manager'];

const show = ref(true);
</script> -->


<script>
import axios from 'axios';

const frontendURL = 'http://localhost:8087';
const backendURL = 'http://localhost:8080';

const AXIOS = axios.create({
    baseURL: backendURL,
    headers: {
        'Access-Control-Allow-Origin': frontendURL,
    }
});

export default{
    name: 'Login',
    data() {
        return {
            username: null,
            password: null,
            customSelect: null,
            email: null,
        }
    },
    methods:{
        logCustomSelect(){
            console.log(this.customSelect);
        },
        // login(){
        //     const params = new FormData();
        //     params.append('username', this.username);
        //     params.append('password', this.password);

        //     const basicAuth = 'Basic ' + btoa(this.username + ':' + this.password);

        //     fetch('http://localhost:8080/login', {
        //         method: 'POST',
        //         headers: {
        //             'Authorization': basicAuth,
        //         },
        //         body: params
        //     })
        // },
        async loginCustomer(){
            try{
                const response = await axios.post('accounts/login/customers' + this.email);
                console.log(response.data);

                if (response.status == 200){
                    this.setLoggedIn(true);
                    this.setUsername(this.username);

                    let id='0';
                    const responseId = await axios.get('accounts/customers/');
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

</script>  