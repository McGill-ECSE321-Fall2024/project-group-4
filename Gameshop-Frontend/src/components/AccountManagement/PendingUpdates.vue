<template>
    <div class="mainContainer">
        <br>
        <!-- <div class="d-flex mb-3">
            <BFormSelect v-model="searchBy" class="me-2 w-auto">
                <BFormSelectOption value="all">All</BFormSelectOption>
                <BFormSelectOption value="name">Name</BFormSelectOption>
                <BFormSelectOption value="genre">Genre</BFormSelectOption>
                <BFormSelectOption value="id">ID</BFormSelectOption>
            </BFormSelect>
            <BFormInput
                v-model="searchQuery"
                placeholder="Search games"
                class="me-2"
            />
            <BButton type="submit" class="search-btn">Search</BButton>
        </div> -->

        <BTable :items="games" :fields="fields">
            <template #cell(stock)="data">
                <div v-if="selectedGame && selectedGame.id === data.item.id">
                <BFormInput v-model="selectedGame.stock" class="mb-2" />
                </div>
                <div v-else>
                {{ data.item.stock }}
                </div>
            </template>

            <template #cell(active)="data">
                <div v-if="selectedGame && selectedGame.id === data.item.id">
                <BFormSelect v-model="selectedGame.active" class="mb-2">
                    <BFormSelectOption :value="true">Active</BFormSelectOption>
                    <BFormSelectOption :value="false">Inactive</BFormSelectOption>
                </BFormSelect>
                </div>
                <div v-else>
                    {{ data.item.active ? 'Active' : 'Inactive' }}
                </div>
            </template>

            <template #cell(actions)="data">
                <div v-if="selectedGame && selectedGame.id === data.item.id" class="mt-3">
                    <BButton variant="secondary" size="sm" @click="cancelEditGame" class="delete-btn">Cancel</BButton>
                    <BButton variant="primary" size="sm" @click="saveEditGame" class="save-info-btn">Save</BButton>                  
                </div>
                <div v-else  class="mt-3">
                    <BButton size="sm" variant="primary" @click="editGame(data.item)" class="save-info-btn">Edit</BButton>
                </div>
            </template>
        </BTable>
  </div>
</template>


<script>
import axios from 'axios';
import { computed } from 'vue';
import GameBrowser from '../GameManagement/GameBrowser.vue';
import GameCatalogue from '../GameManagement/GameCatalogue.vue';

const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
    baseURL: backendURL,
});
export default {
    name: "UpdateGame",
    components: {
        GameCatalogue,
    },
    data(){
        return{
            games: [
                {
                    id: 1,
                    name: 'Game 1',
                    genre: 'Action',
                    stock: 10,
                    active: true,
                },
                {
                    id: 2,
                    name: 'Game 2',
                    genre: 'Adventure',
                    stock: 5,
                    active: false,
                },
                {
                    id: 3,
                    name: 'Game 3',
                    genre: 'RPG',
                    stock: 15,
                    active: true,
                },
            ],
            searchBy: 'all',
            searchQuery: '',
            fields: [
                { key: 'id', label: 'ID' },
                { key: 'name', label: 'Name' },
                { key: 'genre', label: 'Genre' },
                { key: 'stock', label: 'Stock' },
                { key: 'active', label: 'Active Status' },
                { key: 'actions', label: '' },
            ],
            selectedGame: {
                id: null,
                name: '',
                genre: '',
                stock: null,
                active: null,
            },

        }
    },
    computed: {
       
    },
    methods:{
        cancelEditGame() {
            this.selectedGame = null;
        },
        editGame(game){
            this.selectedGame = { ...game };
        },

    }
    
  };

</script>

<style scoped src="../../assets/main.css">
</style>

<!-- update game stock and active status-->