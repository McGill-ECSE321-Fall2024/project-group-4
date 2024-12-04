<script>
import axios from 'axios';

const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
    baseURL: backendURL,
});
export default {
  name: "GamePreview",
  props: {
    game: {
      type: Object,
      required: true,
      validator: (value) =>
          'name' in value &&
          'price' in value &&
          'coverPicture' in value &&
          'stock' in value &&
          'promotions' in value &&
          'id' in value
    },
    enableAddToCart: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    discountedPrice() {
      const now = new Date();
      const activePromotions = this.game.promotions.filter(promo => {
        const startDate = new Date(promo.startDate);
        const endDate = new Date(promo.endDate);
        return now >= startDate && now <= endDate;
      });
      if (activePromotions.length > 0) {
        const discountSum = activePromotions.reduce((sum, promo) => sum + promo.discount, 0)
        const effectiveDiscount = Math.min(discountSum, 0);
        return (this.game.price * (1 - effectiveDiscount / 100)).toFixed(2);
      }
      return null;
    }
  },
  methods: {
    async handleAddToCart(e) {
      e.preventDefault();
      try {
        const response = await axiosClient.put(`/customers/${localStorage.getItem('accountId')}/cart/${this.game.id}`);
        await this.$router.push('/cart');
      }
      catch (error) {
        alert(error);
      }
      // const response = await fetch(
      //     `http://localhost:8080/customers/${localStorage.getItem('accountId')}/cart/${this.game.id}`,
      //     {
      //       method: "PUT",
      //     }
      // );
      // if (!response.ok) {
      //   console.log(response);
      // }
    }
  }
};
</script>

<template>
  <a :href="`/game?gameId=${game.id}`" class="text-decoration-none">
    <div class="game-preview">
      <BCard
          :title="game.name"
          :img-src="game.coverPicture"
          img-top
          tag="article"
          style="max-width: 20rem"
          class="wish-game"
      >
        <BCardText>
          <p v-if="discountedPrice" class="discounted-price">
            <del>Price: $ {{ game.price.toFixed(2) }}</del>
            <span>Now: $ {{ discountedPrice }}</span>
          </p>
          <p v-else>
            Price: $ {{ game.price.toFixed(2) }}
          </p>
        </BCardText>
        <BCardText>
          <p class="game-stock" :class="{ 'low-stock': game.stock <= 5 }">
            {{
              game.stock > 0
                  ? `${game.stock} copies remaining`
                  : 'Out of stock'
            }}
            <BButton
                v-if="enableAddToCart"
                class="add-to-cart"
                :disabled="game.stock <= 0"
                @click="handleAddToCart"
            >
              Add to Cart
            </BButton>
          </p>
        </BCardText>
      </BCard>
    </div>
  </a>
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

.like-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 5px;
}

.like-button:hover {
  background-color: #0056b3;
}
</style>