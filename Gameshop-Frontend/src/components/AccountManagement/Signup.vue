
<template>
    <div id="mainContainer">
        <div class="display-6 my-3" align="center">
            Sign Up
        </div>
  <div align="center">
    Already have an account?
    <a @click="goLogin()" class="email-link">Login</a>
  </div>
  <br>
  
  <BForm>
   
    <BFormGroup  id="email-label" label="Email:" label-for="input-0">
        <BFormInput id="input-0" type="text" v-model="email" placeholder="Enter email" required />
    </BFormGroup>

    <br>
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
    <BFormGroup id="phonenumber-label" label="Phone Number:" label-for="input-4">
        <BFormInput id="input-4" v-model="phoneNumber" type="text" placeholder="Enter phone number" required/>
    </BFormGroup>
    <br>
    <BFormGroup id="password-label" label="Password:" label-for="input-2">
      <BFormInput id="input-2" v-model="password" type="text" placeholder="Enter password" required />
    </BFormGroup>

    <br>
    
    <BButton @click="signUpCustomer()" class="save-info-btn">Sign Up</BButton>
    
  </BForm>

    <br>
    </div>
</template>

<style scoped src="../../assets/main.css">
</style>

<script>
import axios from 'axios';
const frontendURL = 'http://localhost:8087';
const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
	// NOTE: it's baseURL, not baseUrl
	baseURL: backendURL,
//     headers: {
//     'Access-Control-Allow-Origin': frontendURL,
//   },
});

export default{
    name: 'Signup',
    data() {
        return {
            email: null,
            password: null,
            username: null,
            phoneNumber: null,
        }
    },
    methods:{
        async signUpCustomer(){
            let response = '';
            const credentials = {
                    username: this.username,
                    email: this.email,
                    password: this.password,
                    phoneNumber: this.phoneNumber,                   
                };
            
            try{
                console.log(credentials)
                response = await axiosClient.post("/accounts/customers", credentials);
                console.log(response.data) ;

                console.log(response.status);
                if (response.status === 200){
                    this.setLoggedIn(true);
                    this.setUsername(this.username);
                    this.setAccountId(response.data.id);
                    this.clearInputs();
                    this.$router.push('/');
                }

            } catch(error){
                alert(error.message)
            }
       },

        //other methods
      
        goLogin(){
            this.$router.push('/login');
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