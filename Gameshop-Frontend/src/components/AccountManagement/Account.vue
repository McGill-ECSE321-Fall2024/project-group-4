
<template>
    <div class="mainContainer">
        <div class="display-6 my-3" align="center">
            Account Settings
        </div>

        <div class="shadow p-3 mb-5 bg-body rounded" > <!-- this is for customer -->
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
                        <BFormInput id="input-1" type="text" v-model="username" @input="handleInputChange('username')" >{{ this.username }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="phoneNumber-label" label="Phone Number:" label-for="input-2">
                        <BFormInput id="input-2" type="text" v-model="phoneNumber" @input="handleInputChange('phoneNumber')" >{{ this.phoneNumber }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="password-label" label="Password:" label-for="input-3">
                        <BFormInput id="input-3" type="text" v-model="password" @input="handleInputChange('password')" >{{ this.password }} </BFormInput>
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
                    <BButton class="save-info-btn" @click="saveAddress"> Save new address</BButton>
                </div>
                
                <div class="mb-3">
                    <br>
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
                </div>

                <BButton class="add-btn" @click="toggleCreditForm"> Add Credit Card</BButton>
                
                <div class="mb-3 new_creditCard" v-if="showCreditForm">
                    <br>
                    <BFormGroup id="credit-label" label="Enter Credit Card:">
                        <BFormInput id="cardNumber" type="text" v-model="newCreditCard.cardNumber" placeholder="Card Number" /><br />
                        <BFormInput id="expiryDate" type="date" v-model="newCreditCard.expiryDate" placeholder="Expiry Date" /><br />
                        <BFormInput id="cvv" type="text" v-model="newCreditCard.cvv" placeholder="CVV" /><br />
                        <BFormGroup id="billingAddress-label" label="Billing Address:">
                            <BFormSelect v-model="useExistingAddress">
                                <BFormSelectOption :value="true">Use Existing Address</BFormSelectOption>
                                <BFormSelectOption :value="false">Enter New Address</BFormSelectOption>
                            </BFormSelect>
                            <br>
                            <div v-if="useExistingAddress">
                                <BFormSelect v-model="newCreditCard.billingAddress">
                                    <BFormSelectOption value="" disabled>Select an address</BFormSelectOption>
                                    <BFormSelectOption v-for="(address, index) in addresses" :key="index" :value="address">
                                    {{ address }}
                                    </BFormSelectOption>
                                </BFormSelect>
                            </div>
                            <div v-else>
                                <BFormInput id="street" type="text" v-model="billingAddress.street" placeholder="Street" /><br />
                                <BFormInput id="city" type="text" v-model="billingAddress.city" placeholder="City" /><br />
                                <BFormInput id="province" type="text" v-model="billingAddress.province" placeholder="Province" /><br />
                                <BFormInput id="country" type="text" v-model="billingAddress.country" placeholder="Country" /><br />
                                <BFormInput id="postalCode" type="text" v-model="billingAddress.postalCode" placeholder="Postal Code" />
                            </div>
                        </BFormGroup>
                    </BFormGroup>
                    <br>
                    <BButton class="save-info-btn" @click="saveCredit"> Save new credit card</BButton>
                </div>
                <br><br>
                <BButton v-if="showSaveInfoButton" class="save-info-btn" @click="saveInfo">Save Info</BButton>

            </BForm>
        </div>

        <div class="shadow p-3 mb-5 bg-body rounded" > <!-- this is for employee -->
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
                        <BFormInput id="input-1" type="text" v-model="username" @input="handleInputChange('username')">{{ this.username }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="password-label" label="Password:" label-for="input-3">
                        <BFormInput id="input-3" type="text" readonly>{{ this.password }} </BFormInput>
                    </BFormGroup>  
                </div> 

                <BButton v-if="showSaveInfoButton" class="save-info-btn" @click="saveInfoEmployee">Save Info</BButton>
            </BForm>
        </div>

        <div class="shadow p-3 mb-5 bg-body rounded"> <!-- this is for manager -->
            <BForm > 
                <div class="mb-3">
                    <BFormGroup  id="username-label" label="Username:" label-for="input-1">
                        <BFormInput id="input-1" type="text" readonly>{{ this.username }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="password-label" label="Password:" label-for="input-3">
                        <BFormInput id="input-3" type="text" readonly>{{ this.password }} </BFormInput>
                    </BFormGroup>  
                </div> 

            </BForm>
        </div>

    </div>
</template>

<style scoped src="../../assets/main.css">
</style>

<script>
import axios from 'axios';

const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
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
        showCreditForm: false,
        useExistingAddress: true,
        showSaveInfoButton: false,
        creditCards: [],
        newCreditCard: {
            cardNumber: '',
            expiryDate: '',
            cvv: '',
            billingAddress: '',
        },
        newAddress: {
            street: '',
            city: '',
            province: '',
            country: '',
            postalCode: '',
        },
        billingAddress: {
            street: '',
            city: '',
            province: '',
            country: '',
            postalCode: '',
        },
        changedField: '',
    };
  },
  methods: {
    handleInputChange(field) {
        this.changedField = field;
        this.showSaveInfoButton = true;
    },
    async saveInfo() {
        try {
        let response;
        if (this.changedField === 'username') {
          response = await axiosClient.put(`/accounts/customers/${this.email}/username/${this.username}`);
        } else if (this.changedField === 'phoneNumber') {
          response = await axiosClient.put(`/accounts/customers/${this.email}/phoneNumber/${this.phoneNumber}`);
        } else if (this.changedField === 'password') {
          response = await axiosClient.put(`/accounts/customers/${this.email}/password`, {
            password: this.password,
          });
        }
        console.log(response.data);
        this.showSaveInfoButton = false;
      } catch (error) {
        console.error('Error saving info:', error);
      }
    },
    async saveInfoEmployee(){
        try {
            const response = await axiosClient.put(`/accounts/employees/${this.username}/username/${username}`);
            console.log(response.data);
        } catch (error) {
            console.error('Error saving info:', error);
        }
    },
    toggleAddressForm() {
      this.showAddressForm = !this.showAddressForm;
    },
    toggleCreditForm() {
      this.showCreditForm = !this.showCreditForm;
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
            const response = await axiosClient.post(`/accounts/customers/${this.email}/addresses`, {
                street : this.newAddress.street,
                city : this.newAddress.city,
                province : this.newAddress.province,
                country : this.newAddress.country,
                postalCode : this.newAddress.postalCode
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
    },
    async saveCredit(){
        if (this.useExistingAddress){
            if (!this.newCreditCard.cardNumber || !this.newCreditCard.expiryDate || !this.newCreditCard.cvv || !this.newCreditCard.billingAddress){
                alert('Please fill in all credit card fields.');
                return;
            }
        } else{
            if (
                !this.billingAddress.street ||
                !this.billingAddress.city ||
                !this.billingAddress.province ||
                !this.billingAddress.country ||
                !this.billingAddress.postalCode
            ) {
                alert('Please fill in all address fields.');
                return;
            }
            this.newCreditCard.billingAddress = this.billingAddress.street + ', ' + this.billingAddress.city + ', ' + this.billingAddress.province + ', ' + this.billingAddress.country + ', ' + this.billingAddress.postalCode;
        }
        this.saveCreditCard();
    },
    async saveCreditCard(){
        if (
            !this.newCreditCard.cardNumber ||
            !this.newCreditCard.expiryDate ||
            !this.newCreditCard.cvv ||
            !this.newCreditCard.billingAddress
        ){
            alert('Please fill in all credit card fields.');
            return;
        }

        const creditCard = this.newCreditCard.cardNumber + ', ' + this.newCreditCard.expiryDate + ', ' + this.newCreditCard.cvv + ', ' + this.newCreditCard.billingAddress;
        this.creditCards.push(creditCard);

        try{
            const response = await axiosClient.post(`/customers/${this.email}credit-cards`, {
                creditCard: creditCard,
                email: this.email,
            });
            console.log(response.data);
        } catch(error) {
            console.error(error);
        }

        this.showCreditForm = false;
        this.newCreditCard = {
            cardNumber: '',
            expiryDate: '',
            cvv: '',
            billingAddress: '',
        };
        this.billingAddress = {
            street: '',
            city: '',
            province: '',
            country: '',
            postalCode: '',
        };
    },
    async deleteCard(index, creditCard){
        //method not working yet!!!
        try {
            await AXIOS.delete('/customers/' + this.email + '/credit-cards/' + creditCard.id, {
            data: {
                email: this.email,
                creditCard: creditCard,
            },
            });
            this.creditCards.splice(index, 1);
        } catch (error) {
            console.error('Error deleting credit card:', error);
        }
    },

},
};
</script>