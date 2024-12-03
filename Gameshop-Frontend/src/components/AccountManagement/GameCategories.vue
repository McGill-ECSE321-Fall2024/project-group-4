<template>
    <div class="mainContainer">
        <br>
        <div class="d-flex mb-3">
            
            <BFormInput
                v-model="categoryName"
                placeholder="Enter category name :"
                class="me-2"
            />
            <BButton variant="success" class="ms-auto save-info-btn" @click="addCategory(categoryName)">+</BButton>
        </div>
        <BTable :items="categories" :fields="fields2">

        </BTable>
  </div>
  </template>
<script>
import axios from 'axios';
import { computed } from 'vue';

const frontendURL = 'http://localhost:8087';
const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
    baseURL: backendURL,
});
export default {
  name: "GameCategories",
  data() {
    return {
        fields2: [
        { key: 'id', label: 'ID', sortable: true },
        { key: 'name', label: 'Category name', sortable: true },
      ],
      categories : []
    }
  }, methods: {
    async addCategory(categoryName) {
        let response = '';
        try {
        const response = await axiosClient.post(`/categories/` + categoryName);
        if (response.status === 200) {
            await this.fetchCategories();
        }
    } catch (error) {
        alert(error.message);
    }   
    }, async fetchCategories() {
        let response = '';
        try {
        const response = await axiosClient.get('/categories');
        if (response.status === 200) {
            this.categories = response.data;
            console.log(this.categories);
        }
    } catch (error) {
        alert(error.message);
    }   
    }
  },
  async mounted() {
    this.categories = await this.fetchCategories();
  }
  };


</script>

<style scoped src="../../assets/main.css">
</style>