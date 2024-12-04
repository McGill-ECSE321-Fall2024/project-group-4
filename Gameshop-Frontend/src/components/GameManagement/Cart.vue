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
          <button class="checkout-button" @click="toggleCheckoutForm">Proceed to checkout</button>
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
          <div>
            <p v-if="addresses.length === 0" class="text-secondary fst-italic" style="font-size:80%">No addresses available.</p>
            <BButton class="add-btn" @click="toggleAddressForm" size="sm"> Add a new address</BButton>
                
                <div class="mb-3 new_address" v-if="showAddressForm">
                    <br>
                    <BFormGroup id="address-label" label="Enter Address:" label-for="input-4">
                        <BFormInput id="street" type="text" v-model="newAddress.street" placeholder="Street" /><br />
                        <BFormInput id="city" type="text" v-model="newAddress.city" placeholder="City" /><br />
                        <BFormInput id="province" type="text" v-model="newAddress.province" placeholder="Province" /><br />
                        <BFormInput id="country" type="text" v-model="newAddress.country" placeholder="Country" /><br />
                        <BFormInput id="postalCode" type="text" v-model="newAddress.postalCode" placeholder="Postal Code" />
                    </BFormGroup>
                    <br>
                    <BButton class="save-info-btn" @click="saveAddress" size="sm"> Save new address</BButton>
                </div>
          </div>
        </BFormGroup>
        <br>
        <BFormGroup id="credit-label" label="Credit Cards:">
          <BFormSelect v-model="selectedCreditCard" class="mb-2">
            <BFormSelectOption :value="null" disabled>Select a credit card</BFormSelectOption>
            <BFormSelectOption v-for="(creditCard, index) in creditCards" :key="index" :value="creditCard">
              {{ formatCreditCard(creditCard) }}
            </BFormSelectOption>
          </BFormSelect>
          <div>
            <p v-if="creditCards.length === 0" class="text-secondary fst-italic" style="font-size:80%">No credit cards available.</p>
            <BButton @click="toggleAddCreditForm" class="add-btn" size="sm"> Add new credit card</BButton>
          </div>
        
          <div class="mb-3" size="sm" v-if="showAddCreditForm">
              <br>
              <BFormGroup id="credit-label" label="Enter Credit Card:">
                  <BFormInput id="cardNumber" type="text" v-model="newCreditCard.cardNumber" placeholder="Card Number" /><br />
                  <BFormInput id="expiryDate" type="date" v-model="newCreditCard.expiryDate" placeholder="Expiry Date" /><br />
                  <BFormInput id="cvv" type="text" v-model="newCreditCard.cvv" placeholder="CVV" /><br />
                  <BFormGroup id="billingAddress-label" label="Billing Address:">
                  <BFormSelect v-model="newCreditCard.billingAddress">
                      <BFormSelectOption value="" disabled>Select an address</BFormSelectOption>
                      <BFormSelectOption v-for="(address, index) in addresses" :key="index" :value="address">
                      {{ formatAddress(address) }}
                      </BFormSelectOption>
                      </BFormSelect>
                  </BFormGroup>
              </BFormGroup>
              <br>
              <BButton class="save-info-btn" @click="saveCredit" size="sm"> Save new credit card</BButton>
          </div>
    
        </BFormGroup><br>
        <BButton variant="primary" @click="finishedCheckout" :disabled="!selectedAddress || !selectedCreditCard">Submit Purchase</BButton>
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
const newCreditCard = ref({
  cardNumber: "",
  expiryDate: "",
  cvv: "",
  billingAddress: null,
});
const showAddCreditForm = ref(false);
const newAddress = ref({
  street: "",
  city: "",
  province: "",
  country: "",
  postalCode: "",
});
const showAddressForm = ref(false);

const addCreditCard = async () => {
}

const finishedCheckout = async () => {
  try{
    const response = await axiosClient.post(`customers/${localStorage.getItem('email')}/cart/${parseInt(selectedAddress.value.id)}/${parseInt(selectedCreditCard.value.id)}`);
    if (!response.ok) {
      throw new Error(`Error checking out: ${response.statusText}`);
    }
    showCheckoutForm.value = false;
    cart.value = [];
    router.push("/");
    
  } catch (err) {
    error.value = err.message;
  }
  // let response = await axiosClient.post(`customers/${localStorage.getItem('email')}/cart/${selectedAddress.value.id}/${selectedCreditCard.value.id}`);
  // cart.value = [];
  // showCheckoutForm.value = false;
  // router.push("/");

};

const toggleAddCreditForm = () => {
  showAddCreditForm.value = !showAddCreditForm.value;
};

const toggleAddressForm = () => {
  showAddressForm.value = !showAddressForm.value;
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
        // totalPrice.value = parseFloat(text);
        totalPrice.value= cart.value.reduce((total, game) => total + game.price * game.quantity, 0);
        console.log(text);
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
    const cardNumberStr = creditCard.cardNumber.toString();

        const maskedCardNumber = '*'.repeat(creditCard.cardNumber.toString().length - 4) + creditCard.cardNumber.toString().slice(-4);
        // return `Card Number: ${creditCard.cardNumber}, Expiry Date: ${creditCard.expiryDate}, CVV: ${creditCard.cvv}, Billing Address: ${this.formatAddress(creditCard.billingAddress)}`;
      return `Card Number: ${maskedCardNumber}, Expiry Date: ${creditCard.expiryDate}, CVV: ***, Billing Address: ${formatAddress(creditCard.billingAddress)}`;
};



onMounted(created);
</script>

<style scoped>
.cart {
  padding: 20px;
}

.save-info-btn{
  background-color: #3c67bf;
  border-color: #3c67bf;
  color: white;
}

.save-info-btn:hover {
  background-color: #795ac6;
  border-color: #795ac6;
  color: white;
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

.add-btn {
  background-color: #3428ba;
  border-color: #3428ba;
  color: white;
}

.add-btn:hover {
  background-color: #85abff;
  border-color: #85abff;
  color: white;
}
</style>
