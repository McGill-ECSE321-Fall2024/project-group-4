<template>
  <div id="app">
    <b-navbar toggleable="lg" type="dark" variant="info">
      <b-navbar-brand href="#">NavBar</b-navbar-brand>
      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item href="#">Link</b-nav-item>
          <b-nav-item-dropdown text="Dropdown" right>
            <b-dropdown-item href="#">Action</b-dropdown-item>
            <b-dropdown-item href="#">Another action</b-dropdown-item>
            <b-dropdown-item href="#">Something else here...</b-dropdown-item>
            <b-dropdown-divider></b-dropdown-divider>
            <b-dropdown-item href="#">Separated link</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script>

export default {
  name: 'app',
  data() {
    return {
      username: localStorage.getItem('username') || '',
      loggedIn: localStorage.getItem('loggedIn') === 'true',
      time: localStorage.getItem('time') || (new Date()).getDate() + "/" + ((new Date()).getMonth() + 1) + "/" + (new Date()).getFullYear(),
    };
  }
  
}
</script>

<style scoped src="./assets/main.css"></style>