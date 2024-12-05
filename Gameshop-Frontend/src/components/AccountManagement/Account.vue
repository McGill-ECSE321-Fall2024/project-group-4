
<template>
    <div class="mainContainer">
        <div class="display-6 my-3" align="center">
            Account Settings
        </div>

        <div class="shadow p-3 mb-5 bg-body rounded" v-if="userRole=='customer'"> <!-- this is for customer -->
            <BForm > 
                <div class="mb-3">
                    <BFormGroup  id="email-label" label="Email:" label-for="input-0">
                        <p class="text-secondary fst-italic" style="font-size:80%">
                            * Note that you cannot change your email address after signing up.
                        </p>
                        <BFormInput id="input-0" type="text" v-model="email" readonly></BFormInput>
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
                    <BFormGroup  id="old-password-label" label="Old Password:" label-for="input-3">
                        <BFormInput id="input-3" type="text" v-model="oldPassword" @input="handleInputChange('oldPassword')" >{{ this.oldPassword }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="new-password-label" label="New Password:" label-for="input-3">
                        <BFormInput id="input-3" type="text" v-model="newPassword" @input="handleInputChange('newPassword')" >{{ this.newPassword }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup id="address-label" label="Addresses:" label-for="input-5">
                    <BListGroup v-if="addresses">
                        <BListGroupItem v-for="(address, index) in addresses" :key="index">
                        {{ formatAddress(address) }}
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
                        {{ formatCreditCard(creditCard) }}
                        <BButton class="delete-btn" @click="deleteCard(creditCard.id)" variant="danger" size="sm">Delete</BButton>
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
                        <BFormSelect v-model="newCreditCard.billingAddress">
                            <BFormSelectOption value="" disabled>Select an address</BFormSelectOption>
                            <BFormSelectOption v-for="(address, index) in addresses" :key="index" :value="address">
                            {{ formatAddress(address) }}
                            </BFormSelectOption>
                            </BFormSelect>
                        </BFormGroup>
                    </BFormGroup>
                    <br>
                    <BButton class="save-info-btn" @click="saveCredit"> Save new credit card</BButton>
                </div>
                <br><br>
                <BButton v-if="showSaveInfoButton" class="save-info-btn" @click="saveInfo">Save Info</BButton>

            </BForm>
        </div>

        <div class="shadow p-3 mb-5 bg-body rounded" v-if="userRole=='employee'"> <!-- this is for employee -->
            <BForm > 

                <div class="mb-3">
                    <BFormGroup  id="username-label" label="Username:" label-for="input-1">
                        <BFormInput id="input-1" type="text" v-model="username" @input="handleInputChange('username')">{{ this.username }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="old-password-label" label="Password:" label-for="input-3">
                        <p class="text-secondary fst-italic" style="font-size:80%">
                            * Note that you cannot change your password as an employee.
                        </p>
                        <BFormInput id="input-3" type="text" v-model="password" readonly >{{ this.password }} </BFormInput>
                    </BFormGroup>  
                </div>  


                <BButton v-if="showSaveInfoButton" class="save-info-btn" @click="saveInfoEmployee">Save Info</BButton>
            </BForm>
        </div>

        <div class="shadow p-3 mb-5 bg-body rounded" v-if="userRole=='manager'"> <!-- this is for manager -->
            <BForm > 
                <div class="mb-3">
                    <BFormGroup  id="username-label" label="Username:" label-for="input-1">
                        <BFormInput id="input-1" type="text" v-model="username" readonly>{{ this.username}} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="password-label" label="Password:" label-for="input-3">
                        <BFormInput id="input-3" type="text" v-model="password" readonly>{{ this.password }} </BFormInput>
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
    // console.log(localStorage.getItem('accountId'));
    // const accountData = axiosClient.get(`/accounts/customers/ids`, parseInt(localStorage.getItem('accountId')));
    return {
        id: '',
        email: '',
        username: '',
        phoneNumber: '',
        oldPassword: '',
        newPassword: '',
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
        selectedEmployee: {
            id: '',
            username: '',
            isActive: false,
        },
        billingAddress: {
            street: '',
            city: '',
            province: '',
            country: '',
            postalCode: '',
        },
        changedField: '',
        userRole: localStorage.getItem('userRole'),
    };
  },
  async created(){
        try {
            const accountId = parseInt(localStorage.getItem('accountId'));
            if (!isNaN(accountId)) {
                if (this.userRole === 'customer'){
                    const response = await axiosClient.get(`/accounts/customers/ids/${accountId}`);
                    const accountData = response.data;
                    this.username = accountData.username;
                    this.email = accountData.email;
                    this.phoneNumber = accountData.phoneNumber;
                    this.addresses = accountData.addresses.filter(address => address.isActive);;
                    this.creditCards = accountData.creditCards.filter(creditCard => creditCard.isActive);
                } else if (this.userRole === 'employee'){
                    const response = await axiosClient.get(`/accounts/employees/id/${accountId}`);
                    const accountData = response.data;
                    this.username = accountData.username;
                    this.oldUsername = accountData.username;
                    this.password = accountData.password;
                    this.oldPassword = accountData.password;
                } else if (this.userRole == 'manager'){
                    // const response = await axiosClient.get(`/accounts/managers/ids/${accountId}`);
                    // const accountData = response.data;
                    this.username = "manager";
                    this.password = "manager";
                }
                // const response = await axiosClient.get(`/accounts/customers/ids/${accountId}`);
                // const accountData = response.data;
                // this.username = accountData.username;
                // this.email = accountData.email;
                // this.phoneNumber = accountData.phoneNumber;
                // this.addresses = accountData.addresses;
                // this.creditCards = accountData.creditCards;
                //console.log('HERE');
            } else {
                console.error('Invalid account ID');
            }
        } catch (error) {
        console.error('Error fetching account data:', error);
        }
    },
  methods: {
    formatCreditCard(creditCard) {
        const cardNumberStr = creditCard.cardNumber.toString();
        let maskedCardNumber = '';
        if (cardNumberStr.length >4 ) {
            maskedCardNumber = '*'.repeat(cardNumberStr.length - 4) + cardNumberStr.slice(-4);}
        else {
            maskedCardNumber = cardNumberStr;
        }
        return `Card Number: ${maskedCardNumber}, Expiry Date: ${creditCard.expiryDate}, CVV: ***, Billing Address: ${this.formatAddress(creditCard.billingAddress)}`;
    },
    formCreditCard(creditCard){
        return `Card Number: ${creditCard.cardNumber}, Expiry Date: ${creditCard.expiryDate}, CVV: ${creditCard.cvv}, Billing Address: ${this.formatAddress(creditCard.billingAddress)}`;
    },
    formatAddress(address) {
      return `${address.street}, ${address.city}, ${address.province}, ${address.country}, ${address.postalCode}`;
    },
    handleInputChange(field) {
        this.changedField = field;
        this.showSaveInfoButton = true;
    },
    async saveInfo() {
        try {
        let response;
        if (this.changedField === 'username') {
          response = await axiosClient.put(`/accounts/customers/${this.email}/username/${this.username}`).then(this.$router.go());
        } else if (this.changedField === 'phoneNumber') {
          response = await axiosClient.put(`/accounts/customers/${this.email}/phoneNumber/${this.phoneNumber}`).then(this.$router.go());
        } else if (this.changedField === 'newPassword') {
            console.log(this.oldPassword);
            console.log(this.newPassword);
          response = await axiosClient.put(`/accounts/customers/${this.email}/password`, {
            oldPassword : this.oldPassword,
            newPassword: this.newPassword
          }).then(this.$router.go());
        }
        //console.log(response);
        this.showSaveInfoButton = false;
      } catch (error) {
        console.error('Error saving info:', error);
      }
    },
    async saveInfoEmployee(){
        try {
            // const response = await axiosClient.put(`/accounts/employees/${this.username}/username/${username}`).then(this.$route.go());
            // console.log(response.data);
            let response;

            if (this.changedField === 'username') {
                response = await axiosClient.put(`/accounts/employees/${this.oldUsername}/username/${this.username}`).then(this.$router.go());
            } 
            this.showSaveInfoButton = false;
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
    async deleteAddress(index,address) {
      
      let response = '';
      try {
        const addressId = address.id;
        console.log(addressId);
        response = await axiosClient.delete(`/accounts/${this.email}/addresses/${addressId}`).then(this.$router.go());
        
      }catch (error) {
            alert(error.response?.data?.errorMessages || error.message || "Something went wrong"); 
        }
      
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

        try{
            const response = await axiosClient.post(`/accounts/customers/${this.email}/addresses`, {
                id : null,
                street : this.newAddress.street,
                city : this.newAddress.city,
                province : this.newAddress.province,
                country : this.newAddress.country,
                postalCode : this.newAddress.postalCode
            }).then(this.$router.go());
        } catch(error) {
            console.error(error);
            alert(error);
        }

        this.showAddressForm = false;
        this.newAddress = {
            street: '',
            city: '',
            province: '',
            country: '',
            postalCode: '',
        };
    },async fetchCreditCards() { 
        let response ="";
        try {
            response = axiosClient.get(`/customers/${localStorage.getItem('email')}/credit-cards`);
            if (response.status === 200) {
                log(response.data);
                this.creditCards = response.data.filter(creditCard => creditCard.isActive)
                return response.data;
            }
        } catch (error) {
            alert(error.response?.data?.errorMessages || error.message || "Something went wrong"); 
        }
    },
    async saveCredit(){
        if (
            !this.newCreditCard.cardNumber ||
            !this.newCreditCard.expiryDate ||
            !this.newCreditCard.cvv ||
            !this.newCreditCard.billingAddress
        ){
            alert('Please fill in all credit card fields.');
            return;
        }
        console.log(this.newCreditCard.billingAddress);
        try{
            const response = await axiosClient.post(`/customers/${this.email}/credit-cards`, {
                cardNumber : parseInt(this.newCreditCard.cardNumber),
                cvv : parseInt(this.newCreditCard.cvv),
                expiryDate : new Date(this.newCreditCard.expiryDate),
                billingAddress :(this.newCreditCard.billingAddress)
            });//.then(this.$router.go());
            console.log(response.data);
            await this.fetchCreditCards();
            this.$router.go();
        } catch(error) {
            alert(error.response?.data?.errorMessages || error.message || "Something went wrong"); 
        }

        this.showCreditForm = false;
        this.newCreditCard = {
            cardNumber: '',
            expiryDate: '',
            cvv: '',
            billingAddress: '',
        };
    },
    async deleteCard(cardId){
        try {
            await axiosClient.delete(`/customers/${localStorage.getItem('email')}/credit-cards/${cardId}`);
            await this.fetchCreditCards();
            this.$router.go();
        } catch (error) {
            alert(error.response?.data?.errorMessages || error.message || "Something went wrong"); 
        }
    },

},async mounted() {
    await this.fetchCreditCards();
}
};
</script>