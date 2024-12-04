<template>
  <div class="cart">
    <div class="display-6 my-3" align="center">Your Cart</div>
    <div v-if="loading" class="loading">Loading your cart...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else>
      <div v-if="cart.length > 0" class="cart-items">
        <div
            v-for="(game, index) in cart"
            :key="game.id"
            class="cart-item"
        >
          <GamePreview :game="game" />
          <div class="quantity-controls">
            <button @click="decreaseQuantity(index)">-</button>
            <span>{{ game.quantity }}</span>
            <button :disabled="game.quantity >= game.stock" @click="increaseQuantity(index)">+</button>
          </div>
        </div>
        <div class="cart-summary">
          <p>Total: ${{ totalPrice.toFixed(2) }}</p>
          <button class="checkout-button" @click="toggleCheckoutForm">Checkout</button>
        </div>

        <div v-if="showCheckoutForm">
          <BForm>
            <BFormGroup id="address-label" label="Addresses:" label-for="input-5">
          <BFormSelect v-model="selectedAddress" class="mb-2">
            <BFormSelectOption :value="null" disabled>Select an address</BFormSelectOption>
            <BFormSelectOption v-for="(address, index) in addresses" :key="index" :value="address">
              {{ formatAddress(address) }}
            </BFormSelectOption>
          </BFormSelect>
        </BFormGroup>
        <br>
        <BFormGroup id="credit-label" label="Credit Cards:">
          <BFormSelect v-model="selectedCreditCard" class="mb-2">
            <BFormSelectOption :value="null" disabled>Select a credit card</BFormSelectOption>
            <BFormSelectOption v-for="(creditCard, index) in creditCards" :key="index" :value="creditCard">
              {{ formatCreditCard(creditCard) }}
            </BFormSelectOption>
          </BFormSelect>
        </BFormGroup><br>
            <BButton variant="primary" @click="finishedCheckout">Submit</BButton>
          </BForm>
        </div>
      </div>
      <div v-else class="empty-cart">
        Your cart is empty.
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import GamePreview from "./GamePreview.vue";

const axiosClient = axios.create({
  baseURL: "http://localhost:8080",
});

const cart = ref([]);
const loading = ref(true);
const error = ref(null);
const totalPrice = ref(0);
const selectedAddress = ref(null);
const selectedCreditCard = ref(null);
const addresses = ref([]);
const creditCards = ref([]);
const showCheckoutForm = ref(false);

const finishedCheckout = async () => {
  let response = await axiosClient.post(`customers/${localStorage.getItem('email')}/cart/${selectedAddress.value.id}/${selectedCreditCard.value.id}`);
  cart.value = [];
  showCheckoutForm.value = false;
  router.push("/");

};

const toggleCheckoutForm = () => {
  showCheckoutForm.value = !showCheckoutForm.value;
};

const created = async () => {
  await fetchCart();
  await fetchAddresses();
};

const fetchAddresses = async () => {
  try {
    const accountId = parseInt(localStorage.getItem('accountId'));
    if (!isNaN(accountId)) {
      
        const response = await axiosClient.get(`/accounts/customers/ids/${accountId}`);
                    const accountData = response.data;
                    // this.username = accountData.username;
                    // this.email = accountData.email;
                    // this.phoneNumber = accountData.phoneNumber;
                    addresses.value = accountData.addresses;
                    creditCards.value = accountData.creditCards;
      
    }
    
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
};

const fetchCart = async () => {
  try {
    const response = await fetch(`http://localhost:8080/customers/${localStorage.getItem('accountId')}/cart`);
    if (!response.ok) {
      throw new Error(`Error fetching cart: ${response.statusText}`);
    }
    cart.value = await response.json();
    await getTotalPrice();
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
};

const increaseQuantity = async (index) => {
  const response = await fetch("http://localhost:8080/customers/"+localStorage.getItem('accountId')+"/cart/"+cart.value[index].id, {
    method: "PUT",
  });

  if(!response.ok){
    console.error(`HTTP error! Status: ${response.status}`);
  } else {
    cart.value[index].quantity += 1;
    await getTotalPrice();
  }
};

const decreaseQuantity = async (index) => {
  const response = await fetch("http://localhost:8080/customers/"+localStorage.getItem('accountId')+"/cart/"+cart.value[index].id, {
    method: "DELETE",
  });
  if(!response.ok){
    console.error(`HTTP error! Status: ${response.status}`);
  } else{
    if (cart.value[index].quantity > 1) {
      cart.value[index].quantity -= 1;

    } else {
      cart.value.splice(index, 1); // Remove the item from the cart if quantity is 0
    }
    await getTotalPrice();
  }

};

const getTotalPrice = async () => {
      const response = await fetch(`http://localhost:8080/customers/${localStorage.getItem('email')}/cart/price`);
      if (response.ok) {
        // Extract the response body as text
        const text = await response.text();

        // Parse the text as a float
        totalPrice.value = parseFloat(text);
      } else{
        console.error(`HTTP error! Status: ${response.status}`);
      }
    };

const checkout = async () => {
  const response = await fetch(`http://localhost:8080/customers/${localStorage.getItem('email')}/cart?billingAddressId=1&creditCardId=1`, {
    method: "POST",
  })
  console.log(response)
};

const formatAddress = (address) => {
      return `${address.street}, ${address.city}, ${address.province}, ${address.country}, ${address.postalCode}`;
};

const formatCreditCard = (creditCard) => {
      return `Card Number: ${creditCard.cardNumber}, Expiry Date: ${creditCard.expiryDate}, CVV: ${creditCard.cvv}, Billing Address: ${formatAddress(creditCard.billingAddress)}`;
};


onMounted(created);
</script>

<style scoped>
.cart {
  padding: 20px;
}

.cart h1 {
  font-size: 2em;
  margin-bottom: 20px;
}

.loading,
.error,
.empty-cart {
  font-size: 1.2em;
  color: #555;
  text-align: center;
}

.cart-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.cart-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.quantity-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.quantity-controls button {
  padding: 5px 10px;
  font-size: 1em;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.quantity-controls button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.quantity-controls span {
  font-size: 1.2em;
  font-weight: bold;
}

.cart-summary {
  margin-top: 20px;
  text-align: right;
  font-size: 1.2em;
}

.checkout-button {
  margin-top: 10px;
  padding: 10px 20px;
  font-size: 1em;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>
