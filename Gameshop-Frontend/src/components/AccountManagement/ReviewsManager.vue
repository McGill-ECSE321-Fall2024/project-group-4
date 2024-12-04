<template>
    <div class="reviewsManager">
      <br>
      <div class="d-flex mb-3">
        <BFormInput
          v-model="searchQuery"
          placeholder="Search Game Reviews by ID"
          class="me-2"
        />
        <BButton type="submit" class="search-btn" @click="searchReview">Search</BButton>
        <br>
      </div>
      <BTable :items="filteredReviews" :fields="fields">
        <template #cell(reply)="data">
          <div v-if="selectedReview && selectedReview.id === data.item.id">
            <BFormInput v-model="selectedReview.reply" class="mb-2" />
          </div>
          <div v-else>
            {{ data.item.reply }}
          </div>
        </template>
        
        <template #cell(actions)="data">
          <div v-if="selectedReview && selectedReview.id === data.item.id" class="mt-3">
            <BButton variant="secondary" size="sm" @click="cancelEditReview" class="delete-btn">Cancel</BButton>
            <BButton variant="primary" size="sm" @click="updateReview" class="save-info-btn">Send</BButton>
          </div>
          <div v-else class="mt-3">
            <BButton size="sm" variant="primary" @click="editReview(data.item)" class="save-info-btn">Reply</BButton>
          </div>
        </template>
      </BTable>
    </div>
  </template>
  
<script>
    import axios from 'axios';
    
    const frontendURL = 'http://localhost:8087';
    const backendURL = 'http://localhost:8080';

    const axiosClient = axios.create({
        baseURL: backendURL,
        // headers: {
        //     'Access-Control-Allow-Origin': frontendURL,
        // }
    });
    
  export default {
    name: "ReviewsManager",
    data() {
      return {
        searchQuery: '',
        fields: [
          { key: 'id', label: 'ID' },
          { key: 'review', label: 'Game' },
          { key: 'rating', label: 'Rating' },
          { key: 'reply', label: 'Reply' },
          { key: 'actions', label: 'Actions' },
        ],
        reviews: [
            
        ],
        selectedReview: null,
      };
    },
    computed: {
        filteredReviews() {
            return this.reviews.filter(review => {
                const searchTerm = this.searchQuery.toLowerCase();
                return review.id.toString() === searchTerm;
            });        
        },
    },
    methods: {
      async fetchReviews() {
        try {
          const response = await axiosClient.get('/reviews');
          this.reviews = response.data;
        } catch (error) {
          console.error('Error fetching reviews:', error);
        }
      },
      async searchReview() {
        if (this.searchQuery) {
            try {
                const response = await axiosClient.get(`/reviews/${this.searchQuery}`);
                this.reviews = [response.data];
            } catch (error) {
                console.error('Error fetching reviews by ID:', error);
            }
        }
        else {
            // Handle search by ID or other criteria
            this.filteredReviews();
        }
      },
      editReview(review) {
        this.selectedReview = { ...review };
      },
      cancelEditReview() {
        this.selectedReview = null;
      },
      async updateReview() {
        try {
          const response = await axiosClient.put(`/reviews/${this.selectedReview.id}/reply`, this.selectedReview);
        //   /reviews/{reviewId}/reply
          if (response.status === 200) {
            const index = this.reviews.findIndex(review => review.id === this.selectedReview.id);
            if (index !== -1) {
              this.reviews.splice(index, 1, response.data);
            }
            this.selectedReview = null;
          }
        } catch (error) {
          console.error('Error updating review:', error);
        }
      },
    },
    mounted() {
      this.fetchReviews();
    },
  };
  </script>
  
  <style scoped src="../../assets/main.css">
  </style>