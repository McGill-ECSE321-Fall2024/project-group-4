<script>
import { ref, computed, onMounted } from "vue";
import {useRoute, useRouter} from "vue-router";
import axios from 'axios';

const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
    baseURL: backendURL,
});

export default {
  name: "GamePage",
  setup() {
    const route = useRoute();
    const gameId = computed(() => route.query.gameId);
    const gameDetails = ref(null);
    const reviews = ref([]);
    const isLoading = ref(false);
    const isReviewsLoading = ref(false);
    const error = ref(null);
    const reviewsError = ref(null);
    const router = useRouter();

    // Fetch game details from API
    const fetchGameDetails = async () => {
      if (!gameId.value) {
        error.value = "Game ID is missing.";
        return;
      }
      isLoading.value = true;
      try {
        const response = await fetch(`http://localhost:8080/games/${gameId.value}`);
        if (!response.ok) {
          throw new Error(`Failed to fetch game details. Status: ${response.status}`);
        }
        gameDetails.value = await response.json();
      } catch (err) {
        error.value = err.message;
      } finally {
        isLoading.value = false;
      }
    };

    // Fetch reviews for the game
    const fetchReviews = async () => {
      if (!gameId.value) {
        reviewsError.value = "Game ID is missing.";
        return;
      }
      isReviewsLoading.value = true;
      try {
        const response = await fetch(`http://localhost:8080/games/${gameId.value}/reviews`);
        if (!response.ok) {
          throw new Error(`Failed to fetch reviews. Status: ${response.status}`);
        }
        reviews.value = await response.json();
      } catch (err) {
        reviewsError.value = err.message;
      } finally {
        isReviewsLoading.value = false;
      }
    };

    // Compute active discounts and final price
    const discountedPrice = computed(() => {
      if (!gameDetails.value) return 0;
      const activePromotions = gameDetails.value.promotions.filter((promo) => {
        const now = new Date();
        const startDate = new Date(promo.startDate);
        const endDate = new Date(promo.endDate);
        return now >= startDate && now <= endDate;
      });
      const discountSum = activePromotions.reduce((sum, promo) => sum + promo.discount, 0);
      const discountMultiplier = Math.max(0, 1 - discountSum / 100);
      return (gameDetails.value.price * discountMultiplier).toFixed(2);
    });

    const averageRating = computed(() => {
      if (!reviews.value || reviews.value.length === 0) return "N/A";
      const sum = reviews.value.reduce((total, review) => total + review.rating, 0);
      return (sum / reviews.value.length).toFixed(1);
    });

    // Extract category names
    const categoryNames = computed(() =>
        gameDetails.value?.categories.map((category) => category.name).join(", ") || "None"
    );

    // Handle like button interaction
    const handleLikeReview = async (reviewId) => {
      try {
        const response = await fetch(`http://localhost:8080/customers/${localStorage.getItem('email')}/reviews/${reviewId}/likes`, {
          method: "PUT",
        });
        if (!response.ok) {
          throw new Error(`Failed to like review. Status: ${response.status}`);
        }
        // Optionally, fetch updated reviews after liking
        await fetchReviews();
      } catch (err) {
        console.error(err.message);
      }
    };

    const handleAddToCart = async (e) => {
      e.preventDefault();
      try {
        const response = await axiosClient.put(`/customers/${localStorage.getItem('accountId')}/cart/${gameDetails.value.id}`);
        await router.push('/cart');
      }
      catch (error) {
        alert(error);
      }
      // const response = await fetch(
      //     `http://localhost:8080/customers/${localStorage.getItem('accountId')}/cart/${gameDetails.value.id}`,
      //     {
      //       method: "PUT",
      //     }
      // );
      // if (!response.ok) {
      //   console.log(response);
      // } else{
      //   await router.push('/cart');
      // }
    }

    // Fetch data on component mount
    onMounted(() => {
      fetchGameDetails();
      fetchReviews();
    });

    return {
      gameDetails,
      reviews,
      isLoading,
      isReviewsLoading,
      error,
      reviewsError,
      discountedPrice,
      averageRating,
      categoryNames,
      handleLikeReview,
      handleAddToCart
    };
  }
};
</script>

<template>
  <div class="game-page">
    <h1 v-if="isLoading">Loading game details...</h1>
    <div v-if="error" class="error">{{ error }}</div>
    <div v-if="gameDetails">
      <h1>{{ gameDetails.name }}</h1>
      <img :src="gameDetails.coverPicture" :alt="`${gameDetails.name} cover`" class="game-cover" />
      <div class="game-meta">
        <p><strong>Price:</strong> $ {{ discountedPrice }}</p>
        <p><strong>Average Rating:</strong> {{ averageRating }}</p>
        <p><strong>Stock:</strong>
          <span :class="{ 'low-stock': gameDetails.stock <= 5 }">
            {{ gameDetails.stock > 0
              ? `${gameDetails.stock} copies remaining`
              : 'Out of stock'
            }}
          </span>
        </p>
        <p><strong>Categories:</strong> {{ categoryNames }}</p>
        <p><strong>Description:</strong> {{ gameDetails.description }}</p>
        <BButton
            class="add-to-cart"
            :disabled="gameDetails.stock <= 0 || !gameDetails.isActive"
            @click="handleAddToCart"
        >
          Add to Cart
        </BButton>
      </div>
      <h2>Reviews</h2>
      <div v-if="isReviewsLoading">Loading reviews...</div>
      <div v-if="reviewsError" class="error">{{ reviewsError }}</div>
      <ul v-if="reviews.length > 0" class="reviews-list">
        <li v-for="(review, index) in reviews" :key="index">
          <p><strong>{{ review.username }}</strong> rated {{ review.rating }}/5</p>
          <p>{{ review.text }}</p>
          <p class="font-italic" v-if="review.likedBy.length > 0">{{review.likedBy.length}} like {{review.likedBy.length > 1 ? "s" : ""}}</p>
          <BButton v-if="localStorage && !review.likedBy.some((like)=>like.email === localStorage.getItem('email'))" @click="handleLikeReview(review.id)" class="like-button">Like</BButton>
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.game-page {
  margin: 20px;
}

.game-cover {
  max-width: 300px;
  height: auto;
  border-radius: 10px;
  margin-bottom: 20px;
}

.game-meta {
  margin-top: 10px;
  font-size: 1.1em;
}

.low-stock {
  color: #d9534f; /* Red for low stock */
}

.error {
  color: red;
  margin-top: 20px;
}

.reviews-list {
  list-style-type: none;
  padding: 0;
}

.reviews-list li {
  border-bottom: 1px solid #ddd;
  padding: 10px 0;
}

.reviews-list li:last-child {
  border-bottom: none;
}
</style>
