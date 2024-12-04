
<template>
    <div class="mainContainer">
        <div class="display-6 my-3" align="center">
            Wishlist
        </div>
        <div v-if="wishlist.length > 0" class="wishlist-items">
          <div
            v-for="(game, index) in wishlist"
            :key="game.id"
            class="wishlist-item"
          >
            <GamePreview :game="game" />
            <div >
              <BButton @click="addGameToCart(index)" class="add-btn">Add to cart</BButton>
            </div>
            <div >
              <BButton @click="removeGameFromWishlist(index)" class="delete-btn">Remove</BButton>
            </div>
          </div>
        </div>
        <div v-else class="empty-wishlist">
          Your wishlist is empty.
        </div>

    </div>
</template>

<style scoped src="../../assets/main.css">
</style>

<script setup>

import {ref, onMounted} from "vue";
import GamePreview from "@/components/GameManagement/GamePreview.vue";

const wishlist = ref([]);
const loading = ref(true);
const error = ref(null);

const fetchWishlist = async () => {
  try{
    const response = await fetch(`http://localhost:8080/accounts/customers/${localStorage.getItem('accountId')}/wishlist`);
    if (!response.ok) {
      throw new Error(`Error fetching wishlist: ${response.statusText}`);
    }
    wishlist.value = await response.json();

  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
}

const removeGameFromWishlist = async (index) => {
  const response = await fetch("http://localhost:8080/accounts/customers/"+localStorage.getItem("accountId")+"/wishlist/"+wishlist.value[index].id, {method: "Delete",});
  if (!response.ok){
    console.error(`HTTP error! Status ${response.status}`)
  } else {
    wishlist.value.splice(index, 1);
  }
}

const addGameToCart = async (index) => {
  const response = await fetch(`http://localhost:8080/customers/${localStorage.getItem('accountId')}/cart/${wishlist.value[index].id}`, {method: "PUT",});
  if (!response.ok) {
    console.error(`HTTP error! Status: ${response.status}`);
  } else {
    console.log(response);
  }
}

onMounted(fetchWishlist)


</script>

<style scoped>
.wishlist {
  padding: 20px;
}

.wishlist h1 {
  font-size: 2em;
  margin-bottom: 20px;
}

.loading,
.error,
.empty-wishlist {
  font-size: 1.2em;
  color: #555;
  text-align: center;
}

.wishlist-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.wishlist-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.wishlist-summary {
  margin-top: 20px;
  text-align: right;
  font-size: 1.2em;
}
/* 
.add-to-cart-button {
  margin-top: 10px;
  padding: 10px 20px;
  font-size: 1em;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
} */

/* .remove-from-wishlist-button {
  margin-top: 10px;
  padding: 10px 20px;
  font-size: 1em;
  background-color: darkred;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
} */
</style>
