<template>
    <div class="mainContainer">
        <br>
        <div class="d-flex mb-3">
            
            <BFormInput
                v-model="categoryName"
                placeholder="Enter new category name :"
                class="me-2"
            />
            <BButton variant="success" class="ms-auto save-info-btn" @click="addCategory(categoryName)">+

            </BButton>
        </div>
        <BTable :items="categories" :fields="fields2">
            <template #cell(actions)="row">
                <div class="d-flex align-items-center justify-content-between">
                    <BButton variant="danger" @click="deleteCategory(row.item.name)">
                        -
                    </BButton>
                    <b-Dropdown text="Games in category" variant="primary" class="ml-2">
                        <b-dropdown-item
                         v-for="(game) in getGamesForCategory(row.item.name)"
                        >
                            {{ game }}   
                        </b-dropdown-item>
                    </b-Dropdown>

                    <b-dropdown text="Add/remove Games from category" variant="primary" class="ml-2">
                    <template v-for="(game) in games">
                        <b-dropdown-item>
                        <div class="d-flex justify-content-between align-items-center">
                            <span>{{ game.name }}</span>
                            <b-button
                            size="sm"
                            @click="addGameToCategory(row.item.name, game.id)"
                            variant="primary"
                            class="ml-2"
                            >
                            +
                            </b-button>

                            <b-button
                            size="sm"
                            @click="removeGameFromCategory(row.item.name, game.id)"
                            variant="danger"
                            class="ml-2"
                            >
                            -
                            </b-button>
                        </div>
                        </b-dropdown-item>
                    </template>
</b-dropdown>


                </div>
                <title>
                    AAAAAAA
                </title>
            </template>  

        </BTable>
  </div>
  </template>
<script>
import axios from 'axios';

const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
    baseURL: backendURL,
});
export default {
  name: "GameCategories",
  data() {
    return {
        categoryName: '',
        fields2: [
        { key: 'id', label: 'ID', sortable: true },
        { key: 'name', label: 'Category name', sortable: true },
        { key: 'actions', label: 'Actions' },
      ],
      categories : [],
      games : []
    }
  }, methods: {
    async addCategory(categoryName) {
        let response = '';
        try {
        const response = await axiosClient.post(`/categories/${categoryName}`);
        if (response.status === 200) {
            await this.fetchCategories();
        }
    } catch (error) {
        alert(error.response?.data?.errorMessages || error.message || "Something went wrong");
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
        alert(error.response?.data?.errorMessages || error.message || "Something went wrong");
    }   
    },
    async fetchGames() {
        let response = '';
        try {
            const response = await axiosClient.get('/games');
         if (response.status === 200) {
            this.games = response.data;
            console.log(this.games);
        } 
    }  catch (error) {
        alert(error.response?.data?.errorMessages || error.message || "Something went wrong");
    }  
    
    }, async deleteCategory(name) {
    let response = '';
        try {
            const response = await axiosClient.delete('/categories/' + name);
         if (response.status === 200) {
            await this.fetchCategories();
        } 
    }  catch (error) {
        alert(error.message);
    }  
  }, getGamesForCategory(name) {
    let returnArr = [];

returnArr = this.games.filter(
  game =>
    game.categories &&
    game.categories.some(category => category.name && category.name.includes(name))
);

return returnArr.map(game => game.name);
  }, async addGameToCategory (categoryName, gameId) {
    let response = '';
    try {
            const response = await axiosClient.put(`/categories/${categoryName}/games/${gameId}`);
         if (response.status === 200) {
            await this.fetchCategories();
        }  
    }  catch (error) {
        alert(error.response?.data?.errorMessages || error.message || "Something went wrong");
    }  

  }, async removeGameFromCategory (categoryName, gameId) {
    let response = '';
    try {
            const response = await axiosClient.delete(`/categories/${categoryName}/games/${gameId}`);
         if (response.status === 200) {
            await this.fetchCategories();
        }  else {
            alert(response.errorMessages);
        }
    }  catch (error) {
        alert(error.response?.data?.errorMessages || error.message || "Something went wrong");
    }  

  }
  },
  async mounted() {
    await this.fetchCategories();
    await this.fetchGames();
  }
  };


</script>

<style scoped src="../../assets/main.css">
</style>