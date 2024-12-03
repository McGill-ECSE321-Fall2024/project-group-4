<template>
    <div class="mainContainer">
        <br>
        <div class="d-flex mb-3">
            
            <BFormInput
                v-model="searchQuery"
                placeholder="Search pending updates"
                class="me-2"
            />
            <BButton type="submit" class="search-btn">Search</BButton>
            <BButton variant="success" class="ms-auto save-info-btn" @click="showUpdates">+</BButton>
        </div>
  </div>
</template>


<script>
import axios from 'axios';
import { computed } from 'vue';

const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
    baseURL: backendURL,
});
export default {
  name: "PendingUpdates",
  };

</script>

<style scoped src="../../assets/main.css">
</style>