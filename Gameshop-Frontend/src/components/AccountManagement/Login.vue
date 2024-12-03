<style scoped src="../../assets/main.css">
</style>

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
        <BFormSelectOption :value="null" selected disabled>Please select an option</BFormSelectOption>
        <BFormSelectOption value="Customer">Customer</BFormSelectOption>
        <BFormSelectOption value="Employee">Employee</BFormSelectOption>
        <BFormSelectOption value="Manager">Manager</BFormSelectOption>
        </BFormSelect>
    </BFormGroup>
    
    <br>
    <BFormGroup v-if="this.customSelect == 'Customer'" id="email-label" label="Email:" label-for="input-0">
        <BFormInput id="input-0" type="email" v-model="email" placeholder="Enter email" required />
        
    </BFormGroup>

    <BFormGroup v-if="this.customSelect == 'Employee' || this.customSelect == 'Manager'"
      id="username-label"
      label="Username:"
      label-for="input-1"
    >
            <BFormInput
        id="input-1"
        type="text"
        v-model="username"
        placeholder="Enter username"
        required
        />
    </BFormGroup>
    <br>
    <BFormGroup id="password-label" label="Password:" label-for="input-2">
      <BFormInput id="input-2" v-model="password" placeholder="Enter password" required />
    </BFormGroup>

    <br>
    
    <BButton v-if="this.customSelect == 'Customer'" @click="loginCustomer()" class="save-info-btn">Login</BButton>
    <BButton v-else-if="this.customSelect == 'Employee'" @click="loginEmployee()" class="save-info-btn">Login</BButton>
    <BButton v-else-if="this.customSelect == 'Manager'" @click="loginManager()" class="save-info-btn">Login</BButton>
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

const axiosClient = axios.create({
    baseURL: backendURL,
    headers: {
        'Access-Control-Allow-Origin': frontendURL,
    }
});

// console.log('Login page');
//     if (localStorage.getItem('loggedIn')){
    
//     this.$router.push('/');
//     console.log('Already logged in');

//     }

export default{
    name: 'Login',
    data() {
        return {
            // username: null,
            password: null,
            customSelect: null,
            email: null,
            userRole: localStorage.getItem('userRole'),
        }
    },
   
    methods:{

        logCustomSelect(){
            console.log(this.customSelect);
        },
        async loginCustomer(){
            try{
                const passwordEntered = this.password;
                const response = await axiosClient.post(`accounts/login/customers/${this.email}`,passwordEntered, {
                headers: {
                    'Content-Type': 'text/plain',  // Set Content-Type to text/plain
                },});
                console.log(response.data)

                if (response.status === 200){
                    this.setLoggedIn(true);
                    this.setUsername(this.username);
                    this.setAccountId(response.data.id);
                    this.setEmail(response.data.email);
                    localStorage.setItem('userRole', 'customer');
                    this.userRole = 'customer';
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
                const response = await axiosClient.post(`/login/employees/${this.username}`, {password : this.password});

                if (response.status === 200){
                    this.setLoggedIn(true);
                    this.setUsername(response.data.username);
                    this.setAccountId(response.data.id);
                    this.$root.setEmail(response.data.email);
                    this.$root.setUserRole('customer');

                    localStorage.setItem('userRole', 'employee');
                    this.userRole = 'employee';
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
                const passwordEntered = this.password;
                console.log(this.user)
                const response = await axiosClient.post(`accounts/login/manager/${this.username}`,passwordEntered, {
                headers: {
                    'Content-Type': 'text/plain',  // Set Content-Type to text/plain
                },});
                if (response.status == 200){
                  this.setLoggedIn(true);
                  this.setUsername(response.data.username);
                  this.setAccountId(response.data.id);
                  localStorage.setItem('userRole', 'manager');
                    this.userRole = 'manager';
                    this.clearInputs();
                    // this.$router.push('/');
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
        setLoggedIn(value){
            this.loggedIn = value;
            localStorage.setItem('loggedIn', value);
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
        setEmail(email){
          localStorage.setItem('email', email);
        },
        getEmail(){
          return localStorage.getItem('email');
        },

    }
}

</script>  