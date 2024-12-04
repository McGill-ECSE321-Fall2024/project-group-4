<template>
  <div id="app" :key="$route.fullPath">
    <div v-if="userRole === 'customer'"> <!-- if customer logged in-->
        <BNavbar toggleable="lg" class="fixed-top custom-navbar">
          <BNavbarBrand @click="goToHome()" class="navbar-brand">
            <router-link to="/">
              <img :src="logo" alt="Logo2" class="navbar-logo" />
            </router-link>
          </BNavbarBrand>
          <BNavbarToggle target="nav-collapse" />
          <BCollapse id="nav-collapse" is-nav >
            <BNavbarNav>
              <BNavItem @click="goCatalogue" class="BNavItem">Catalogue</BNavItem>
              <BNavItem @click="goReviews" class="BNavItem">Reviews</BNavItem>
              <BNavItem @click="goPolicy" class="BNavItem">Policy</BNavItem>
            </BNavbarNav>
            <!-- Right aligned nav items -->
            <BNavbarNav class="ms-auto mb-2 mb-lg-0">
              <BNavForm :onsubmit="search" class="d-flex">
                <BFormInput v-model="searchQuery" class="me-2" placeholder="Search Games" />
                <BFormSelect v-model="searchBy" class="me-2 w-auto">
                  <BFormSelectOption value="all">All</BFormSelectOption>
                  <BFormSelectOption value="name">Name</BFormSelectOption>
                  <BFormSelectOption value="genre">Genre</BFormSelectOption>
                  <BFormSelectOption value="id">ID</BFormSelectOption>
                </BFormSelect>
                <BButton type="submit" class="search-btn">Search</BButton>
              </BNavForm>
                <BNavItemDropdown text="Profile" right>
                  <template #button-content>
                      <BAvatar class="mb-1" />
                  </template>
                  <BDropdownItem @click="goAccount()">Account</BDropdownItem>
                  <BDropdownItem @click="goHistory()">Purchase History</BDropdownItem>
                  <BDropdownItem @click="goWishlist()">Wishlist</BDropdownItem>
                  <BDropdownItem @click="goLogout()">Logout</BDropdownItem>
                </BNavItemDropdown>
            
              <BNavItem @click="goCart()" text="Cart" right>View Cart</BNavItem>
            </BNavbarNav>
            
          </BCollapse>
        </BNavbar>
    </div>

      <div v-else-if="userRole === 'employee'"> <!-- if employee logged in-->
        <BNavbar toggleable="lg" class="fixed-top custom-navbar">
          <BNavbarBrand @click="goToHome()" class="navbar-brand">
            <router-link to="/">
              <img :src="logo" alt="Logo2" class="navbar-logo" />
            </router-link>
          </BNavbarBrand>
          <BNavbarToggle target="nav-collapse" />
          <BCollapse id="nav-collapse" is-nav >
            <BNavbarNav>
              <BNavItem @click="goCatalogue" class="BNavItem">Catalogue</BNavItem>
              <BNavItem @click="goRefundRequests" class="BNavItem">Refund Requests</BNavItem>
              <BNavItem @click="goGameZone" class="BNavItem">Game Zone</BNavItem>
              <BNavItem @click="goReviews" class="BNavItem">View History</BNavItem>
            </BNavbarNav>
            <!-- Right aligned nav items -->
            <BNavbarNav class="ms-auto mb-2 mb-lg-0">
              <BNavForm :onsubmit="search" class="d-flex">
                <BFormInput v-model="searchQuery" class="me-2" placeholder="Search Games" />
                <BFormSelect v-model="searchBy" class="me-2 w-auto">
                  <BFormSelectOption value="all">All</BFormSelectOption>
                  <BFormSelectOption value="name">Name</BFormSelectOption>
                  <BFormSelectOption value="genre">Genre</BFormSelectOption>
                  <BFormSelectOption value="id">ID</BFormSelectOption>
                </BFormSelect>
                <BButton type="submit" class="search-btn">Search</BButton>
              </BNavForm>
                <BNavItemDropdown text="Profile" right>
                  <template #button-content>
                      <BAvatar class="mb-1" />
                  </template>
                  <BDropdownItem @click="goAccount()">Account</BDropdownItem>
                  <BDropdownItem @click="goLogout()">Logout</BDropdownItem>
                </BNavItemDropdown>
            </BNavbarNav>
            
          </BCollapse>
        </BNavbar>
      </div>

      <div v-else-if="userRole === 'manager'"> <!-- if manager logged in-->
        <BNavbar toggleable="lg" class="fixed-top custom-navbar">
          <BNavbarBrand @click="goToHome()" class="navbar-brand">
            <router-link to="/">
              <img :src="logo" alt="Logo2" class="navbar-logo" />
            </router-link>
          </BNavbarBrand>
          <BNavbarToggle target="nav-collapse" />
          <BCollapse id="nav-collapse" is-nav >
            <BNavbarNav>
              <BNavItem @click="goCatalogue" class="BNavItem">Catalogue</BNavItem>
              <BNavItem @click="goManageCenter" class="BNavItem">Manage Center</BNavItem>
              <!-- <BNavItem @click="goUpdates" class="BNavItem">Pending Updates</BNavItem>
              <BNavItem @click="goReviewsManager" class="BNavItem">Reviews</BNavItem> -->
            </BNavbarNav>
            <!-- Right aligned nav items -->
            <BNavbarNav class="ms-auto mb-2 mb-lg-0">
              <BNavForm :onsubmit="search" class="d-flex">
                <BFormInput v-model="searchQuery" class="me-2" placeholder="Search Games" />
                <BFormSelect v-model="searchBy" class="me-2 w-auto">
                  <BFormSelectOption value="all">All</BFormSelectOption>
                  <BFormSelectOption value="name">Name</BFormSelectOption>
                  <BFormSelectOption value="genre">Genre</BFormSelectOption>
                  <BFormSelectOption value="id">ID</BFormSelectOption>
                </BFormSelect>
                <BButton type="submit" class="search-btn">Search</BButton>
              </BNavForm>
                <BNavItemDropdown text="Profile" right>
                  <template #button-content>
                      <BAvatar class="mb-1" />
                  </template>
                  <BDropdownItem @click="goAccount()">Account</BDropdownItem>
                  <BDropdownItem @click="goLogout()">Logout</BDropdownItem>
                </BNavItemDropdown>
            </BNavbarNav>
            
          </BCollapse>
        </BNavbar>
      </div>

      <div v-else>
        <BNavbar toggleable="lg" class="fixed-top custom-navbar">
          <BNavbarBrand @click="goToHome()" class="navbar-brand">
            <router-link to="/">
              <img :src="logo" alt="Logo2" class="navbar-logo" />
            </router-link>
          </BNavbarBrand>
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

<script setup>
import { RouterView, useRouter, useRoute } from 'vue-router';
import logo from './assets/logo.png';
import { ref, watch } from 'vue';

// Reactive state
const loggedIn = ref(localStorage.getItem('loggedIn') === 'true');
const searchQuery = ref('');
const searchBy = ref('all');
const username = ref(localStorage.getItem('username'));
const userRole = ref(localStorage.getItem('userRole'));

const router = useRouter();
const route = useRoute();

// Watch for route changes
watch(
    () => route.path,
    () => {
      // Trigger updates or reinitialize state
      loggedIn.value = localStorage.getItem('loggedIn') === 'true';
      username.value = localStorage.getItem('username');
      userRole.value = localStorage.getItem('userRole');
    }
);

// Navigation functions
function goGameZone() {
  router.push('/game-zone');
}

function goToHome() {
  router.push('/');
}
function goAccount() {
  router.push('/account');
}
function goCatalogue() {
  router.push('/games');
}
function goRefundRequests() {
  router.push('/refund-requests');
}
function goReviews() {
  router.push('/reviews');
}
function goLogin() {
  router.push('/login');
}
function goLogout() {
  localStorage.removeItem('loggedIn');
  localStorage.removeItem('username');
  localStorage.removeItem('userRole');
  loggedIn.value = false;
  username.value = '';
  userRole.value = '';
  router.push('/'); // Redirect to home
}
function goCart() {
  router.push('/cart');
}
function goWishlist() {
  router.push('/wishlist');
}
function goHistory() {
  router.push('/history');
}
function goCreateAccount() {
  router.push('/sign-up');
}
function goManageCenter() {
  router.push('/manage-center');
}
function goUpdates() {
  router.push('/pending-updates');
}
function goReviewsManager() {
  router.push('/manager-reviews');
}
function goPolicy() {
  router.push('/policies');
}
function search(e) {
  if (searchQuery.value.trim().length > 0) {
    if(searchBy.value === "id"){
      router.push(`/game?gameId=${searchQuery.value}`);
    } else if(searchBy.value === "name"){
      router.push(`/games?search=${searchQuery.value}`);
    } else if(searchBy.value === "genre"){
      router.push(`/games?category=${searchQuery.value}`);
    } else if(searchBy.value === "all"){
      router.push(`/games?search=${searchQuery.value}`);
    } else {
      router.push(`/games?search=${searchQuery.value}`);
    }
  }
}

</script>