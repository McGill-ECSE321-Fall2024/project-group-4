<script setup>
  // import { BAvatar } from 'bootstrap-vue-next/dist/bootstrap-vue-next.umd';
import { RouterView } from 'vue-router';
</script>

<template>
  <div id="app">
    <BNavbar toggleable="lg" class="fixed-top custom-navbar">
      <BNavbarBrand href="/" class="BNavItem">Logo</BNavbarBrand>
      <BNavbarToggle target="nav-collapse" />
      <BCollapse id="nav-collapse" is-nav>
        <BNavbarNav>
          <BNavItem href="/games" class="BNavItem">Catalogue</BNavItem>
          <BNavItem href="/" class="BNavItem">Refund Requests</BNavItem>
          <BNavItem href="/" class="BNavItem">Reviews</BNavItem>

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
            <BDropdownItem href="#">Account</BDropdownItem>
            <BDropdownItem href="#">Purchase History</BDropdownItem>
            <BDropdownItem href="#">Wishlist</BDropdownItem>
            <BDropdownItem href="#">Logout</BDropdownItem>
          </BNavItemDropdown>
         
          <BNavItemDropdown text="Cart" right>
            
            <BDropdownItem href="#">Check-Out</BDropdownItem>
          </BNavItemDropdown>
        </BNavbarNav>
        
      </BCollapse>
    </BNavbar>
  
  <main class="container mt-5 pt-5">
      <RouterView />
  </main>

  </div>
</template>

<style scoped src="./assets/main.css">
</style>