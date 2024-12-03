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
            <BFormInput v-model="newGameRequest.game" placeholder="Game" class="mb-2" />
            <BButton variant="secondary" @click="cancelGameRequest" size="sm" class="delete-btn">Cancel</BButton>
            <BButton variant="primary" @click="" size="sm" class="save-info-btn">Save</BButton>
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
  name: "GameRequestEmployee",
  data() {
    return {
      searchQuery: '',
      showAddForm: false,
      gameRequests: [],
      fields: [
        { key: 'id', label: 'ID', sortable: true },
        { key: 'description', label: 'Description', sortable: true },
        { key: 'game', label: 'Game', sortable: true },
        { key: 'requestStatus', label: 'Request Status', sortable: true },
        { key: 'actions', label: 'Actions' },
      ],
      newGameRequest:{
        description: '',
        game: '',
        requestStatus: '',
        requestor: '',
      }
    };
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
  }
  };

        // String name,
        // String description,
        // String coverPicture,
        // float price,
        // boolean isActive,
        // int stock,
        // Set<CategoryResponseDTO> categories,
        // Set<PromotionResponseDTO> promotions

</script>

<style scoped src="../../assets/main.css">
</style>