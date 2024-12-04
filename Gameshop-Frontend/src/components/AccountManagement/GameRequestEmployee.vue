<template>
    <div class="mainContainer">
        <br>
        <div class="d-flex mb-3">
            <BFormInput
                v-model="searchQuery"
                placeholder="Search game requests"
                class="me-2"
            />
            <BButton type="submit" class="search-btn">Search</BButton>
            <BButton variant="success" class="ms-auto save-info-btn" @click="showGameRequests">+</BButton>
        </div>
        <div v-if="showAddForm" class="mb-3">
            <br>
            <BFormTextarea v-model="newGameRequest.description" placeholder="Description" class="mb-2" />
            <BFormInput v-model="newGameRequest.title" placeholder="Title" class="mb-2" />
            <BFormInput v-model="newGameRequest.price" placeholder="Price" class="mb-2" />
            <BFormInput v-model="newGameRequest.review" placeholder="Review" class="mb-2" />
            <BButton variant="secondary" @click="cancelGameRequest" size="sm" class="delete-btn">Cancel</BButton>
            <BButton variant="primary" @click="saveGameRequest" size="sm" class="save-info-btn">Save</BButton>
        </div>

        <BTable :items="gameRequests" :fields="fields">
          <template #cell(game)="data">
            <div>
              <strong>Game: {{ data.item.game.name }}</strong><br>
              <small>Game ID: {{ data.item.game.id }}</small><br>
              <small>Description: {{ data.item.game.description }}</small><br>
              <small>Active: {{ data.item.game.isActive }}</small><br>
              <small>Price: ${{ data.item.game.price }}</small><br>
              <small>Stock: {{ data.item.game.stock }}</small><br>
              <small>Categories: {{ data.item.game.categories }}</small><br>
              <small>Promotions: {{ data.item.game.promotions }}</small><br>
              <small>External Review: {{ data.item.externalReview }}</small><br>
            </div>
          </template>
        </BTable>
       
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
  name: "GameRequestEmployee",
  data() {
    return {
      searchQuery: '',
      showAddForm: false,
      gameRequests: [],
      fields: [
        { key: 'id', label: 'ID', sortable: true },
        { key: 'game', label: 'Game', sortable: true },
        { key: 'requestStatus', label: 'Request Status', sortable: true },
      ],
      newGameRequest:{
        description: '',
        title: '',
        price: '',
        review: '',
        isActive: false,
        categories: [],
        promotions: [],
        stock: 0,
      },
      
    };
  },
  async created() {
    try {
      const response = await axiosClient.get(`/gameRequests`);
      this.gameRequests = response.data;
    } catch (error) {
      alert(`Error fetching data: ${error}`);
    }
  },
  methods:{
    showGameRequests() {
      this.showAddForm = !this.showAddForm;
    },
    cancelGameRequest() {
      this.showAddForm = false;
      this.newGameRequest = {
        externalReview: '',
        gameId: '',
        requestorId: '',
      };
    },
    async saveGameRequest() {
      try {
        let requestor = await axiosClient.get(`/accounts/employees/username/${localStorage.getItem('username')}`);
        let game = {
            id: null,
            name: this.newGameRequest.title,
            description: this.newGameRequest.description,
            coverPicture: '', // FILL IN LATER
            price: this.newGameRequest.price,
            isActive: false,
            stock: 0,
            categories: [], // FILL IN LATER
            promotions: []
        };
        let gameResponse = await axiosClient.post("/games", game);
        const request = {
          id: null,
          externalReview: this.newGameRequest.review,
          requestStatus: 2,
          requestor: requestor.data,
          game: gameResponse.data
        }
        let response = await axiosClient.post('/gameRequests', request).then(this.$router.go());
      } catch (error) {
        alert(error);
      }
    }
  }
  };

</script>

<style scoped src="../../assets/main.css">
</style>