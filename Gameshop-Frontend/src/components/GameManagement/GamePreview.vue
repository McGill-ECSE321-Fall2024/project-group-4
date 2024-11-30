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
          'id' in value
    },
    enableAddToCart: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    async handleAddToCart(){
      const response = await fetch("http://localhost:8080/customers/"+localStorage.getItem('accountId')+"/cart/"+this.game.id, {
        method: "PUT",
      })
      if(!response.ok){
        console.log(response)
      }
    }
  }
};
</script>

<template>
  <div class="game-preview">
    <img :src="game.coverPicture" :alt="`${game.name} cover`" class="game-cover" />
    <div class="game-details">
      <h2 class="game-title">{{ game.name }}</h2>
      <p class="game-price">$ {{ game.price.toFixed(2) }}</p>
      <p class="game-stock" :class="{ 'low-stock': game.stock <= 5 }">
        {{ game.stock > 0
          ? `${game.stock} copies remaining`
          : 'Out of stock'
        }}
        <button v-if="enableAddToCart"
            class="add-to-cart"
            :disabled="game.stock <= 0"
            @click="handleAddToCart"
        >
          Add to Cart
        </button>
      </p>
    </div>
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