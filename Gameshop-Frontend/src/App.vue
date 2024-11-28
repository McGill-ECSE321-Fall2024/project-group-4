<script setup>
  // import { BAvatar } from 'bootstrap-vue-next/dist/bootstrap-vue-next.umd';
import { RouterView } from 'vue-router';
</script>

<template>
  <div id="app">
    <div v-if="loggedIn">
        <BNavbar toggleable="lg" class="fixed-top custom-navbar">
          <BNavbarBrand @click="goToHome()" class="BNavItem">Logo</BNavbarBrand>
          <BNavbarToggle target="nav-collapse" />
          <BCollapse id="nav-collapse" is-nav >
            <BNavbarNav>
              <BNavItem @click="goCatalogue" class="BNavItem">Catalogue</BNavItem>
              <BNavItem @click="goRefundRequests" class="BNavItem">Refund Requests</BNavItem>
              <BNavItem @click="goReviews" class="BNavItem">Reviews</BNavItem>
            </BNavbarNav>
            <!-- Right aligned nav items -->
            <BNavbarNav class="ms-auto mb-2 mb-lg-0">
              <BNavForm class="d-flex">
                <BFormInput class="me-2" placeholder="Search" />
                <BButton type="submit" class="search-btn">Search</BButton>
              </BNavForm>
                <BNavItemDropdown text="Profile" right>
                  <template #button-content>
                      <BAvatar class="mb-1" />
                  </template>
                  <BDropdownItem @click="goCreateAccount()">Account</BDropdownItem>
                  <BDropdownItem @click="goHistory()">Purchase History</BDropdownItem>
                  <BDropdownItem @click="goWishlist()">Wishlist</BDropdownItem>
                  <BDropdownItem @click="goLogout()">Logout</BDropdownItem>
                </BNavItemDropdown>
            
              <BNavItemDropdown text="Cart" right>
                <BDropdownItem @click="goCart()">Check-Out</BDropdownItem>
              </BNavItemDropdown>
            </BNavbarNav>
            
          </BCollapse>
        </BNavbar>
      </div>

      <div v-else>
        <BNavbar toggleable="lg" class="fixed-top custom-navbar">
          <BNavbarBrand @click="goToHome()" class="BNavItem">Logo</BNavbarBrand>
          <BNavbarToggle target="nav-collapse" />
            <BCollapse id="nav-collapse" is-nav >
            <BNavbarNav>
              <BNavItem @click="goCatalogue" class="BNavItem">Catalogue</BNavItem>
             
            </BNavbarNav>
            <!-- Right aligned nav items -->
            <BNavbarNav class="ms-auto mb-2 mb-lg-0">
              <BNavForm class="d-flex">
                <BFormInput class="me-2" placeholder="Search" />
                <BButton type="submit" class="search-btn">Search</BButton>
              </BNavForm>
              <BNavItem @click="goLogin()" class="BNavItem">Login</BNavItem>
              <BNavItem @click="goCreateAccount()" class="BNavItem">Sign Up</BNavItem>
            
            </BNavbarNav>
            
          </BCollapse>

        </BNavbar>
        
      </div>
    
  
  <main class="container mt-5 pt-5">
      <RouterView />
  </main>

  </div>
</template>

<style scoped src="./assets/main.css">
</style>

<script>
export default {
  name: 'App',
  // data() {
  //   return {
  //     loggedIn: localStorage.getItem('loggedIn') === 'true',
  //   };
  // },
  methods: {
    //global variables
    getLoggedIn() {
      return localStorage.getItem('loggedIn');
    },
    setLoggedIn(loggedIn) {
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

    //navigation
    goToHome() {
      this.$router.push('/');
    },
    goAccount(){
      this.$router.push('/account');
    },
    goCatalogue(){
      this.$router.push('/games');
    },
    goRefundRequests(){
      this.$router.push('/refund-requests');
    },
    goReviews(){
      this.$router.push('/reviews');
    },
    goLogin()
    {
      this.$router.push('/login');
    },
    goLogout(){
      this.setLoggedIn(false);
      this.setUsername('');
      this.setAccountId('');
      this.$router.push('/logout');
    },
    goCart(){
      this.$router.push('/cart');
    },
    goWishlist(){
      this.$router.push('/wishlist');
    },
    goHistory(){
      this.$router.push('/history');
    },
    goCheckOut(){
      this.$router.push('/checkout');
    },
    goCreateAccount(){
      this.$router.push('/sign-up');
    },
    goViewEmployees(){
      this.$router.push('/view-employees');
    },
    goManageEmployees(){
      this.$router.push('/manage-employees');
    },
    
  },
  watch: {
  //look for changes in global variables
    loggedIn: {
      handler() {
        localStorage.setItem('loggedIn', this.loggedIn);
      },
      immediate: true
    },
    username: {
      handler() {
        localStorage.setItem('username', this.username);
      },
      immediate: true
    }
  }
}
</script>