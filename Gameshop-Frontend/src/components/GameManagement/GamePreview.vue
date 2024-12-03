<script>
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
    async handleAddToCart() {
      const response = await fetch(
          `http://localhost:8080/customers/${localStorage.getItem('accountId')}/cart/${this.game.id}`,
          {
            method: "PUT",
          }
      );
      if (!response.ok) {
        console.log(response);
      }
    }
  }
};
</script>

<template>
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
</template>

<style scoped>
.game-preview {
  display: flex;
  align-items: center;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-bottom: 10px;
  background-color: #f9f9f9;
}

.discounted-price del {
  color: #d9534f; /* Red for original price */
  margin-right: 5px;
}

.discounted-price span {
  color: #5cb85c; /* Green for discounted price */
  font-weight: bold;
}

.game-cover {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 5px;
  margin-right: 15px;
}

.game-details {
  display: flex;
  flex-direction: column;
}

.game-title {
  font-size: 1.2em;
  margin: 0;
  color: #333;
}

.game-price {
  font-size: 1.1em;
  color: #007bff;
  margin: 5px 0;
}

.game-stock {
  font-size: 0.9em;
  color: #555;
}

.game-stock.low-stock {
  color: #d9534f; /* Red for low stock */
}
</style>
