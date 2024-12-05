<template>
    <div class="mainContainer">
      <div class="display-6 my-3" align="center">
        Past Purchases
      </div>

      <!-- display all purchases -->
        <BTable :items="purchases" :fields="fields" >
        </BTable>
    </div>
</template>


<script>
import axios from 'axios';

const frontendURL = 'http://localhost:8087';
const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
    baseURL: backendURL,
    // headers: {
    //     'Access-Control-Allow-Origin': frontendURL,
    // }
});

export default {
    name: "Purchases",
    data() {
        return {
            purchases: [],
            fields: [
                { key: 'id', label: 'Purchase ID' },
                { key: 'gameTitle', label: 'Game Name' },
                { key: 'customerId', label: 'Customer ID' },
                // { key: 'purchaseDate', label: 'Purchase Date' },
                // { key: 'price', label: 'Price' },
            ]
        }
    },
    methods: {
        // async fetchPurchases() {
        //     const response = await axiosClient.get('/purchases');
        //     this.purchases = response.data;
        // }
    }
}

</script>


<style scoped src="../../assets/main.css">
</style>