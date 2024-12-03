<template>
    <div class="promotion">
        <br>
        <BTable :items="promotions" :fields="fields2">
        </BTable>
    </div>
</template>

<style scoped src="../../assets/main.css">
</style>

<script>
import axios from 'axios';
import dayjs from 'dayjs';

const frontendURL = 'http://localhost:8087';
const backendURL = 'http://localhost:8080';

const axiosClient = axios.create({
    baseURL: backendURL,
    // headers: {
    //     'Access-Control-Allow-Origin': frontendURL,
    // }
});


const loadPromotions = () => {
  console.log('Page has been refreshed!');
  // Your logic here
};


export default {
  name: "Promotion",
  props: {
    promotion: {
      type: Object,
      required: true,
      validator: (value) =>
          'discount' in value &&
          'startDate' in Date &&
          'endDate' in Date &&
          'promotionId' in value
    },
  },
  data(){
    return{
        promotions: [
            // { promotionId: 1, game: 'Game1', discount: 10, start_date: '2021-10-01', end_date: '2021-10-31' },
            // { promotionId: 2, game: 'Game2', discount: 20, start_date: '2021-10-01', end_date: '2021-10-31' },
        ],
        fields2: [
            {key: 'promotionId', label: 'ID', sortable: true},
            { key: 'discount', label: 'Discount', sortable: true },
            { key: 'start_date', label: 'Start Date', sortable: true },
            { key: 'end_date', label: 'End Date', sortable: true },
            { key: 'actions', label: '' },
        ],
    }
  },
  methods:{
    async fetchPromotions() {
    let response = '';
    try {
        response = await axiosClient.get("/promotions");
        if (response.status === 200) {
            console.log(response.data); 
            
          
            this.promotions = response.data.map(promotion => {
                return {
                    ...promotion,
                    promotionId : `${promotion.id}`,
                    start_date: dayjs(new Date(promotion.startDate)).format('YYYY-MM-DD'), 
                    end_date:  dayjs(new Date(promotion.endDate)).format('YYYY-MM-DD'), 
                    discount: `${promotion.discount}%`,
                    key: 'actions', label: '' 
                };
            });

            console.log(this.promotions); // Check the transformed promotions array
        }
    } catch (error) {
        alert(error.message); // Show error if any
    }
}
  }

};



</script>