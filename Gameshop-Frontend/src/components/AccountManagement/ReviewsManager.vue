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
        <template #cell(rating)="data">
          <div v-if="selectedReview && selectedReview.id === data.item.id">
            <BFormSelect v-model="selectedReview.rating" class="mb-2">
              <BFormSelectOption :value="1">1</BFormSelectOption>
              <BFormSelectOption :value="2">2</BFormSelectOption>
              <BFormSelectOption :value="3">3</BFormSelectOption>
              <BFormSelectOption :value="4">4</BFormSelectOption>
              <BFormSelectOption :value="5">5</BFormSelectOption>
            </BFormSelect>
          </div>
          <div v-else>
            {{ data.item.rating }}
          </div>
        </template>
        <template #cell(actions)="data">
          <div v-if="selectedReview && selectedReview.id === data.item.id" class="mt-3">
            <BButton variant="secondary" size="sm" @click="cancelEditReview" class="delete-btn">Cancel</BButton>
            <BButton variant="primary" size="sm" @click="updateReview" class="save-info-btn">Save</BButton>
          </div>
          <div v-else class="mt-3">
            <BButton size="sm" variant="primary" @click="editReview(data.item)" class="save-info-btn">Edit</BButton>
            <BButton size="sm" variant="danger" @click="deleteReview(data.item.id)" class="delete-btn">Delete</BButton>
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
          { key: 'review', label: 'Review' },
          { key: 'reply', label: 'Reply' },
          { key: 'rating', label: 'Rating' },
          { key: 'actions', label: 'Actions' },
        ],
        reviews: [
            {
                id: 1,
                review: 'Great game',
                reply: 'Thank you!',
                rating: 5,
            },
            {
                id: 2,
                review: 'Bad game',
                reply: 'We are sorry to hear that',
                rating: 1,
            },
        ],
        selectedReview: null,
      };
    },
    computed: {
        filteredReviews() {
            return this.reviews.filter(review1 => {
                const searchTerm = this.searchQuery.toLowerCase();
            
                return review1.id.toString().includes(searchTerm);
               
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
      async deleteReview(reviewId) {
        try {
          await axiosClient.delete(`/reviews/${reviewId}`);
          this.reviews = this.reviews.filter(review => review.id !== reviewId);
        } catch (error) {
          console.error('Error deleting review:', error);
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