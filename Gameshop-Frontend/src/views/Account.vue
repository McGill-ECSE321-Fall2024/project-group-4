

<template>
    <div class="mainContainer">
        <div class="display-6 my-3" align="center">
            Account Settings
        </div>

        <div class="shadow p-3 mb-5 bg-body rounded"> <!-- this is for customer -->
            <BForm > 
                <div class="mb-3">
                    <BFormGroup  id="email-label" label="Email:" label-for="input-0">
                        <p class="text-secondary fst-italic" style="font-size:80%">
                            * Note that you cannot change your email address after signing up.
                        </p>
                        <BFormInput id="input-0" type="text" readonly>{{ this.email }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="username-label" label="Username:" label-for="input-1">
                        <BFormInput id="input-1" type="text" v-model="username">{{ this.username }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="phoneNumber-label" label="Phone Number:" label-for="input-2">
                        <BFormInput id="input-2" type="text" v-model="phoneNumber">{{ this.phoneNumber }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="password-label" label="Password:" label-for="input-3">
                        <BFormInput id="input-3" type="text" v-model="password">{{ this.password }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup id="address-label" label="Addresses:" label-for="input-5">
                    <BListGroup v-if="addresses.length">
                        <BListGroupItem v-for="(address, index) in addresses" :key="index">
                        {{ address }}
                        <BButton class="delete-btn" @click="deleteAddress(index, address)" variant="danger" size="sm">Delete</BButton>
                        </BListGroupItem>
                    </BListGroup>
                    <p v-else class="text-secondary fst-italic" style="font-size:80%">
                    No addresses available.
                     </p>
                    </BFormGroup>
                </div>

                <BButton class="add-btn" @click="toggleAddressForm"> Add address</BButton>
                
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
                    <BButton class="add-btn" @click="saveAddress"> Save new address</BButton>
                </div>

                <!-- <div class="mb-3">
                    <BFormGroup id="credit-label" label="Credit Cards:" >
                    <BListGroup v-if="creditCards.length">
                        <BListGroupItem v-for="(creditCard, index) in creditCards" :key="index">
                        {{ creditCard }}
                        <BButton class="delete-btn" @click="deleteCard(index, creditCard)" variant="danger" size="sm">Delete</BButton>
                        </BListGroupItem>
                    </BListGroup>
                    <p v-else class="text-secondary fst-italic" style="font-size:80%">
                    No credit cards available.
                     </p>
                    </BFormGroup>
                </div> -->

                <!-- <BButton class="add-btn" @click="toggleCreditForm"> Add Credit Card</BButton> -->
                
                <!-- <div class="mb-3 new_creditCard" v-if="showAddressForm">
                    <br>
                    <BFormGroup id="address-label" label="Enter Credit Card:" label-for="input-4">
                        
                    </BFormGroup>
                    <br>
                    <BButton class="add-btn" @click="saveCredit"> Save new credit card</BButton>
                </div> -->
            </BForm>
        </div>

        <div class="shadow p-3 mb-5 bg-body rounded"> <!-- this is for employee -->
            <BForm > 
                <div class="mb-3">
                    <BFormGroup  id="email-label" label="Email:" label-for="input-0">
                        <p class="text-secondary fst-italic" style="font-size:80%">
                            * Note that you cannot change your email address after signing up.
                        </p>
                        <BFormInput id="input-0" type="text" readonly>{{ this.email }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="username-label" label="Username:" label-for="input-1">
                        <BFormInput id="input-1" type="text" >{{ this.username }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="phoneNumber-label" label="Phone Number:" label-for="input-2">
                        <BFormInput id="input-2" type="text">{{ this.phoneNumber }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="password-label" label="Password:" label-for="input-3">
                        <BFormInput id="input-3" type="text">{{ this.password }} </BFormInput>
                    </BFormGroup>  
                </div>  
            </BForm>
        </div>

    </div>
</template>

<style scoped src="../assets/main.css">
</style>

<script>
import axios from 'axios';

const backendURL = 'http://localhost:8080';

const AXIOS = axios.create({
  baseURL: backendURL,
});

export default {
  data() {
    return {
        username: '',
        phoneNumber: '',
        password: '',
        addresses: [],
        showAddressForm: false,
        newAddress: {
            street: '',
            city: '',
            province: '',
            country: '',
            postalCode: '',
        },
    };
  },
  methods: {
    toggleAddressForm() {
      this.showAddressForm = !this.showAddressForm;
    },
    deleteAddress(index) {
      this.addresses.splice(index, 1);
      // Optionally, you can also send a request to the backend to delete the address from the database
    },
    async saveAddress(){
        if (
            !this.newAddress.street ||
            !this.newAddress.city ||
            !this.newAddress.province ||
            !this.newAddress.country ||
            !this.newAddress.postalCode
        ) {
            alert('Please fill in all address fields.');
            return;
        }
        const address = this.newAddress.street + ', ' + this.newAddress.city + ', ' + this.newAddress.province + ', ' + this.newAddress.country + ', ' + this.newAddress.postalCode;
        this.addresses.push(address);

        try{
            const response = await AXIOS.post('/account/' + this.email + '/addresses', {
                address: address,
            });
            console.log(response.data);
        } catch(error) {
            console.error(error);
        }

        this.showAddressForm = false;
        this.newAddress = {
            street: '',
            city: '',
            province: '',
            country: '',
            postalCode: '',
        };
    }
  },
};
</script>